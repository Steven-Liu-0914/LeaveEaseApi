package com.leaveease.api.dto.request;

import lombok.Data;

@Data
public class PublicHolidayRequestDto {
    private int id;
    private String name;
    private String date; // YYYY-MM-DD
    private String day;
}