package com.schools.logesco.admin.controller;

import com.schools.logesco.admin.dto.UserCreateRequest;
import com.schools.logesco.admin.dto.UserDto;
import com.schools.logesco.admin.dto.UserUpdateRequest;
import com.schools.logesco.admin.service.AdminService;
import com.schools.logesco.common.ApiDoc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Utilisateurs", description = "Gestion des utilisateurs (ADMIN)")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRATEUR')")
public class AdminController {

    private final AdminService service;

    @Operation(
            summary = "Créer un utilisateur",
            description = "Accessible au rôle ADMINISTRATEUR uniquement"
    )
    @ApiDoc
    @PostMapping
    public UserDto create(@Valid @RequestBody UserCreateRequest request) {
        return service.create(request);
    }

    @Operation(
            summary = "Lister les utilisateurs",
            description = "Accessible au rôle ADMINISTRATEUR uniquement"
    )
    @ApiDoc
    @GetMapping
    public Page<UserDto> list(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "20") int size) {
        return service.list(page, size);
    }

    @Operation(
            summary = "Mettre à jour les informations d'un utilisateur",
            description = "Accessible au rôle ADMINISTRATEUR uniquement"
    )
    @ApiDoc
    @PutMapping("/{id}")
    public UserDto update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        return service.update(id, request);
    }

    @Operation(
            summary = "Récupérer les informations d'un utilisateur",
            description = "Accessible au rôle ADMINISTRATEUR uniquement"
    )
    @ApiDoc
    @GetMapping("/{id}")
    public UserDto getUserDetails(@PathVariable Long id) {
        return service.getUser(id);
    }

    @Operation(
            summary = "Supprimer définitivement un utilisateur",
            description = "Accessible au rôle ADMINISTRATEUR uniquement"
    )
    @ApiDoc
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @Operation(
            summary = "Activer ou désactiver un utilisateur",
            description = "Accessible au rôle ADMINISTRATEUR uniquement"
    )
    @ApiDoc
    @PatchMapping("/{id}/toggle")
    public UserDto toggleActivation(@PathVariable Long id) {
        return service.toggleActivation(id);
    }

    @Operation(
            summary = "Récupération des détails du dashboard",
            description = "Accessible au rôle ADMINISTRATEUR uniquement"
    )
    @ApiDoc
    @GetMapping("/dashboard")
    public Map<String, Object> getDashboard() {
        return service.getDashboardData();
    }
}