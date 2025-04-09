package com.leaveease.api.dto.response;

import lombok.Data;

@Data
public class UserProfileResponseDto {
    private int staffId;
    private String fullName;
    private String email;
    private String phone;
    private String department;
    private String jobTitle;
    private String role;
}
