package com.leaveease.api.controller;

import com.leaveease.api.dto.request.LoginRequestDto;
import com.leaveease.api.dto.response.LoginInfoResponseDto;
import com.leaveease.api.model.ApiResponse;
import com.leaveease.api.service.LoginService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ApiResponse<LoginInfoResponseDto> login(@RequestBody LoginRequestDto dto) {
        return ApiResponse.of(loginService.login(dto));
    }
}
