package com.schools.logesco.admin.dto;

public record UserUpdateRequest(

        String nom,
        String prenom,

        String role
) {}

