package com.leaveease.api.controller;

import com.leaveease.api.dto.request.LeaveApplicationRequestDto;
import com.leaveease.api.model.ApiResponse;
import com.leaveease.api.service.LeaveApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leave")
public class LeaveApplicationController {

    @Autowired
    private LeaveApplicationService service;

    @PostMapping("/apply/{staffId}")
    public ApiResponse<Void> submitLeave(
            @PathVariable int staffId,
            @RequestBody LeaveApplicationRequestDto dto) {

        service.submitLeave(staffId, dto);
        return new ApiResponse<>(null); // no payload, just success
    }

    @PutMapping("/update/{leaveId}")
    public ApiResponse<Void> updateLeave(
            @PathVariable int leaveId,
            @RequestBody LeaveApplicationRequestDto dto) {

        service.updateLeave(leaveId, dto);
        return new ApiResponse<>(null);
    }

    @DeleteMapping("/cancel/{leaveId}")
    public ApiResponse<Void> cancelLeave(@PathVariable int leaveId) {
        service.cancelLeave(leaveId);
        return new ApiResponse<>(null);
    }
}
