package com.leaveease.api.controller;

import com.leaveease.api.dto.response.DashboardResponseDto;
import com.leaveease.api.service.DashboardService;
import com.leaveease.api.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService service;

    @GetMapping("/{staffId}")
    public ApiResponse<DashboardResponseDto> getDashboard(@PathVariable int staffId) {
        DashboardResponseDto dto = service.getDashboardData(staffId);
        return new ApiResponse<>(dto);
    }
}
