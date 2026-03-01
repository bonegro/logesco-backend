package com.schools.logesco.eleve.repository;

import com.schools.logesco.classe.entity.Classe;
import com.schools.logesco.common.EleveMapper;
import com.schools.logesco.eleve.dto.EleveDto;
import com.schools.logesco.eleve.dto.SearchRequest;
import com.schools.logesco.eleve.entity.Eleve;
import com.schools.logesco.paiement.entity.Paiement;
import com.schools.logesco.paiement.entity.StatutPaiement;
import com.schools.logesco.paiement.repository.PaiementRepository;
import com.schools.logesco.paiement.service.PaiementService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class EleveCriteriaRepository {

    private final EntityManager em;
    private final PaiementRepository paiementRepository;
    private final PaiementService paiementService;

    public Page<EleveDto> search(SearchRequest req, Pageable pageable) {

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Eleve> cq = cb.createQuery(Eleve.class);
        Root<Eleve> root = cq.from(Eleve.class);

        List<Predicate> predicates = buildPredicates(req, cb, root);
        cq.where(predicates.toArray(new Predicate[0]));

        List<Eleve> results = em.createQuery(cq).getResultList();

        // Si aucun élève → retour direct
        if (results.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        // 🔵 Préchargement des paiements (1 seule requête)
        List<Long> eleveIds = results.stream()
                .map(Eleve::getId)
                .toList();

        Map<Long, Paiement> paiementMap = paiementRepository
                .findByEleveIdIn(eleveIds)
                .stream()
                .collect(Collectors.toMap(
                        p -> p.getEleve().getId(),
                        p -> p
                ));

        // 🔵 Filtrage si un statut est demandé
        if (req.statutPaiement() != null) {
            results = results.stream()
                    .filter(e -> {
                        Paiement p = paiementMap.get(e.getId());
                        return p != null && p.getStatut() == req.statutPaiement();
                    })
                    .toList();
        }

        // 🔵 Mapping vers DTO avec statut réel
        List<EleveDto> dtoList = results.stream()
                .map(e -> {
                    Paiement p = paiementMap.get(e.getId());
                    StatutPaiement statut = (p != null) ? p.getStatut() : StatutPaiement.NON_PAYE;
                    return EleveMapper.toDto(e, statut);
                })
                .toList();

        // 🔵 Pagination manuelle
        long total = dtoList.size();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), dtoList.size());
        List<EleveDto> pageContent = start > end ? List.of() : dtoList.subList(start, end);

        return new PageImpl<>(pageContent, pageable, total);
    }

    private List<Predicate> buildPredicates(SearchRequest req, CriteriaBuilder cb, Root<Eleve> root) {
        List<Predicate> predicates = new ArrayList<>();

        // nom
        if (req.nom() != null && !req.nom().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("nom")), "%" + req.nom().toLowerCase() + "%"));
        }

        // prenom
        if (req.prenom() != null && !req.prenom().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("prenom")), "%" + req.prenom().toLowerCase() + "%"));
        }

        // sexe
        if (req.sexe() != null && !req.sexe().isBlank()) {
            predicates.add(cb.equal(root.get("sexe"), req.sexe()));
        }

        // redoublant
        if (req.redoublant() != null) {
            predicates.add(cb.equal(root.get("redoublant"), req.redoublant()));
        }

        // quartier
        if (req.quartier() != null && !req.quartier().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("quartier")), "%" + req.quartier().toLowerCase() + "%"));
        }

        // nom du tuteur
        if (req.nomTuteur() != null && !req.nomTuteur().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("nomTuteur")), "%" + req.prenom().toLowerCase() + "%"));
        }

        // matricule
        if (req.matricule() != null && !req.matricule().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("matricule")), "%" + req.matricule().toLowerCase() + "%"));
        }

        // classe
        if (req.classeId() != null) {
            Join<Eleve, Classe> classeJoin = root.join("classe", JoinType.LEFT);
            predicates.add(cb.equal(classeJoin.get("id"), req.classeId()));
        }

        // année d'inscription
        // année d'inscription
        if (req.annee() != null && !req.annee().isBlank()) {
            Integer annee = Integer.valueOf(req.annee());
            predicates.add(
                    cb.equal(
                            cb.function("YEAR", Integer.class, root.get("dateInscription")),
                            annee
                    )
            );
        }

        return predicates;
    }
}
