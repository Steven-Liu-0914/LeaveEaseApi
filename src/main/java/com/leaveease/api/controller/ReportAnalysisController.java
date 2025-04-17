package com.leaveease.api.controller;

import com.leaveease.api.dto.request.ReportAnalysisRequestDto;
import com.leaveease.api.dto.response.ReportAnalysisResponseDto;
import com.leaveease.api.model.ApiResponse;
import com.leaveease.api.service.ReportAnalysisService;
import com.leaveease.api.util.ExcelExportUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/report/analysis")
@RequiredArgsConstructor
public class ReportAnalysisController {

    private final ReportAnalysisService service;

    @PostMapping
    public ApiResponse<List<ReportAnalysisResponseDto>> search(@RequestBody ReportAnalysisRequestDto request) {
        return new ApiResponse<>(service.getReport(request));
    }

    @PostMapping("/export")
    public ResponseEntity<byte[]> exportToExcel(@RequestBody ReportAnalysisRequestDto request) {
        List<ReportAnalysisResponseDto> data = service.getReport(request);
        return ExcelExportUtil.exportReportAnalysisToExcel(data, "report-analysis.xlsx");
    }
}