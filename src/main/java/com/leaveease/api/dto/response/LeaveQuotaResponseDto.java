package com.leaveease.api.dto.response;

import lombok.Data;

@Data
public class LeaveQuotaResponseDto {
    private int staffId;
    private String staffNumber;
    private String staffName;
    private LeaveQuotas quotas;

    @Data
    public static class LeaveQuotas {
        private int children;
        private int annual;
        private int sick;
        private int emergency;
    }
}
