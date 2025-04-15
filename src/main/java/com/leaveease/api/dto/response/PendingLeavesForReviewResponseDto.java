
package com.leaveease.api.dto.response;

import com.leaveease.api.util.CommonEnums;
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
    private String status = CommonEnums.LeaveStatus.PENDING.getValue();
    private LocalDate createdAt = LocalDate.now();
}