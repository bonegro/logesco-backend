package com.schools.logesco.impression.dto;

import java.time.LocalDate;

public record ListeAppelRequest(
        Long classeId,
        LocalDate date
) {}
