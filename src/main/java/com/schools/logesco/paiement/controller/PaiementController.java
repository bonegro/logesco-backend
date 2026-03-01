package com.schools.logesco.paiement.controller;

import com.schools.logesco.paiement.dto.PaiementDto;
import com.schools.logesco.paiement.dto.PaiementUpdateRequest;
import com.schools.logesco.paiement.service.PaiementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Paiements", description = "Gestion des paiements des élèves")
@RestController
@RequestMapping("/paiements")
@RequiredArgsConstructor
public class PaiementController {

    private final PaiementService service;

    @Operation(
            summary = "Consulter le paiement d'un élève",
            description = "Accessible aux rôles ECONOME, ADMINISTRATEUR et DIRECTEUR"
    )
    @GetMapping("/{eleveId}")
    @PreAuthorize("hasAnyRole('ECONOME','ADMINISTRATEUR','DIRECTEUR')")
    public PaiementDto getPaiement(@PathVariable Long eleveId) {
        return service.getPaiement(eleveId);
    }

    @Operation(
            summary = "Modifier le statut de paiement",
            description = "Accessible aux rôles ECONOME et ADMINISTRATEUR"
    )
    @PutMapping("/{eleveId}/statut")
    @PreAuthorize("hasAnyRole('ECONOME','ADMINISTRATEUR')")
    public PaiementDto updateStatut(@PathVariable Long eleveId,
                                    @Valid @RequestBody PaiementUpdateRequest request) {
        return service.updateStatut(eleveId, request);
    }
}
