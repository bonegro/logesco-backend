package com.schools.logesco.eleve.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EleveCreateRequest(

        @NotBlank
        @Size(max = 50)
        String nom,

        @NotBlank @Size(max = 50)
        String prenom,

        @NotNull
        LocalDate dateNaissance,

        @NotBlank @Size(max = 100)
        String lieuNaissance,

        @NotNull
        String sexe, // "MASCULIN" / "FEMININ"

        @NotNull
        Boolean redoublant,

        @NotBlank @Size(max = 100)
        String quartier,

        @NotBlank @Size(max = 100)
        String nomTuteur,

        @NotBlank @Size(max = 20)
        @Pattern(regexp = "^[0-9+()\\-\\s]{6,20}$",
                message = "Format de téléphone invalide")
        String telephoneTuteur,

        @NotNull
        Long classeId,

        @Size(max = 500)
        String antecedentsMedicaux,

        @Size(max = 500)
        String etatSante,

        @Size(max = 500)
        String commentaire,

        String annee,

        Boolean hasPaid
) {}

