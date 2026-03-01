package com.schools.logesco.eleve.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schools.logesco.classe.entity.Classe;
import com.schools.logesco.classe.repository.ClasseRepository;
import com.schools.logesco.common.EleveMapper;
import com.schools.logesco.eleve.dto.EleveCreateRequest;
import com.schools.logesco.eleve.dto.EleveDto;
import com.schools.logesco.eleve.dto.EleveUpdateRequest;
import com.schools.logesco.eleve.dto.SearchRequest;
import com.schools.logesco.eleve.entity.Eleve;
import com.schools.logesco.eleve.entity.MotifArchivage;
import com.schools.logesco.eleve.entity.Sexe;
import com.schools.logesco.eleve.entity.StatutEleve;
import com.schools.logesco.eleve.repository.EleveCriteriaRepository;
import com.schools.logesco.eleve.repository.EleveRepository;
import com.schools.logesco.log.service.LogEntryService;
import com.schools.logesco.paiement.entity.Paiement;
import com.schools.logesco.paiement.entity.StatutPaiement;
import com.schools.logesco.paiement.repository.PaiementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class EleveService {

    private final EleveRepository eleveRepository;
    private final ClasseRepository classeRepository;
    private final LogEntryService logEntryService;
    private final EleveCriteriaRepository criteriaRepository;
    private final PaiementRepository paiementRepository;
    private final ObjectMapper mapper;

    // -------------------------
    // 1. Création d’un élève
    // -------------------------
    @Transactional
    public Eleve create(EleveCreateRequest req) {

        Classe classe = classeRepository.findById(req.classeId())
                .orElseThrow(() -> new IllegalArgumentException("Classe introuvable"));

        LocalDateTime dateInscription = LocalDateTime.now();

        if (req.annee() != null && !req.annee().isBlank() && Integer.parseInt(req.annee()) != dateInscription.getYear()) {
            dateInscription = dateInscription.withYear(Integer.parseInt(req.annee()));
        }

        Eleve eleve = Eleve.builder()
                .matricule(generateMatricule())
                .nom(req.nom().trim())
                .prenom(req.prenom().trim())
                .dateNaissance(req.dateNaissance())
                .lieuNaissance(req.lieuNaissance().trim())
                .sexe(Sexe.valueOf(req.sexe()))
                .redoublant(req.redoublant())
                .quartier(req.quartier().trim())
                .nomTuteur(req.nomTuteur().trim())
                .telephoneTuteur(req.telephoneTuteur().trim())
                .classe(classe)
                .antecedentsMedicaux(req.antecedentsMedicaux())
                .etatSante(req.etatSante())
                .commentaire(req.commentaire())
                .dateInscription(dateInscription)
                .statut(StatutEleve.ACTIF)
                .build();

        Eleve created = eleveRepository.save(eleve);
        Paiement paiement = Paiement.builder()
                .eleve(created)
                .statut(req.hasPaid()? StatutPaiement.PAYE: StatutPaiement.NON_PAYE)
                .build();
        paiementRepository.save(paiement);

        return created;
    }

    // -------------------------
    // 2. Modifier un élève
    // -------------------------
    @Transactional
    public Eleve update(Long id, EleveUpdateRequest req) {
        log.info("Objet reçu: {}", req);

        Eleve eleve = eleveRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Élève introuvable"));

        Eleve beforeUpdate = deepCopy(eleve);

        // Vérification classe
        if (req.classeId() != null) {
            Classe classe = classeRepository.findById(req.classeId())
                    .orElseThrow(() -> new IllegalArgumentException("Classe introuvable"));
            eleve.setClasse(classe);
        }

        // Champs simples
        updateIfPresent(req.nom(), eleve::setNom);
        updateIfPresent(req.prenom(), eleve::setPrenom);
        updateIfPresent(req.dateNaissance(), eleve::setDateNaissance);
        updateIfPresent(req.lieuNaissance(), eleve::setLieuNaissance);
        updateIfPresent(req.quartier(), eleve::setQuartier);
        updateIfPresent(req.nomTuteur(), eleve::setNomTuteur);
        updateIfPresent(req.telephoneTuteur(), eleve::setTelephoneTuteur);
        updateIfPresent(req.antecedentsMedicaux(), eleve::setAntecedentsMedicaux);
        updateIfPresent(req.etatSante(), eleve::setEtatSante);
        updateIfPresent(req.commentaire(), eleve::setCommentaire);

        // Enum sécurisé
        if (req.sexe() != null) {
            try {
                eleve.setSexe(Sexe.valueOf(req.sexe()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Sexe invalide : " + req.sexe());
            }
        }

        // Booléen
        if (req.redoublant() != null) {
            eleve.setRedoublant(req.redoublant());
        }

        log.info("Entity construit : {}", eleve);
        Eleve updated = eleveRepository.save(eleve);
        log.info("DTO retourné : {}", EleveMapper.toDto(updated));

        logEntryService.logEleveChanges(beforeUpdate, updated, getUserConnected());

        return updated;
    }

    // -------------------------
    // 3. Archiver un élève
    // -------------------------
    @Transactional
    public void archiver(Long id, String motif) {
        Eleve eleve = eleveRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Élève introuvable"));

        Eleve beforeUpdate = deepCopy(eleve);

        eleve.setStatut(StatutEleve.ARCHIVE);
        eleve.setMotifArchivage(MotifArchivage.valueOf(motif));
        eleve.setDateArchivage(LocalDateTime.now());
        Eleve updated = eleveRepository.save(eleve);

        String username = getUserConnected();

        // Traçabilité des modifications
        logEntryService.logEleveChanges(beforeUpdate, updated, username);
    }

    // -------------------------
    // 4. Rechercher un élève
    // -------------------------
    public Page<EleveDto> search(SearchRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return criteriaRepository.search(request, pageable);
    }

    // -------------------------
    // 5. Consulter un élève
    // -------------------------
    public Eleve getById(Long id) {
        return eleveRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Élève introuvable"));
    }

    // -------------------------
    // 5. Supprimer définitivement un élève
    // -------------------------
    public void delete(Long id) {
        eleveRepository.deleteById(id);
    }

    // -------------------------
    // Génération matricule
    // -------------------------
    private String generateMatricule() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase() + java.time.Year.now().getValue();
    }

    private Eleve deepCopy(Eleve eleve) {
        return mapper.convertValue(mapper.convertValue(eleve, Map.class), Eleve.class);
    }

    private String getUserConnected() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private <T> void updateIfPresent(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

}
