package com.leaveease.api.dto.response;

import lombok.Data;

@Data
public class ReportAnalysisResponseDto {
    private int staffId;
    private String staffNumber;
    private String name;
    private String department;
    private int takenChildren;
    private int takenAnnual;
    private int takenSick;
    private int takenEmergency;
    private int total;
}
