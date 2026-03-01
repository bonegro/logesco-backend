package com.schools.logesco.classe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClasseCreateRequest(

        @NotBlank @Size(max = 50)
        String nom,

        @NotBlank @Size(max = 50)
        String niveau,

        @Size(max = 100)
        String professeurPrincipal
) {}

