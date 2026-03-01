package com.schools.logesco.paiement.dto;

import com.schools.logesco.paiement.entity.StatutPaiement;
import lombok.Builder;

@Builder
public record PaiementDto(
        Long id,
        Long eleveId,
        String eleveNomComplet,
        StatutPaiement statut,
        String commentaire
) {}

