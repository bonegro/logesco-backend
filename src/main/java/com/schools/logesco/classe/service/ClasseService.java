package com.schools.logesco.classe.service;

import com.schools.logesco.classe.dto.ClasseCreateRequest;
import com.schools.logesco.classe.dto.ClasseDto;
import com.schools.logesco.classe.dto.ClasseUpdateRequest;
import com.schools.logesco.classe.entity.Classe;
import com.schools.logesco.classe.repository.ClasseRepository;
import com.schools.logesco.common.ClasseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClasseService {

    private final ClasseRepository repository;

    // -------------------------
    // 1. Créer une classe
    // -------------------------
    public ClasseDto create(ClasseCreateRequest req) {

        Classe classe = Classe.builder()
                .nom(req.nom())
                .niveau(req.niveau())
                .professeurPrincipal(req.professeurPrincipal())
                .build();

        repository.save(classe);

        return ClasseMapper.toDto(classe);
    }

    // -------------------------
    // 2. Lister toutes les classes
    // -------------------------
    public Page<ClasseDto> list(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable)
                .map(ClasseMapper::toDto);
    }

    // -------------------------
    // 3. Consulter une classe
    // -------------------------
    public ClasseDto get(Long id) {
        Classe classe = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Classe introuvable"));
        return ClasseMapper.toDto(classe);
    }

    // -------------------------
    // 4. Modifier une classe
    // -------------------------
    public ClasseDto update(Long id, ClasseUpdateRequest req) {

        Classe classe = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Classe introuvable"));

        classe.setNom(req.nom());
        classe.setNiveau(req.niveau());
        classe.setProfesseurPrincipal(req.professeurPrincipal());

        repository.save(classe);

        return ClasseMapper.toDto(classe);
    }

    // -------------------------
    // 5. Supprimer une classe
    // -------------------------
    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<ClasseDto> search(String search) {
        return repository.findByNomContainingIgnoreCase(search)
                .stream()
                .map(ClasseMapper::toDto)
                .toList();
    }

}

