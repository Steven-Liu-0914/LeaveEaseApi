package com.leaveease.api.dto.request;

import com.leaveease.api.dto.response.LeaveQuotaResponseDto;
import lombok.Data;

@Data
public class LeaveQuotaUpdateRequestDto {
    private int staffId;
    private LeaveQuotaResponseDto.LeaveQuotas quotas;
}
