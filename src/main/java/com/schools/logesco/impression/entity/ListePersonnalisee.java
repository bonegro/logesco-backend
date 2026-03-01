package com.schools.logesco.impression.entity;

import com.schools.logesco.eleve.entity.Eleve;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "listes_personnalisees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListePersonnalisee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @ManyToMany
    @JoinTable(
            name = "liste_personnalisee_eleves",
            joinColumns = @JoinColumn(name = "liste_id"),
            inverseJoinColumns = @JoinColumn(name = "eleve_id")
    )
    private List<Eleve> eleves;
}

