package com.schools.logesco.classe.dto;

import lombok.Builder;

@Builder
public record ClasseDto(
        Long id,
        String nom,
        String niveau,
        String professeurPrincipal
) {}

