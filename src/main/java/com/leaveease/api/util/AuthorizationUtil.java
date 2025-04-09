package com.leaveease.api.util;

import com.leaveease.api.entity.StaffEntity;
import com.leaveease.api.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationUtil {

    @Autowired
    private StaffRepository staffRepository;

    public void validateAdminFromHR(int requestedBy) {
        StaffEntity staff = staffRepository.findById(requestedBy)
                .orElseThrow(() -> new RuntimeException("Unauthorized: Invalid staff."));

        if (!"admin".equalsIgnoreCase(staff.getRole()) ||
                !"Human Resources".equalsIgnoreCase(staff.getDepartment())) {
            throw new RuntimeException("Unauthorized: Not an HR admin.");
        }
    }
}
