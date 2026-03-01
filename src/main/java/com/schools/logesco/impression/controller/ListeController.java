package com.schools.logesco.impression.controller;

import com.schools.logesco.common.ApiDoc;
import com.schools.logesco.impression.dto.FormatType;
import com.schools.logesco.impression.dto.ListeAppelRequest;
import com.schools.logesco.impression.dto.ListePersonnaliseeRequest;
import com.schools.logesco.impression.service.ImpressionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Listes", description = "Listes d'appel et listes personnalisées")
@RestController
@RequestMapping("/listes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SECRETAIRE','ADMINISTRATEUR','DIRECTEUR')")
public class ListeController {

    private final ImpressionService service;

    @Operation(
            summary = "Générer une liste personalisée PDF ou EXCEL",
            description = "Accessible aux rôles ADMINISTRATEUR, 'DIRECTEUR' et SECRETAIRE"
    )
    @ApiDoc
    @PostMapping("/liste-personnalisee")
    public ResponseEntity<byte[]> listePersonnalisee(@RequestBody ListePersonnaliseeRequest req) {

        byte[] file = service.generateListePersonalisee(req);

        String filename = "liste-personnalisee." +
                (req.format().equals(FormatType.EXCEL.name()) ? "xlsx" : "pdf");

        MediaType type = req.format().equals(FormatType.EXCEL.name())
                ? MediaType.parseMediaType("application/vnd.ms-excel")
                : MediaType.APPLICATION_PDF;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(type)
                .body(file);
    }

    @Operation(
            summary = "Générer un document PDF prêt à imprimer",
            description = "Accessible aux rôles ADMINISTRATEUR, 'DIRECTEUR' et SECRETAIRE"
    )
    @ApiDoc
    @PostMapping("/liste-appel")
    public ResponseEntity<byte[]> listeAppel(@RequestBody ListeAppelRequest req) {

        byte[] pdf = service.generateListeAppel(req);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=liste-appel.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
