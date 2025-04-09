package com.leaveease.api.dto.request;

import lombok.Data;

@Data
public class ReportAnalysisRequestDto {
    private String startDate;   // Optional
    private String endDate;     // Optional
    private String department;  // Optional
    private String keyword;     // Optional (can be staff number or name)
}
