package com.schools.logesco.classe.controller;

import com.schools.logesco.classe.dto.ClasseCreateRequest;
import com.schools.logesco.classe.dto.ClasseDto;
import com.schools.logesco.classe.dto.ClasseUpdateRequest;
import com.schools.logesco.classe.service.ClasseService;
import com.schools.logesco.common.ApiDoc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Classes", description = "Gestion des classes")
@RestController
@RequestMapping("/classes")
@RequiredArgsConstructor
public class ClasseController {

    private final ClasseService service;

    @Operation(
            summary = "Créer une classe",
            description = "Accessible aux rôles ADMINISTRATEUR, 'DIRECTEUR' et SECRETAIRE"
    )
    @ApiDoc
    @PostMapping
    @PreAuthorize("hasAnyRole('SECRETAIRE','ADMINISTRATEUR','DIRECTEUR')")
    public ClasseDto create(@Valid @RequestBody ClasseCreateRequest request) {
        return service.create(request);
    }

    @Operation(
            summary = "Lister toutes classes créées",
            description = "Accessible aux rôles ADMINISTRATEUR, 'DIRECTEUR' et SECRETAIRE"
    )
    @ApiDoc
    @GetMapping
    @PreAuthorize("hasAnyRole('SECRETAIRE','ADMINISTRATEUR','DIRECTEUR')")
    public Page<ClasseDto> list(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "20") int size) {
        return service.list(page, size);
    }

    @Operation(
            summary = "Récupérer les informations d'une classe",
            description = "Accessible aux rôles ADMINISTRATEUR, 'DIRECTEUR' et SECRETAIRE"
    )
    @ApiDoc
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SECRETAIRE','ADMINISTRATEUR','DIRECTEUR')")
    public ClasseDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @Operation(
            summary = "Mettre à jour les informations d'une classe",
            description = "Accessible aux rôles ADMINISTRATEUR, 'DIRECTEUR' et SECRETAIRE"
    )
    @ApiDoc
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SECRETAIRE','ADMINISTRATEUR','DIRECTEUR')")
    public ClasseDto update(@PathVariable Long id, @Valid @RequestBody ClasseUpdateRequest request) {
        return service.update(id, request);
    }

    @Operation(
            summary = "Supprimer une classe",
            description = "Accessible aux rôles ADMINISTRATEUR, 'DIRECTEUR' et SECRETAIRE"
    )
    @ApiDoc
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SECRETAIRE','ADMINISTRATEUR','DIRECTEUR')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @Operation(
            summary = "Chercher une classe par nom",
            description = "Accessible aux rôles ADMINISTRATEUR, 'DIRECTEUR' et SECRETAIRE"
    )
    @ApiDoc
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('SECRETAIRE','ADMINISTRATEUR','DIRECTEUR')")
    public List<ClasseDto> search(@RequestParam String q) {
        return service.search(q);
    }
}

