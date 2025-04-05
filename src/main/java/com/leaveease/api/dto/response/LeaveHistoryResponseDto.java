package com.leaveease.api.dto.response;

import com.leaveease.api.entity.LeaveApplicationEntity;
import lombok.Data;

@Data
public class LeaveHistoryResponseDto {
    private Integer leaveApplicationId;
    private String leaveType;
    private String startDate;
    private String endDate;
    private String reason;
    private String status;

    public static LeaveHistoryResponseDto toDto(LeaveApplicationEntity entity) {
        LeaveHistoryResponseDto dto = new LeaveHistoryResponseDto();
        dto.setLeaveApplicationId(entity.getLeaveApplicationId());
        dto.setLeaveType(entity.getLeaveType());
        dto.setStartDate(entity.getStartDate().toString());
        dto.setEndDate(entity.getEndDate().toString());
        dto.setReason(entity.getReason());
        dto.setStatus(entity.getStatus());
        return dto;
    }

}