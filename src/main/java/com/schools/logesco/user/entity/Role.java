package com.schools.logesco.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    SECRETAIRE("Secrétaire"),
    ECONOME("Econome"),
    ADMINISTRATEUR("Administrateur"),
    DIRECTEUR("Directeur");

    private String label;

    public static Role fromLabel(String label) {
        for (Role r : Role.values()) {
            if (r.getLabel().equalsIgnoreCase(label)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Role inconnu : " + label);
    }
}
