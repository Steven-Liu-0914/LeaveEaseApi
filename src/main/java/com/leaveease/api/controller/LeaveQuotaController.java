package com.leaveease.api.controller;

import com.leaveease.api.dto.request.LeaveQuotaUpdateRequestDto;
import com.leaveease.api.dto.response.LeaveQuotaResponseDto;
import com.leaveease.api.entity.LeaveQuotaEntity;
import com.leaveease.api.model.ApiResponse;
import com.leaveease.api.service.LeaveQuotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/leave/quota")
@RequiredArgsConstructor
public class LeaveQuotaController {

    private final LeaveQuotaService service;

    @GetMapping
    public ApiResponse<List<LeaveQuotaResponseDto>> getAllQuotas(@RequestParam int requestedBy) {
        return new ApiResponse<>(service.getAllQuotas(requestedBy));
    }

    @PutMapping()
    public ApiResponse<Void> updateQuota(
                                         @RequestParam int requestedBy,
                                         @RequestBody LeaveQuotaUpdateRequestDto dto) {
        service.updateQuota(dto,requestedBy);
        return new ApiResponse<>(null);
    }


}
