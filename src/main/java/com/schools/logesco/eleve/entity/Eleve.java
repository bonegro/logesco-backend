package com.schools.logesco.eleve.entity;

import com.schools.logesco.classe.entity.Classe;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "eleves")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Eleve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Identifiant unique métier (matricule)
    @Column(name = "matricule", unique = true, nullable = false, length = 30)
    private String matricule;

    @Column(name = "nom", nullable = false, length = 50)
    private String nom;

    @Column(name = "prenom", length = 50)
    private String prenom;

    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    @Column(name = "lieu_naissance", nullable = false, length = 100)
    private String lieuNaissance;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexe", nullable = false, length = 10)
    private Sexe sexe;

    @Column(name = "redoublant", nullable = false)
    private boolean redoublant;

    @Column(name = "quartier", nullable = false, length = 100)
    private String quartier;

    @Column(name = "nom_tuteur", length = 100)
    private String nomTuteur;

    @Column(name = "telephone_tuteur", length = 20)
    private String telephoneTuteur;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "classe_id", nullable = false)
    private Classe classe;

    @Column(name = "antecedents_medicaux", length = 500)
    private String antecedentsMedicaux;

    @Column(name = "etat_sante", length = 500)
    private String etatSante;

    @Column(name = "commentaire", length = 500)
    private String commentaire;

    @Column(name = "date_inscription", nullable = false)
    private LocalDateTime dateInscription;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 20)
    private StatutEleve statut;

    @Column(name = "date_archivage")
    private LocalDateTime dateArchivage;

    @Enumerated(EnumType.STRING)
    @Column(name = "motif_archivage", length = 20)
    private MotifArchivage motifArchivage;
}

