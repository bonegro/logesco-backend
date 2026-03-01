package com.schools.logesco.eleve.controller;

import com.schools.logesco.common.ApiDoc;
import com.schools.logesco.common.EleveMapper;
import com.schools.logesco.eleve.dto.EleveCreateRequest;
import com.schools.logesco.eleve.dto.EleveDto;
import com.schools.logesco.eleve.dto.EleveUpdateRequest;
import com.schools.logesco.eleve.dto.SearchRequest;
import com.schools.logesco.eleve.entity.Eleve;
import com.schools.logesco.eleve.service.EleveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Élèves", description = "Gestion des élèves")
@RestController
@RequestMapping("/eleves")
@RequiredArgsConstructor
public class EleveController {

    private final EleveService service;

    @Operation(
            summary = "Créer un élève",
            description = "Accessible aux rôles SECRETAIRE et ADMINISTRATEUR"
    )
    @ApiDoc
    @PostMapping
    @PreAuthorize("hasAnyRole('SECRETAIRE','ADMINISTRATEUR')")
    public EleveDto create(@RequestBody EleveCreateRequest request) {
        Eleve eleve = service.create(request);
        return EleveMapper.toDto(eleve);
    }

    @Operation(
            summary = "Mettre à jour les informations d'un élève",
            description = "Accessible aux rôles SECRETAIRE et ADMINISTRATEUR"
    )
    @ApiDoc
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SECRETAIRE','ADMINISTRATEUR')")
    public EleveDto update(@PathVariable Long id, @RequestBody EleveUpdateRequest request) {
        Eleve eleve = service.update(id, request);
        return EleveMapper.toDto(eleve);
    }

    @Operation(
            summary = "Archiver un élève",
            description = "Accessible aux rôles SECRETAIRE et ADMINISTRATEUR"
    )
    @ApiDoc
    @PostMapping("/{id}/archiver")
    @PreAuthorize("hasAnyRole('SECRETAIRE','ADMINISTRATEUR')")
    public void archiver(@PathVariable Long id, @RequestParam String motif) {
        service.archiver(id, motif);
    }

    @Operation(
            summary = "Rechercher des élèves",
            description = "Accessible aux rôles SECRETAIRE, ECONOME, ADMINISTRATEUR et DIRECTEUR"
    )
    @ApiDoc
    @PostMapping("/search")
    @PreAuthorize("hasAnyRole('SECRETAIRE','ECONOME','ADMINISTRATEUR','DIRECTEUR')")
    public Page<EleveDto> search(
            @RequestBody SearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return service.search(request, page, size);
    }

    @Operation(
            summary = "Récupérer les détails d'un élève",
            description = "Accessible aux rôles SECRETAIRE, ECONOME, ADMINISTRATEUR et DIRECTEUR"
    )
    @ApiDoc
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SECRETAIRE','ECONOME','ADMINISTRATEUR','DIRECTEUR')")
    public EleveDto getEleveDetails(@PathVariable Long id) {
        return EleveMapper.toDto(service.getById(id));
    }

    @Operation(
            summary = "Supprimer un élève",
            description = "Accessible au rôle ADMINISTRATEUR"
    )
    @ApiDoc
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR')")
    public void supprimer(@PathVariable Long id) {
        service.delete(id);
    }
}