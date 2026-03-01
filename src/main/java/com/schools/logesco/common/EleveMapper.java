package com.schools.logesco.common;

import com.schools.logesco.eleve.dto.EleveDto;
import com.schools.logesco.eleve.entity.Eleve;
import com.schools.logesco.paiement.entity.StatutPaiement;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EleveMapper {

    public static EleveDto toDto(Eleve e) {
        return EleveDto.builder()
                .id(e.getId())
                .matricule(e.getMatricule())
                .nom(e.getNom())
                .prenom(e.getPrenom())
                .dateNaissance(e.getDateNaissance())
                .lieuNaissance(e.getLieuNaissance())
                .sexe(e.getSexe())
                .redoublant(e.isRedoublant())
                .quartier(e.getQuartier())
                .nomTuteur(e.getNomTuteur())
                .telephoneTuteur(e.getTelephoneTuteur())
                .classe(ClasseMapper.toDto(e.getClasse()))
                .antecedentsMedicaux(e.getAntecedentsMedicaux())
                .etatSante(e.getEtatSante())
                .commentaire(e.getCommentaire())
                .dateInscription(e.getDateInscription())
                .statut(e.getStatut())
                .build();
    }

    public static EleveDto toDto(Eleve e, StatutPaiement statut) {
        EleveDto dto = toDto(e);
        dto.setStatutPaiement(statut);
        return dto;
    }
}
