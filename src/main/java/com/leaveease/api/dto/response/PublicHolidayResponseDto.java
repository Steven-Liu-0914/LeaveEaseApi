package com.leaveease.api.dto.response;

import lombok.Data;

@Data
public class PublicHolidayResponseDto {
    private int id;
    private String name;
    private String date; // YYYY-MM-DD
    private String day;
}
