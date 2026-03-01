package com.schools.logesco.auth.controller;

import com.schools.logesco.admin.dto.UserDto;
import com.schools.logesco.admin.service.AdminService;
import com.schools.logesco.common.ApiDoc;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/profil")
public class ProfilController {

    private final AdminService service;

    @Operation(
            summary = "Récupérer les informations de l'utilisateur connecté",
            description = "Accessible à tous les utilisateurs"
    )
    @ApiDoc
    @GetMapping
    UserDto getUserDetails() {
        return service.profil();
    }
}
