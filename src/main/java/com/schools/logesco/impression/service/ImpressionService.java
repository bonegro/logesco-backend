package com.schools.logesco.impression.service;

import com.schools.logesco.classe.entity.Classe;
import com.schools.logesco.classe.repository.ClasseRepository;
import com.schools.logesco.eleve.entity.Eleve;
import com.schools.logesco.eleve.entity.StatutEleve;
import com.schools.logesco.eleve.repository.EleveRepository;
import com.schools.logesco.impression.dto.FormatType;
import com.schools.logesco.impression.dto.ListeAppelRequest;
import com.schools.logesco.impression.dto.ListePersonnaliseeRequest;
import com.schools.logesco.impression.util.ReflectionUtils;
import com.schools.logesco.paiement.entity.StatutPaiement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImpressionService {

    private final EleveRepository eleveRepository;
    private final ClasseRepository classeRepository;
    private final PdfService pdfService;
    private final ExcelService excelService;

    public byte[] generateListeAppel(final ListeAppelRequest req) {

        Classe classe = classeRepository.findById(req.classeId())
                .orElseThrow(() -> new IllegalArgumentException("Classe introuvable"));

        List<Eleve> eleves = eleveRepository.findByClasseIdOrderByNomAsc(req.classeId());

        return pdfService.generateListeAppelPdf(classe, eleves, req.date());
    }

    public byte[] generateListePersonalisee(ListePersonnaliseeRequest req) {

        List<Eleve> eleves;
        // Filtre paiement
        if (req.paiementStatut() != null) {
            eleves = eleveRepository.findByStatutPaiement(StatutPaiement.valueOf(req.paiementStatut()));
        } else {
            eleves = eleveRepository.findAll();
        }

        // Filtre classe
        if (req.classes() != null && !req.classes().isEmpty()) {
            eleves = eleves.stream()
                    .filter(e -> req.classes().contains(e.getClasse().getId()))
                    .toList();
        }

        // Filtre actifs
        if (req.actifs() != null) {
            eleves = eleves.stream()
                    .filter(e -> e.getStatut().equals(req.actifs()? StatutEleve.ACTIF: StatutEleve.ARCHIVE))
                    .toList();
        }

        // Tri
        if (req.tri() != null) {
            eleves = eleves.stream()
                    .sorted(Comparator.comparing(e -> ReflectionUtils.getFieldValue(e, req.tri())))
                    .toList();
        }

        // Format
        return req.format().equalsIgnoreCase(FormatType.EXCEL.name())
                ? excelService.generateExcel(eleves, req.colonnes())
                : pdfService.generatePdf(eleves, req.colonnes());
    }
}
