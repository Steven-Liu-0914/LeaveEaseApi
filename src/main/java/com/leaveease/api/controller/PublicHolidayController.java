package com.leaveease.api.controller;

import com.leaveease.api.dto.request.PublicHolidayRequestDto;
import com.leaveease.api.dto.request.ReportAnalysisRequestDto;
import com.leaveease.api.dto.response.PublicHolidayResponseDto;
import com.leaveease.api.dto.response.ReportAnalysisResponseDto;
import com.leaveease.api.model.ApiResponse;
import com.leaveease.api.service.PublicHolidayService;
import com.leaveease.api.util.ExcelExportUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public-holiday")
@RequiredArgsConstructor
public class PublicHolidayController {

    private final PublicHolidayService service;

    @GetMapping
    public ApiResponse<List<PublicHolidayResponseDto>> getAll() {
        return new ApiResponse<>(service.getAll());
    }

    @PostMapping
    public ApiResponse<Void> add(@RequestBody PublicHolidayRequestDto dto) {
        service.add(dto);
        return new ApiResponse<>(null);
    }

    @PutMapping
    public ApiResponse<Void> update( @RequestBody PublicHolidayRequestDto dto) {
        service.update(dto);
        return new ApiResponse<>(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable int id) {
        service.delete(id);
        return new ApiResponse<>(null);
    }

    @PostMapping("/export")
    public ResponseEntity<byte[]> exportToExcel(@RequestBody ReportAnalysisRequestDto request) {
        List<PublicHolidayResponseDto> data = service.getAll();
        return ExcelExportUtil.exportPublicHolidayToExcel(data, "public-holiday.xlsx");
    }
}
