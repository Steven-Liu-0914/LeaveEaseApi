package com.leaveease.api.controller;

import com.leaveease.api.dto.response.LeaveHistoryResponseDto;
import com.leaveease.api.model.ApiResponse;
import com.leaveease.api.service.LeaveHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leave/history")
@RequiredArgsConstructor
public class LeaveHistoryController {

    private final LeaveHistoryService leaveHistoryService;

    @GetMapping("/{staffId}")
    public ApiResponse<List<LeaveHistoryResponseDto>> getLeaveHistory(@PathVariable Integer staffId) {
        List<LeaveHistoryResponseDto> data = leaveHistoryService.getLeaveHistory(staffId);
        return ApiResponse.of(data);
    }
}
