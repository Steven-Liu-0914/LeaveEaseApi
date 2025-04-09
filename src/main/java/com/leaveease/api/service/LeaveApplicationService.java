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

        LocalDate start = LocalDate.parse(dto.getStartDate());
        LocalDate end = LocalDate.parse(dto.getEndDate());

        // Step 1: Conflict check
        List<LeaveApplicationEntity> conflictingLeaves = leaveRepo
                .findByStaffIdAndStatusInAndDateRangeOverlap(staffId, List.of("Pending", "Approved"),
                        start, end);

        if (!conflictingLeaves.isEmpty()) {
            throw new RuntimeException("Leave application conflicts with existing applied leave. Please cancel and reapply.");
        }

        List<PublicHolidayEntity> publicHolidays = publicHolidayRepository.findAll();
        Set<LocalDate> publicHolidayDates = publicHolidays.stream()
                .map(PublicHolidayEntity::getDate)
                .collect(Collectors.toSet());

        List<LeaveApplicationEntity> validRanges = new ArrayList<>();
        List<LocalDate> currentRange = new ArrayList<>();

        // Step 2: Skip holidays & weekends
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            boolean isWeekend = date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
            boolean isPublicHoliday = publicHolidayDates.contains(date);

            if (!isWeekend && !isPublicHoliday) {
                currentRange.add(date);
            } else if (!currentRange.isEmpty()) {
                validRanges.add(createLeaveEntity(currentRange, staffId, dto, staff));
                currentRange.clear();
            }
        }

        if (!currentRange.isEmpty()) {
            validRanges.add(createLeaveEntity(currentRange, staffId, dto, staff));
        }

        if (!validRanges.isEmpty() && validRanges.size() > 1) {
            for (LeaveApplicationEntity leave : validRanges) {
                String originalReason = leave.getReason();
                leave.setReason(originalReason + " (Auto-Split)");
            }
        }

        if(!validRanges.isEmpty())
        {
            leaveRepo.saveAll(validRanges);
        }
        else
        {
            throw new RuntimeException("The requested leave period includes only non-working days (weekends or public holidays). No leave was submitted.");
        }
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

        if (!("Pending".equalsIgnoreCase(leave.getStatus()) || "Approved".equalsIgnoreCase(leave.getStatus())) && status.equalsIgnoreCase("Canceled")) {
            throw new RuntimeException("Only pending or approved leave can be cancelled");
        }

        leave.setStatus(status);
        leaveRepo.save(leave);
    }
}
