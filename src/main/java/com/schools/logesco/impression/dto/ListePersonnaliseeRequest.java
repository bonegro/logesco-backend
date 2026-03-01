package com.schools.logesco.impression.dto;

import java.util.List;

public record ListePersonnaliseeRequest(
        List<Long> classes,
        Boolean actifs,
        String paiementStatut,
        List<ColumnDefinition> colonnes,
        String tri, // "nom", "prenom", "classe"
        String format // "PDF" ou "EXCEL"
) {}
