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
               .orElseThrow(() -> new RuntimeException(ErrorMessages.UNAUTHORIZED_INVALID_STAFF.getMessage()));

   if (!CommonEnums.StaffRole.ADMIN.getValue().equalsIgnoreCase(staff.getRole()) ||
            !CommonEnums.Department.HUMAN_RESOURCE.getValue().equalsIgnoreCase(staff.getDepartment())) {
       throw new RuntimeException(ErrorMessages.UNAUTHORIZED_NOT_HR_ADMIN.getMessage());
    }
    }
}
