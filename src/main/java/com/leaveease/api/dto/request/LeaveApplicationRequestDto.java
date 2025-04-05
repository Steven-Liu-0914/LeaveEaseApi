package com.leaveease.api.dto.request;

import lombok.Data;

@Data
public class LeaveApplicationRequestDto {
    private String leaveType;
    private String startDate;
    private String endDate;
    private String reason;
}
