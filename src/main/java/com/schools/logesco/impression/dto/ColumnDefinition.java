package com.schools.logesco.impression.dto;

public record ColumnDefinition(
        String field,     // "nom", "prenom", "classe", "telephone"
        String label      // "Nom", "Prénom", "Classe", "Téléphone"
) {}

