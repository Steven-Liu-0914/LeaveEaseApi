package com.leaveease.api.dto.response;

import com.leaveease.api.entity.LeaveApplicationEntity;
import lombok.Data;

import java.util.List;

@Data
public class DashboardResponseDto {
    private int remainingLeave;
    private int upcomingLeave;
    private int totalApplied;
    private List<LeaveHistoryResponseDto> nextUpcomingLeave;
    private List<PendingLeavesForReviewResponseDto> pendingApproveLeave;
}
