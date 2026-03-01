package com.schools.logesco.log.dto;

import com.schools.logesco.eleve.dto.EleveDto;

import java.time.LocalDateTime;

public record LogEntryDto(
        Long id,
        String oldValue,
        String username,
        LocalDateTime date,
        String newValue,
        EleveDto eleveDto,
        String fieldValue
) {}
