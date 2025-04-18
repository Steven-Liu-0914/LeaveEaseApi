package com.leaveease.api.service;

import com.leaveease.api.dto.request.LoginRequestDto;
import com.leaveease.api.dto.response.LoginInfoResponseDto;
import com.leaveease.api.entity.StaffEntity;
import com.leaveease.api.repository.StaffRepository;
import com.leaveease.api.util.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.leaveease.api.util.ErrorMessages.LOGIN_INVALID_CREDENTIALS;
import static com.leaveease.api.util.ErrorMessages.PASSWORD_HASHING_ERROR;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final StaffRepository staffRepository;

    public LoginInfoResponseDto login(LoginRequestDto dto) {
        StaffEntity staff = staffRepository.findByStaffNumber(dto.getStaffNumber())
                .orElseThrow(() -> new RuntimeException(ErrorMessages.LOGIN_INVALID_CREDENTIALS.getMessage()));

        String decryptedPassword = dto.getPassword();
        String expectedHash = hashPassword(decryptedPassword, staff.getPasswordSalt());

        if (!expectedHash.equals(staff.getPasswordHash())) {
            throw new RuntimeException(LOGIN_INVALID_CREDENTIALS.getMessage());
        }

        return new LoginInfoResponseDto(
                staff.getStaffId(),
                staff.getStaffNumber(),
                staff.getFullName(),
                staff.getEmail(),
                staff.getDepartment(),
                staff.getJobTitle(),
                staff.getRole()
        );
    }

    private String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest((password + salt).getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
           throw new RuntimeException(PASSWORD_HASHING_ERROR.getMessage());
        }
    }
}
