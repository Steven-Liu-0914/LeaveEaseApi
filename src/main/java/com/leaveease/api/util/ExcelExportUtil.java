package com.leaveease.api.util;

import com.leaveease.api.dto.response.ReportAnalysisResponseDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ExcelExportUtil {

    public static ResponseEntity<byte[]> exportToExcel(List<ReportAnalysisResponseDto> data, String filename) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Report");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"Employee No.", "Name", "Department", "Taken Children", "Taken Annual", "Taken Sick", "Taken Emergency", "Total"};

            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            int rowIdx = 1;
            for (ReportAnalysisResponseDto dto : data) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(dto.getStaffNumber());
                row.createCell(1).setCellValue(dto.getName());
                row.createCell(2).setCellValue(dto.getDepartment());
                row.createCell(3).setCellValue(dto.getTakenChildren());
                row.createCell(4).setCellValue(dto.getTakenAnnual());
                row.createCell(5).setCellValue(dto.getTakenSick());
                row.createCell(6).setCellValue(dto.getTakenEmergency());
                row.createCell(7).setCellValue(dto.getTotal());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(outputStream.toByteArray());

        } catch (Exception ex) {
            throw new RuntimeException("Failed to export to Excel");
        }
    }
}
