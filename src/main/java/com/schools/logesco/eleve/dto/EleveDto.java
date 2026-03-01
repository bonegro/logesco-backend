package com.schools.logesco.eleve.dto;

import com.schools.logesco.classe.dto.ClasseDto;
import com.schools.logesco.eleve.entity.Sexe;
import com.schools.logesco.eleve.entity.StatutEleve;
import com.schools.logesco.paiement.entity.StatutPaiement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Builder
public class EleveDto {

    private Long id;
    private String matricule;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String lieuNaissance;
    private Sexe sexe;
    private Boolean redoublant;
    private String quartier;
    private String nomTuteur;
    private String telephoneTuteur;
    private ClasseDto classe;
    private String antecedentsMedicaux;
    private String etatSante;
    private String commentaire;
    private LocalDateTime dateInscription;
    private StatutEleve statut;
    private StatutPaiement statutPaiement;
}
