package com.leaveease.api.service;

import com.leaveease.api.dto.request.LeaveApplicationRequestDto;
import com.leaveease.api.entity.LeaveApplicationEntity;
import com.leaveease.api.entity.PublicHolidayEntity;
import com.leaveease.api.entity.StaffEntity;
import com.leaveease.api.repository.LeaveApplicationRepository;
import com.leaveease.api.repository.PublicHolidayRepository;
import com.leaveease.api.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveApplicationService {

    private final LeaveApplicationRepository leaveRepo;
    private final StaffRepository staffRepository;
    private final PublicHolidayRepository publicHolidayRepository;

    public void submitLeave(int staffId, LeaveApplicationRequestDto dto) {
        StaffEntity staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        List<LeaveApplicationEntity> validLeaves = buildValidatedLeaveApplications(staffId, dto, staff, null);

        leaveRepo.saveAll(validLeaves);
    }

    public void updateLeave(int leaveId, LeaveApplicationRequestDto dto) {
        LeaveApplicationEntity originalLeave = leaveRepo.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave application not found"));

        if (!"Pending".equalsIgnoreCase(originalLeave.getStatus())) {
            throw new RuntimeException("Only pending leave can be updated");
        }

        StaffEntity staff = staffRepository.findById(originalLeave.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff not found"));



        List<LeaveApplicationEntity> newLeaves = buildValidatedLeaveApplications(
                originalLeave.getStaffId(), dto, staff, leaveId // exclude self from conflict check
        );

        leaveRepo.saveAll(newLeaves);

        // Delete the old leave (since we may replace it with multiple new ones)
        leaveRepo.deleteById(leaveId);
    }

    private List<LeaveApplicationEntity> buildValidatedLeaveApplications(
            int staffId,
            LeaveApplicationRequestDto dto,
            StaffEntity staff,
            Integer excludeLeaveId // pass leaveId when updating to skip checking conflict against itself
    ) {
        LocalDate start = LocalDate.parse(dto.getStartDate());
        LocalDate end = LocalDate.parse(dto.getEndDate());

        // Conflict check
        List<LeaveApplicationEntity> conflicts = leaveRepo
                .findByStaffIdAndStatusInAndDateRangeOverlap(staffId, List.of("Pending", "Approved"), start, end);

        if (excludeLeaveId != null) {
            conflicts = conflicts.stream()
                    .filter(lv -> !lv.getLeaveApplicationId().equals(excludeLeaveId))
                    .collect(Collectors.toList());
        }

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Leave application conflicts with existing applied leave. Please cancel and reapply.");
        }

        // Weekend and PH skipping
        List<PublicHolidayEntity> publicHolidays = publicHolidayRepository.findAll();
        Set<LocalDate> phDates = publicHolidays.stream()
                .map(PublicHolidayEntity::getDate)
                .collect(Collectors.toSet());

        List<LeaveApplicationEntity> validLeaves = new ArrayList<>();
        List<LocalDate> currentRange = new ArrayList<>();

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            boolean isWeekend = date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
            boolean isHoliday = phDates.contains(date);

            if (!isWeekend && !isHoliday) {
                currentRange.add(date);
            } else if (!currentRange.isEmpty()) {
                validLeaves.add(createLeaveEntity(currentRange, staffId, dto, staff));
                currentRange.clear();
            }
        }

        if (!currentRange.isEmpty()) {
            validLeaves.add(createLeaveEntity(currentRange, staffId, dto, staff));
        }

        // If auto-split happened, update reason
        if (validLeaves.size() > 1) {
            for (LeaveApplicationEntity leave : validLeaves) {
                if(!leave.getReason().endsWith("(Auto-Split)"))
                {
                  leave.setReason(leave.getReason() + " (Auto-Split)");
                }
            }
        }

        if (validLeaves.isEmpty()) {
            throw new RuntimeException("The requested leave period includes only non-working days (weekends or public holidays). No leave was submitted.");
        }

        return validLeaves;
    }

    private LeaveApplicationEntity createLeaveEntity(List<LocalDate> dates, int staffId,
                                                     LeaveApplicationRequestDto dto, StaffEntity staff) {
        LeaveApplicationEntity entity = new LeaveApplicationEntity();
        entity.setStaffId(staffId);
        entity.setLeaveType(dto.getLeaveType());
        entity.setStartDate(dates.get(0));
        entity.setEndDate(dates.get(dates.size() - 1));
        entity.setReason(dto.getReason());
        entity.setStatus("admin".equalsIgnoreCase(staff.getRole()) && staff.getDepartment() != null
                ? "Approved" : "Pending");
        entity.setCreatedAt(LocalDate.now());
        return entity;
    }

    public void cancelLeave(int leaveId) {
        setLeaveStatus(leaveId, "Cancelled");
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

        // If trying to cancel
        if ("Cancelled".equalsIgnoreCase(status)) {
            // Only allow cancel if current status is Pending or Approved
            if (!("Pending".equalsIgnoreCase(leave.getStatus()) || "Approved".equalsIgnoreCase(leave.getStatus()))) {
                throw new RuntimeException("Only pending or approved leaves can be cancelled");
            }

            // If already approved, ensure it hasn't started yet
            if ("Approved".equalsIgnoreCase(leave.getStatus()) && !leave.getStartDate().isAfter(LocalDate.now())) {
                throw new RuntimeException("Approved leave that has already started cannot be cancelled");
            }
        }

        leave.setStatus(status);
        leaveRepo.save(leave);
    }
}
