package com.leaveease.api.controller;

import com.leaveease.api.dto.request.UserProfileRequestDto;
import com.leaveease.api.dto.response.UserProfileResponseDto;
import com.leaveease.api.model.ApiResponse;
import com.leaveease.api.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService service;

    @GetMapping("/{staffId}")
    public ApiResponse<UserProfileResponseDto> getProfile(@PathVariable int staffId) {
        return new ApiResponse<>(service.getProfile(staffId));
    }

    @PutMapping("/{staffId}")
    public ApiResponse<Void> updateProfile(
            @PathVariable int staffId,
            @RequestBody UserProfileRequestDto dto) {
        service.updateProfile(staffId, dto);
        return new ApiResponse<>(null);
    }
}
