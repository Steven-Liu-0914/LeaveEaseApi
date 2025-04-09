package com.leaveease.api.service;

import com.leaveease.api.dto.request.UserProfileRequestDto;
import com.leaveease.api.dto.response.UserProfileResponseDto;
import com.leaveease.api.entity.StaffEntity;
import com.leaveease.api.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final StaffRepository staffRepository;

    public UserProfileResponseDto getProfile(int staffId) {
        StaffEntity staff = staffRepository.findByStaffId(staffId);

        UserProfileResponseDto dto = new UserProfileResponseDto();
        dto.setStaffId(staff.getStaffId());
        dto.setFullName(staff.getFullName());
        dto.setEmail(staff.getEmail());
        dto.setPhone(staff.getPhone());
        dto.setDepartment(staff.getDepartment());
        dto.setJobTitle(staff.getJobTitle());
        dto.setRole(staff.getRole());

        return dto;
    }

    public void updateProfile(int staffId, UserProfileRequestDto dto) {
        StaffEntity staff = staffRepository.findByStaffId(staffId);
        if (staff == null) {
            throw new RuntimeException("Staff not found");
        }

        staff.setFullName(dto.getFullName());
        staff.setEmail(dto.getEmail());
        staff.setPhone(dto.getPhone());

        staffRepository.save(staff);
    }
}
