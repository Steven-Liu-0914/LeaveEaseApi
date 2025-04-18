package com.leaveease.api.service;

import com.leaveease.api.dto.request.LeaveApplicationRequestDto;
import com.leaveease.api.entity.LeaveApplicationEntity;
import com.leaveease.api.entity.LeaveQuotaEntity;
import com.leaveease.api.entity.PublicHolidayEntity;
import com.leaveease.api.entity.StaffEntity;
import com.leaveease.api.repository.LeaveApplicationRepository;
import com.leaveease.api.repository.LeaveQuotaRepository;
import com.leaveease.api.repository.PublicHolidayRepository;
import com.leaveease.api.repository.StaffRepository;
import com.leaveease.api.util.CommonEnums;
import com.leaveease.api.util.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveApplicationService {

    private final LeaveApplicationRepository leaveRepo;
    private final StaffRepository staffRepository;
    private final PublicHolidayRepository publicHolidayRepository;
    private final LeaveQuotaRepository leaveQuotaRepository;

    public void submitLeave(int staffId, LeaveApplicationRequestDto dto) {
        StaffEntity staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.STAFF_NOT_FOUND.getMessage()));

        List<LeaveApplicationEntity> validLeaves = buildValidatedLeaveApplications(staffId, dto, staff, null);
        leaveRepo.saveAll(validLeaves);
    }

    public void updateLeave(int leaveId, LeaveApplicationRequestDto dto) {
        LeaveApplicationEntity originalLeave = leaveRepo.findById(leaveId)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.LEAVE_APPLICATION_NOT_FOUND.getMessage()));

        if (!CommonEnums.LeaveStatus.PENDING.getValue().equalsIgnoreCase(originalLeave.getStatus())) {
            throw new RuntimeException(ErrorMessages.LEAVE_ONLY_PENDING_CAN_UPDATE.getMessage());
        }

        StaffEntity staff = staffRepository.findById(originalLeave.getStaffId())
                .orElseThrow(() -> new RuntimeException(ErrorMessages.STAFF_NOT_FOUND.getMessage()));

        List<LeaveApplicationEntity> newLeaves = buildValidatedLeaveApplications(
                originalLeave.getStaffId(), dto, staff, leaveId
        );

        leaveRepo.saveAll(newLeaves);
        leaveRepo.deleteById(leaveId);
    }

    private List<LeaveApplicationEntity> buildValidatedLeaveApplications(
            int staffId,
            LeaveApplicationRequestDto dto,
            StaffEntity staff,
            Integer excludeLeaveId
    ) {
        LocalDate start = LocalDate.parse(dto.getStartDate());
        LocalDate end = LocalDate.parse(dto.getEndDate());

        checkConflict(staffId, start, end, excludeLeaveId);

        List<LeaveApplicationEntity> validLeaves = generateValidLeaves(start, end, staffId, dto, staff);

        if (validLeaves.isEmpty()) {
            throw new RuntimeException(ErrorMessages.LEAVE_ONLY_NONWORKING_DAYS.getMessage());
        }

        enforceQuotaValidation(staffId, dto.getLeaveType(), validLeaves, excludeLeaveId);

        return validLeaves;
    }

    private void checkConflict(int staffId, LocalDate start, LocalDate end, Integer excludeLeaveId) {
        List<LeaveApplicationEntity> conflicts = leaveRepo
                .findByStaffIdAndStatusInAndDateRangeOverlap(
                        staffId,
                        List.of(CommonEnums.LeaveStatus.PENDING.getValue(), CommonEnums.LeaveStatus.APPROVED.getValue()),
                        start, end
                );

        if (excludeLeaveId != null) {
            conflicts = conflicts.stream()
                    .filter(lv -> !lv.getLeaveApplicationId().equals(excludeLeaveId))
                    .collect(Collectors.toList());
        }

        if (!conflicts.isEmpty()) {
            throw new RuntimeException(ErrorMessages.LEAVE_CONFLICT_EXISTS.getMessage());
        }
    }

    private List<LeaveApplicationEntity> generateValidLeaves(
            LocalDate start, LocalDate end, int staffId,
            LeaveApplicationRequestDto dto, StaffEntity staff) {

        Set<LocalDate> phDates = publicHolidayRepository.findAll().stream()
                .map(PublicHolidayEntity::getDate)
                .collect(Collectors.toSet());

        List<LeaveApplicationEntity> result = new ArrayList<>();
        List<LocalDate> currentRange = new ArrayList<>();

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            boolean isWeekend = date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
            boolean isHoliday = phDates.contains(date);

            if (!isWeekend && !isHoliday) {
                currentRange.add(date);
            } else if (!currentRange.isEmpty()) {
                result.add(createLeaveEntity(currentRange, staffId, dto, staff));
                currentRange.clear();
            }
        }

        if (!currentRange.isEmpty()) {
            result.add(createLeaveEntity(currentRange, staffId, dto, staff));
        }

        if (result.size() > 1) {
            for (LeaveApplicationEntity leave : result) {
                if (!leave.getReason().endsWith("(Auto-Split)")) {
                    leave.setReason(leave.getReason() + " (Auto-Split)");
                }
            }
        }

        return result;
    }

    private void enforceQuotaValidation(int staffId, String leaveType, List<LeaveApplicationEntity> newLeaves, Integer excludeLeaveId) {
        int requestedDays = newLeaves.stream()
                .mapToInt(lv -> (int) ChronoUnit.DAYS.between(lv.getStartDate(), lv.getEndDate()) + 1)
                .sum();

        LeaveQuotaEntity quota = leaveQuotaRepository.findByStaffId(staffId);


        int currentYear = LocalDate.now().getYear();

        List<LeaveApplicationEntity> usedLeaves = leaveRepo
                .findByStaffIdAndLeaveTypeAndStatusInAndYear(
                        staffId,
                        leaveType,
                        List.of(CommonEnums.LeaveStatus.PENDING.getValue(), CommonEnums.LeaveStatus.APPROVED.getValue()),
                        currentYear
                );

        int usedDays = usedLeaves.stream()
                .filter(lv -> excludeLeaveId == null || !lv.getLeaveApplicationId().equals(excludeLeaveId))
                .mapToInt(lv -> (int) ChronoUnit.DAYS.between(lv.getStartDate(), lv.getEndDate()) + 1)
                .sum();

        int quotaForType = getQuotaForType(leaveType, quota);
        int remainingDays = quotaForType - usedDays;

        if (requestedDays > remainingDays) {
            throw new RuntimeException(ErrorMessages.LEAVE_QUOTA_EXCEEDED.getMessage());
        }
    }

    private int getQuotaForType(String leaveType, LeaveQuotaEntity quota) {
        if (leaveType.equals(CommonEnums.LeaveType.ANNUAL.getValue())) {
            return quota.getAnnual();
        } else if (leaveType.equals(CommonEnums.LeaveType.SICK.getValue())) {
            return quota.getSick();
        } else if (leaveType.equals(CommonEnums.LeaveType.EMERGENCY.getValue())) {
            return quota.getEmergency();
        } else if (leaveType.equals(CommonEnums.LeaveType.CHILDREN.getValue())) {
            return quota.getChildren();
        } else {
            throw new RuntimeException("Unsupported leave type: " + leaveType);
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
        entity.setStatus(CommonEnums.StaffRole.ADMIN.getValue().equalsIgnoreCase(staff.getRole()) && staff.getDepartment() != null
                ? CommonEnums.LeaveStatus.APPROVED.getValue() : CommonEnums.LeaveStatus.PENDING.getValue());
        entity.setCreatedAt(LocalDate.now());
        return entity;
    }

    public void cancelLeave(int leaveId) {
        setLeaveStatus(leaveId, CommonEnums.LeaveStatus.CANCELLED.getValue());
    }

    public void approveLeave(int leaveId) {
        setLeaveStatus(leaveId, CommonEnums.LeaveStatus.APPROVED.getValue());
    }

    public void rejectLeave(int leaveId) {
        setLeaveStatus(leaveId, CommonEnums.LeaveStatus.REJECTED.getValue());
    }

    public void setLeaveStatus(int leaveId, String status) {
        LeaveApplicationEntity leave = leaveRepo.findById(leaveId)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.LEAVE_APPLICATION_NOT_FOUND.getMessage()));

        if (CommonEnums.LeaveStatus.CANCELLED.getValue().equalsIgnoreCase(status)) {
            if (!(CommonEnums.LeaveStatus.PENDING.getValue().equalsIgnoreCase(leave.getStatus())
                    || CommonEnums.LeaveStatus.APPROVED.getValue().equalsIgnoreCase(leave.getStatus()))) {
                throw new RuntimeException(ErrorMessages.LEAVE_ONLY_PENDING_APPROVED_CAN_CANCEL.getMessage());
            }

            if (CommonEnums.LeaveStatus.APPROVED.getValue().equalsIgnoreCase(leave.getStatus())
                    && !leave.getStartDate().isAfter(LocalDate.now())) {
                throw new RuntimeException(ErrorMessages.LEAVE_ALREADY_STARTED.getMessage());
            }
        }

        leave.setStatus(status);
        leaveRepo.save(leave);
    }
}
