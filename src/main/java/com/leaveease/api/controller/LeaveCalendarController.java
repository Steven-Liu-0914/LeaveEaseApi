package com.leaveease.api.controller;

import com.leaveease.api.dto.response.CalendarEventResponseDto;
import com.leaveease.api.model.ApiResponse;
import com.leaveease.api.service.LeaveCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class LeaveCalendarController {

    private final LeaveCalendarService service;

    @GetMapping
    public ApiResponse<List<CalendarEventResponseDto>> getCalendarByMonth(
            @RequestParam int year,
            @RequestParam int month) {
        List<CalendarEventResponseDto> events = service.getCalendarByMonth(year, month);
        return new ApiResponse<>(events);
    }
}
