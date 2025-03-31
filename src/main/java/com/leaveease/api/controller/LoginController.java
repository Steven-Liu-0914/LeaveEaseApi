package com.leaveease.api.controller;

import com.leaveease.api.dto.LoginDto;
import com.leaveease.api.model.ApiResponse;
import com.leaveease.api.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public ApiResponse<Map<String, Object>> login(@RequestBody LoginDto dto) {
        Map<String, Object> result = loginService.login(dto);
        return ApiResponse.of(result);  // will serialize with camelCase keys
    }
}
