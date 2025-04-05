package com.leaveease.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfoResponseDto {
    private int staffId;
    private String staffNumber;
    private String fullName;
    private String email;
    private String department;
    private String jobTitle;
    private String role;
}
