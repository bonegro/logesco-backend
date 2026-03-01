package com.schools.logesco.paiement.entity;

import com.schools.logesco.eleve.entity.Eleve;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "paiements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eleve_id", nullable = false, unique = true)
    private Eleve eleve;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutPaiement statut; // PAYE / NON_PAYE / PARTIEL

    @Column(length = 255)
    private String commentaire;
}

