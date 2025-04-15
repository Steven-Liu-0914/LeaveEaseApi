package com.leaveease.api.service;

import com.leaveease.api.dto.response.DashboardResponseDto;

import com.leaveease.api.dto.response.LeaveHistoryResponseDto;
import com.leaveease.api.dto.response.PendingLeavesForReviewResponseDto;
import com.leaveease.api.entity.LeaveApplicationEntity;
import com.leaveease.api.entity.LeaveQuotaEntity;
import com.leaveease.api.entity.StaffEntity;
import com.leaveease.api.repository.LeaveApplicationRepository;
import com.leaveease.api.repository.LeaveQuotaRepository;
import com.leaveease.api.repository.StaffRepository;
import com.leaveease.api.util.CommonEnums;
import com.leaveease.api.util.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
public class DashboardService {

    @Autowired
    private LeaveQuotaRepository leaveQuotaRepository;


    private final StaffRepository staffRepository;
    private final LeaveApplicationRepository leaveAppRepo;

    public DashboardResponseDto getDashboardData(int staffId) {
        DashboardResponseDto dto = new DashboardResponseDto();

        LocalDate today = LocalDate.now();
        LocalDate firstDayOfYear = today.withDayOfYear(1);

        // 1. Total approved leave days for this year
        List<LeaveApplicationEntity> approvedThisYear = leaveAppRepo
                .findByStaffIdAndStatusAndStartDateGreaterThanEqual(staffId, CommonEnums.LeaveStatus.APPROVED.getValue(), firstDayOfYear);

        int totalAppliedDays = approvedThisYear.stream()
                .mapToInt(app -> (int) DAYS.between(app.getStartDate(), app.getEndDate()) + 1)
                .sum();
        dto.setTotalApplied(totalAppliedDays);

        // 2. Total upcoming approved leave days (from tomorrow onward)
        List<LeaveApplicationEntity> upcoming = leaveAppRepo
                .findTop10ByStaffIdAndStatusAndStartDateAfter(staffId, CommonEnums.LeaveStatus.APPROVED.getValue(), today);

        int upcomingLeaveDays = upcoming.stream()
                .mapToInt(app -> (int) DAYS.between(app.getStartDate(), app.getEndDate()) + 1)
                .sum();
        dto.setUpcomingLeave(upcomingLeaveDays);

        // 3. Next upcoming leave (top 10 records)
        List<LeaveApplicationEntity> nextUpcoming = leaveAppRepo
                .findTop10ByStaffIdAndStatusAndStartDateAfterOrderByStartDateAsc(
                        staffId, CommonEnums.LeaveStatus.APPROVED.getValue(), today);
        List<LeaveHistoryResponseDto>  mapped_nextUpcoming = nextUpcoming.stream().map(entity->
        {
            LeaveHistoryResponseDto nextUpcomingDto = new LeaveHistoryResponseDto();
            nextUpcomingDto.setLeaveType(entity.getLeaveType());
            nextUpcomingDto.setReason(entity.getReason());
            nextUpcomingDto.setStartDate(entity.getStartDate().toString());
            nextUpcomingDto.setEndDate(entity.getEndDate().toString());
            return nextUpcomingDto;
        }).toList();

        dto.setNextUpcomingLeave(mapped_nextUpcoming);

        // 4. Remaining Leave (quota - total applied)
        LeaveQuotaEntity quota = leaveQuotaRepository.findByStaffId(staffId);
        int totalQuota = 0;

        if (quota != null) {
            totalQuota = quota.getChildren()
                    + quota.getAnnual()
                    + quota.getSick()
                    + quota.getEmergency();
        }

        int remainingLeave = totalQuota - totalAppliedDays;
        dto.setRemainingLeave(Math.max(remainingLeave, 0)); // ensure non-negative


        //5. Pending Leaves if Staff is Admin of Department
        StaffEntity staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.STAFF_NOT_FOUND.getMessage()));

        List<LeaveApplicationEntity> pending = new ArrayList<>();

        if (CommonEnums.StaffRole.ADMIN.getValue().equalsIgnoreCase(staff.getRole())) {
            if ("Human Resources".equalsIgnoreCase(staff.getDepartment())) {
                // HR Admin sees all pending leave from all departments
                pending = leaveAppRepo.findByStatusAndStaff_RoleOrderByStaff_StaffNumberAsc(
                        CommonEnums.LeaveStatus.PENDING.getValue(), CommonEnums.StaffRole.USER.getValue()
                );
            } else if (staff.getDepartment() != null) {
                // Department Admin sees pending leave of their own department
                pending = leaveAppRepo.findByStatusAndStaff_DepartmentAndStaff_RoleOrderByStaff_StaffNumberAsc(
                        CommonEnums.LeaveStatus.PENDING.getValue(),
                        staff.getDepartment(),
                        CommonEnums.StaffRole.USER.getValue()
                );
            }
        }

            List<PendingLeavesForReviewResponseDto> mapped_pending = pending.stream().map(entity -> {
                StaffEntity _staff = staffRepository.findById(entity.getStaffId()).orElse(null);
                PendingLeavesForReviewResponseDto reviewDto = new PendingLeavesForReviewResponseDto();
                reviewDto.setLeaveApplicationId(entity.getLeaveApplicationId());
                reviewDto.setStaffId(entity.getStaffId());
                reviewDto.setLeaveType(entity.getLeaveType());
                reviewDto.setStartDate(entity.getStartDate());
                reviewDto.setEndDate(entity.getEndDate());
                reviewDto.setReason(entity.getReason());
                reviewDto.setStatus(entity.getStatus());
                reviewDto.setCreatedAt(entity.getCreatedAt());

                if (_staff != null) {
                    reviewDto.setStaffNumber(_staff.getStaffNumber());
                    reviewDto.setStaffName(_staff.getFullName());
                }

                return reviewDto;
            }).toList();

            dto.setPendingApproveLeave(mapped_pending);

        return dto;
    }

}

