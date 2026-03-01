package com.schools.logesco.admin.dto;

import com.schools.logesco.user.entity.Role;
import lombok.Builder;

@Builder
public record UserDto(
        Long id,
        String username,
        Role role,
        boolean enabled,
        String nom,
        String prenom
) {}

