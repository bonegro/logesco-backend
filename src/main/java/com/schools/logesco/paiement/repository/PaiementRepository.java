package com.schools.logesco.paiement.repository;

import com.schools.logesco.paiement.entity.Paiement;
import com.schools.logesco.paiement.entity.StatutPaiement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    Optional<Paiement> findByEleveId(Long eleveId);

    List<Paiement> findByStatut(StatutPaiement statutRecherche);

    boolean existsByEleveId(Long eleveId);

    List<Paiement> findByEleveIdIn(List<Long> eleveIds);
}

