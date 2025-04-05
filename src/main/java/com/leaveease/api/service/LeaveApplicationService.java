package com.leaveease.api.service;

import com.leaveease.api.dto.request.LeaveApplicationRequestDto;
import com.leaveease.api.entity.LeaveApplicationEntity;
import com.leaveease.api.repository.LeaveApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LeaveApplicationService {

    private final LeaveApplicationRepository leaveRepo;

    public void submitLeave(int staffId, LeaveApplicationRequestDto dto) {
        LeaveApplicationEntity entity = new LeaveApplicationEntity();
        entity.setStaffId(staffId);
        entity.setLeaveType(dto.getLeaveType());
        entity.setStartDate(LocalDate.parse(dto.getStartDate()));
        entity.setEndDate(LocalDate.parse(dto.getEndDate()));
        entity.setReason(dto.getReason());
        entity.setStatus("Pending");
        entity.setCreatedAt(LocalDate.now());

        leaveRepo.save(entity);
    }
    public void updateLeave(int leaveId, LeaveApplicationRequestDto dto) {
        LeaveApplicationEntity leave = leaveRepo.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave application not found"));

        if (!"Pending".equalsIgnoreCase(leave.getStatus())) {
            throw new RuntimeException("Only pending leave can be updated");
        }

        leave.setLeaveType(dto.getLeaveType());
        leave.setStartDate(LocalDate.parse(dto.getStartDate()));
        leave.setEndDate(LocalDate.parse(dto.getEndDate()));
        leave.setReason(dto.getReason());

        leaveRepo.save(leave);
    }

    public void cancelLeave(int leaveId) {
        LeaveApplicationEntity leave = leaveRepo.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave application not found"));

        if (!"Pending".equalsIgnoreCase(leave.getStatus())) {
            throw new RuntimeException("Only pending leave can be cancelled");
        }

        leave.setStatus("Cancelled");
        leaveRepo.save(leave);
    }
}
