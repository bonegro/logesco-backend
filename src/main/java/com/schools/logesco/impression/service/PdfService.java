package com.schools.logesco.impression.service;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.schools.logesco.classe.entity.Classe;
import com.schools.logesco.eleve.entity.Eleve;
import com.schools.logesco.impression.dto.ColumnDefinition;
import com.schools.logesco.impression.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PdfService {

    public byte[] generateListeAppelPdf(Classe classe, List<Eleve> eleves, LocalDate date) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);

        document.open();

        // Titre
        Paragraph title = new Paragraph(
                "Liste d'appel - " + classe.getNom() + classe.getNiveau(),
                new Font(Font.HELVETICA, 18, Font.BOLD)
        );
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("École : Mon École Privée"));
        document.add(new Paragraph("Classe : " + classe.getNom() + classe.getNiveau()));
        document.add(new Paragraph("Date : " + (date != null ? date : LocalDate.now())));
        document.add(Chunk.NEWLINE);

        // Tableau
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);

        Stream.of("N°", "Nom", "Prénom", "Absence", "Observations")
                .forEach(col -> {
                    PdfPCell cell = new PdfPCell(new Phrase(col));
                    cell.setBackgroundColor(Color.LIGHT_GRAY);
                    table.addCell(cell);
                });

        int i = 1;
        for (Eleve e : eleves) {
            table.addCell(String.valueOf(i++));
            table.addCell(e.getNom());
            table.addCell(e.getPrenom());
            table.addCell(""); // vide
            table.addCell(""); // vide
        }

        document.add(table);

        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Total élèves : " + eleves.size()));

        document.close();
        return out.toByteArray();
    }

    public byte[] generatePdf(List<Eleve> eleves, List<ColumnDefinition> colonnes) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc, out);
        doc.open();

        PdfPTable table = new PdfPTable(colonnes.size());
        table.setWidthPercentage(100);

        // En-têtes
        for (ColumnDefinition col : colonnes) {
            PdfPCell cell = new PdfPCell(new Phrase(col.label()));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            table.addCell(cell);
        }

        // Lignes
        for (Eleve e : eleves) {
            for (ColumnDefinition col : colonnes) {
                table.addCell(ReflectionUtils.getFieldValue(e, col.field()));
            }
        }

        doc.add(table);
        doc.close();
        return out.toByteArray();
    }

}
