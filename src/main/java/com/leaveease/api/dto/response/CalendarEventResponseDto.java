package com.leaveease.api.dto.response;

import lombok.Data;

@Data
public class CalendarEventResponseDto {
    private String start;   // ISO String: yyyy-MM-dd
    private String end;     // ISO String: yyyy-MM-dd
    private String title;   // "John Doe takes Sick Leave"
}
