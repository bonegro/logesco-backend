package com.schools.logesco.admin.service;

import com.schools.logesco.admin.dto.UserCreateRequest;
import com.schools.logesco.admin.dto.UserDto;
import com.schools.logesco.admin.dto.UserUpdateRequest;
import com.schools.logesco.admin.repository.AdminRepository;
import com.schools.logesco.eleve.entity.Eleve;
import com.schools.logesco.eleve.entity.StatutEleve;
import com.schools.logesco.eleve.repository.EleveRepository;
import com.schools.logesco.mail.EmailTemplateService;
import com.schools.logesco.mail.MailService;
import com.schools.logesco.paiement.entity.Paiement;
import com.schools.logesco.paiement.entity.StatutPaiement;
import com.schools.logesco.paiement.repository.PaiementRepository;
import com.schools.logesco.user.entity.Role;
import com.schools.logesco.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final EmailTemplateService emailTemplateService;
    private final MailService mailService;
    private final EleveRepository eleveRepository;
    private final PaiementRepository paiementRepository;

    @Value("${app.base-url}")
    private String appUrl;

    // -------------------------
    // 1. Créer un utilisateur
    // -------------------------
    public UserDto create(final UserCreateRequest req) {

        if (repository.existsByUsername(req.username())) {
            throw new IllegalArgumentException("Nom d'utilisateur déjà utilisé");
        }

        String tempPassword = RandomStringUtils.randomAlphanumeric(10);
        User user = new User();
        user.setUsername(req.username());
        user.setPassword(passwordEncoder.encode(tempPassword));
        user.setRole(Role.valueOf(req.role()));
        user.setEnabled(true);
        user.setNom(req.nom());
        user.setPrenom(req.prenom());

        repository.save(user);

        String html = emailTemplateService.userCreated(user.getUsername(), tempPassword, appUrl);

        mailService.send(user.getUsername(), "Vos identifiants de connexion", html);

        return toDto(user);
    }

    // -------------------------
    // 2. Lister les utilisateurs
    // -------------------------
    public Page<UserDto> list(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable)
                .map(this::toDto);
    }

    // -------------------------
    // 3. Modifier un utilisateur
    // -------------------------
    public UserDto update(Long id, UserUpdateRequest req) {

        User user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        if(req.nom() != null) {
            user.setNom(req.nom());
        }

        if(req.prenom() != null) {
            user.setPrenom(req.prenom());
        }
        if(req.role() != null) {
            user.setRole(Role.valueOf(req.role()));
        }

        repository.save(user);

        return toDto(user);
    }

    // -------------------------
    // 4. Supprimer un utilisateur
    // -------------------------
    public void delete(Long id) {
        repository.deleteById(id);
    }

    // -------------------------
    // 5. Activer / désactiver un utilisateur
    // -------------------------
    public UserDto toggleActivation(Long id) {

        User user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        user.setEnabled(!user.isEnabled());
        repository.save(user);

        return toDto(user);
    }

    public UserDto getUser(Long id) {
        return repository.findById(id).map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
    }

    public Map<String, Object> getDashboardData() {

        List<Eleve> eleves = eleveRepository.findAll();
        List<Paiement> paiements = paiementRepository.findAll();

        Map<String, Object> data = new HashMap<>();

        // 1. Total élèves actifs
        long totalActifs = eleves.stream()
                .filter(e -> e.getStatut() == StatutEleve.ACTIF)
                .count();
        data.put("totalElevesActifs", totalActifs);

        // 2. Élèves par classe
        List<Map<String, Object>> elevesParClasse = eleves.stream()
                .filter(e -> e.getStatut() == StatutEleve.ACTIF)
                .filter(e -> e.getClasse() != null)
                .collect(Collectors.groupingBy(
                        e -> e.getClasse().getNom() + e.getClasse().getNiveau(),
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .map(entry -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("classe", entry.getKey());
                    m.put("nombre", entry.getValue());
                    return m;
                })
                .toList();

        data.put("elevesParClasse", elevesParClasse);

        // 3. Élèves impayés
        long impayes = paiements.stream()
                .filter(p -> p.getStatut() == StatutPaiement.PAYE)
                .count();

        data.put("elevesImpayes", eleves.size() - impayes);

        // 4. Alertes
        List<String> alertes = new ArrayList<>();

        long sansClasse = eleves.stream()
                .filter(e -> e.getClasse() == null)
                .count();
        if (sansClasse > 0) {
            alertes.add(sansClasse + " élèves sans classe");
        }

        long sansResponsable = eleves.stream()
                .filter(e -> e.getNomTuteur() == null || e.getNomTuteur().isBlank())
                .filter(e -> e.getTelephoneTuteur() == null || e.getTelephoneTuteur().isBlank())
                .count();
        if (sansResponsable > 0) {
            alertes.add(sansResponsable + " élèves sans responsable légal");
        }

        data.put("alertes", alertes);

        return data;
    }

    public UserDto profil() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = repository.findByUsername(userDetails.getUsername()).orElseThrow();
        return toDto(user);
    }

    // -------------------------
    // Mapper interne
    // -------------------------
    private UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .build();
    }
}
