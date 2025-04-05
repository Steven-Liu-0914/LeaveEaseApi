package com.leaveease.api.service;

import com.leaveease.api.dto.response.DashboardResponseDto;

import com.leaveease.api.entity.LeaveApplicationEntity;
import com.leaveease.api.entity.LeaveQuotaEntity;
import com.leaveease.api.repository.LeaveApplicationRepository;
import com.leaveease.api.repository.LeaveQuotaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    @Autowired
    private LeaveQuotaRepository leaveQuotaRepository;

    private final LeaveApplicationRepository leaveAppRepo;

    public DashboardResponseDto getDashboardData(int staffId) {
        DashboardResponseDto dto = new DashboardResponseDto();

        // Total approved leaves
        dto.setTotalApplied((int) leaveAppRepo.countByStaffIdAndStatus(staffId, "Approved"));

        // Approved leaves starting in the future
        dto.setUpcomingLeave((int) leaveAppRepo.countByStaffIdAndStatusAndStartDateAfter(
                staffId, "Approved", LocalDate.now()));

        // Next upcoming leave (top 10)
        List<LeaveApplicationEntity> upcoming =
                leaveAppRepo.findTop10ByStaffIdAndStatusAndStartDateAfterOrderByStartDateAsc(
                        staffId, "Approved", LocalDate.now());

        dto.setNextUpcomingLeave(upcoming);

        LeaveQuotaEntity quota =
                leaveQuotaRepository.findByStaffId(staffId);

        int remainingLeave = 0;

        if (quota != null) {
            remainingLeave = quota.getChildren()
                    + quota.getAnnual()
                    + quota.getSick()
                    + quota.getEmergency();
        }

        dto.setRemainingLeave(remainingLeave);

        return dto;
    }
}

