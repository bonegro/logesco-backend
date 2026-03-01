package com.schools.logesco.paiement.dto;

import jakarta.validation.constraints.NotBlank;

public record PaiementUpdateRequest(

        @NotBlank
        String statut, // "PAYE", "NON_PAYE", "PARTIEL"

        String commentaire
) {}

