
package com.leaveease.api.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PendingLeavesForReviewResponseDto {
    private Integer leaveApplicationId;
    private Integer staffId;
    private String staffNumber;
    private String staffName;
    private String leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String status = "Pending";
    private LocalDate createdAt = LocalDate.now();
}