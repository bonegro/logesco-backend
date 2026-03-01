package com.schools.logesco.classe.repository;

import com.schools.logesco.classe.entity.Classe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClasseRepository extends JpaRepository<Classe, Long> {
    boolean existsByNom(String nom);
    List<Classe> findByNomContainingIgnoreCase(String nom);
}