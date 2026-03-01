package com.schools.logesco.paiement.service;

import com.schools.logesco.eleve.entity.Eleve;
import com.schools.logesco.eleve.repository.EleveRepository;
import com.schools.logesco.paiement.dto.PaiementDto;
import com.schools.logesco.paiement.dto.PaiementUpdateRequest;
import com.schools.logesco.paiement.entity.Paiement;
import com.schools.logesco.paiement.entity.StatutPaiement;
import com.schools.logesco.paiement.repository.PaiementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaiementService {

    private final PaiementRepository paiementRepository;
    private final EleveRepository eleveRepository;

    // -------------------------
    // 1. Consulter le paiement d’un élève
    // -------------------------
    public PaiementDto getPaiement(Long eleveId) {

        Paiement paiement = paiementRepository.findByEleveId(eleveId)
                .orElseThrow(() -> new IllegalArgumentException("Paiement introuvable"));

        return toDto(paiement);
    }

    // -------------------------
    // 2. Modifier le statut de paiement
    // -------------------------
    @Transactional
    public PaiementDto updateStatut(Long eleveId, PaiementUpdateRequest req) {

        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new IllegalArgumentException("Élève introuvable"));

        Paiement paiement = paiementRepository.findByEleveId(eleveId)
                .orElse(Paiement.builder()
                        .eleve(eleve)
                        .statut(StatutPaiement.NON_PAYE)
                        .build()
                );

        paiement.setStatut(StatutPaiement.valueOf(req.statut()));
        paiement.setCommentaire(req.commentaire());

        paiementRepository.save(paiement);

        return toDto(paiement);
    }

    public boolean existByEleveId(final Long id) {
        return paiementRepository.existsByEleveId(id);
    }

    // -------------------------
    // Mapper interne
    // -------------------------
    private PaiementDto toDto(Paiement p) {
        return PaiementDto.builder()
                .id(p.getId())
                .eleveId(p.getEleve().getId())
                .eleveNomComplet(p.getEleve().getNom() + " " + p.getEleve().getPrenom())
                .statut(p.getStatut())
                .commentaire(p.getCommentaire())
                .build();
    }
}

