package com.schools.logesco.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(

        @NotBlank @Size(max = 50)
        String username,

        @NotBlank
        String role,

        @NotBlank
        String nom,

        String prenom
) {}