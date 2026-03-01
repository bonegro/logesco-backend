package com.schools.logesco.eleve.dto;

import com.schools.logesco.paiement.entity.StatutPaiement;

public record SearchRequest(

        String nom,

        String prenom,

        String sexe,

        Boolean redoublant,

        String quartier,

        String nomTuteur,

        String matricule,

        Long classeId,

        StatutPaiement statutPaiement,

        String annee
) {
}
