package com.schools.logesco.log.controller;

import com.schools.logesco.common.ApiDoc;
import com.schools.logesco.log.dto.LogEntryDto;
import com.schools.logesco.log.service.LogEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Traçabilité", description = "Gestion de la traçabilité")
@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMINISTRATEUR','DIRECTEUR')")
public class LogEntryController {

    private final LogEntryService logEntryService;

    @Operation(
            summary = "Lister des logs",
            description = "Accessible aux rôles ADMINISTRATEUR et 'DIRECTEUR"
    )
    @ApiDoc
    @GetMapping
    public Page<LogEntryDto> list(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "20") int size) {
        return logEntryService.list(page, size);
    }

}
