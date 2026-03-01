package com.schools.logesco.eleve.repository;

import com.schools.logesco.eleve.entity.Eleve;
import com.schools.logesco.eleve.entity.StatutEleve;
import com.schools.logesco.paiement.entity.StatutPaiement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EleveRepository extends JpaRepository<Eleve, Long> {

    // Lister par statut (ACTIF ou ARCHIVE)
    Page<Eleve> findByStatut(StatutEleve statut, Pageable pageable);

    // Recherche multi-critères : nom, prénom, matricule, tuteur
    @Query("""
        SELECT e FROM Eleve e
        WHERE LOWER(e.nom) LIKE %:q%
           OR LOWER(e.prenom) LIKE %:q%
           OR LOWER(e.matricule) LIKE %:q%
           OR LOWER(e.nomTuteur) LIKE %:q%
    """)
    List<Eleve> search(String q);

    // Vérifier unicité du matricule (utile si on veut une logique métier plus stricte)
    boolean existsByMatricule(String matricule);

    List<Eleve> findByClasseIdAndStatut(Long classeId, StatutEleve statut);

    List<Eleve> findByClasseIdOrderByNomAsc(Long classeId);

    @Query("""
        SELECT e FROM Eleve e
        JOIN Paiement p ON p.eleve.id = e.id
        WHERE p.statut = :statut
    """)
    List<Eleve> findByStatutPaiement(@Param("statut") StatutPaiement statut);
}

