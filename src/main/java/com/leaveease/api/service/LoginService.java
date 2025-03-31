package com.leaveease.api.service;

import com.leaveease.api.dto.LoginDto;
import com.leaveease.api.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    public Map<String, Object> login(LoginDto dto) {
        String salt = loginRepository.getSaltByStaffNumber(dto.getStaffNumber());
        String password =dto.getPassword();
        String hash = hashPassword(password, salt);
        return loginRepository.login(dto.getStaffNumber(), hash);
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
            throw new RuntimeException("Error hashing password.");
        }
    }
}
