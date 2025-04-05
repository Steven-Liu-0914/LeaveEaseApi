package com.leaveease.api.dto.request;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String staffNumber;
    private String password;
}