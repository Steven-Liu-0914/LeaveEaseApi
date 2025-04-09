package com.leaveease.api.service;

import com.leaveease.api.dto.request.LeaveApplicationRequestDto;
import com.leaveease.api.entity.LeaveApplicationEntity;
import com.leaveease.api.entity.StaffEntity;
import com.leaveease.api.repository.LeaveApplicationRepository;
import com.leaveease.api.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LeaveApplicationService {

    private final LeaveApplicationRepository leaveRepo;
    private final StaffRepository staffRepository;

    public void submitLeave(int staffId, LeaveApplicationRequestDto dto) {


        StaffEntity staff = staffRepository.findById(staffId).orElseThrow(() -> new RuntimeException("Staff not found"));


        LeaveApplicationEntity entity = new LeaveApplicationEntity();
        entity.setStaffId(staffId);
        entity.setLeaveType(dto.getLeaveType());
        entity.setStartDate(LocalDate.parse(dto.getStartDate()));
        entity.setEndDate(LocalDate.parse(dto.getEndDate()));
        entity.setReason(dto.getReason());
        if ("admin".equalsIgnoreCase(staff.getRole()) && staff.getDepartment() != null)
        {
            entity.setStatus("Approved");
        }else
        {
            entity.setStatus("Pending");
        }
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
        setLeaveStatus(leaveId, "Canceled");
    }

    public void approveLeave(int leaveId)
    {
        setLeaveStatus(leaveId, "Approved");
    }

   public void rejectLeave(int leaveId)
   {
       setLeaveStatus(leaveId, "Rejected");
   }

    public void setLeaveStatus(int leaveId, String status) {
        LeaveApplicationEntity leave = leaveRepo.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave application not found"));

        if (!"Pending".equalsIgnoreCase(leave.getStatus()) && status.equalsIgnoreCase("Canceled")) {
            throw new RuntimeException("Only pending leave can be cancelled");
        }

        leave.setStatus(status);
        leaveRepo.save(leave);
    }
}
