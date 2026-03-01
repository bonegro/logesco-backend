package com.schools.logesco.impression.service;

import com.schools.logesco.eleve.entity.Eleve;
import com.schools.logesco.impression.dto.ColumnDefinition;
import com.schools.logesco.impression.util.ReflectionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ExcelService {

    public byte[] generateExcel(List<Eleve> eleves, List<ColumnDefinition> colonnes) {
        try (Workbook wb = new XSSFWorkbook()) {

            Sheet sheet = wb.createSheet("Liste");
            Row header = sheet.createRow(0);

            // En-têtes
            for (int i = 0; i < colonnes.size(); i++) {
                header.createCell(i).setCellValue(colonnes.get(i).label());
            }

            // Lignes
            int rowIdx = 1;
            for (Eleve e : eleves) {
                Row row = sheet.createRow(rowIdx++);
                for (int i = 0; i < colonnes.size(); i++) {
                    String value = ReflectionUtils.getFieldValue(e, colonnes.get(i).field());
                    row.createCell(i).setCellValue(value);
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erreur génération Excel", e);
        }
    }
}
