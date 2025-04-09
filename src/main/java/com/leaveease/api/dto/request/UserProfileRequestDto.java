package com.leaveease.api.dto.request;

import lombok.Data;

@Data
public class UserProfileRequestDto {
    private String fullName;
    private String email;
    private String phone;
}
