package com.leaveease.api.service;

import com.leaveease.api.dto.request.PublicHolidayRequestDto;
import com.leaveease.api.dto.request.ReportAnalysisRequestDto;
import com.leaveease.api.dto.response.PublicHolidayResponseDto;
import com.leaveease.api.dto.response.ReportAnalysisResponseDto;
import com.leaveease.api.entity.LeaveApplicationEntity;
import com.leaveease.api.entity.LeaveQuotaEntity;
import com.leaveease.api.entity.PublicHolidayEntity;
import com.leaveease.api.repository.LeaveApplicationRepository;
import com.leaveease.api.repository.LeaveQuotaRepository;
import com.leaveease.api.repository.PublicHolidayRepository;
import com.leaveease.api.util.CommonEnums;
import com.leaveease.api.util.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicHolidayService {

    private final PublicHolidayRepository repository;
    private final LeaveApplicationRepository leaveRepo;
    private final LeaveQuotaRepository quotaRepo;

    public List<PublicHolidayResponseDto> getAll() {
        return repository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }


    public void add(PublicHolidayRequestDto dto) {
        PublicHolidayEntity entity = new PublicHolidayEntity();
        entity.setName(dto.getName());
        LocalDate date = LocalDate.parse(dto.getDate());
        entity.setDate(date);
        entity.setDay(dto.getDay());

        repository.save(entity);

        handleLeaveImpact(date);
    }

    public void update(PublicHolidayRequestDto dto) {
        PublicHolidayEntity entity = repository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException(ErrorMessages.HOLIDAY_NOT_FOUND.getMessage()));
        entity.setName(dto.getName());
        LocalDate date = LocalDate.parse(dto.getDate());
        entity.setDate(date);
        entity.setDay(dto.getDay());
        repository.save(entity);

        handleLeaveImpact(date);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    private PublicHolidayResponseDto mapToDto(PublicHolidayEntity e) {
        PublicHolidayResponseDto dto = new PublicHolidayResponseDto();
        dto.setId(e.getPublicHolidayId());
        dto.setName(e.getName());
        dto.setDate(e.getDate().toString());
        dto.setDay(e.getDay());
        return dto;
    }

    private void handleLeaveImpact(LocalDate newPhDate) {
        List<LeaveApplicationEntity> affectedLeaves =
                leaveRepo.findByDateRangeIncluding(newPhDate);

        for (LeaveApplicationEntity leave : affectedLeaves) {
            if (CommonEnums.LeaveStatus.APPROVED.getValue().equalsIgnoreCase(leave.getStatus())) {
                LeaveQuotaEntity quota = quotaRepo.findByStaffId(leave.getStaffId());

                if (quota != null) {
                    String type = leave.getLeaveType();

                    if (CommonEnums.LeaveType.ANNUAL.getValue().equalsIgnoreCase(type)) {
                        quota.setAnnual(quota.getAnnual() + 1);
                    } else if (CommonEnums.LeaveType.SICK.getValue().equalsIgnoreCase(type)) {
                        quota.setSick(quota.getSick() + 1);
                    } else if (CommonEnums.LeaveType.CHILDREN.getValue().equalsIgnoreCase(type)) {
                        quota.setChildren(quota.getChildren() + 1);
                    } else if (CommonEnums.LeaveType.EMERGENCY.getValue().equalsIgnoreCase(type)) {
                        quota.setEmergency(quota.getEmergency() + 1);
                    }

                    quotaRepo.save(quota);
                }
            } else if (CommonEnums.LeaveStatus.PENDING.getValue().equalsIgnoreCase(leave.getStatus())) {
                // Pending → Auto Cancelled
                leave.setStatus(CommonEnums.LeaveStatus.CANCELLED.getValue());
                leave.setReason(leave.getReason() + " (Auto-cancelled due to public holiday)");
                leaveRepo.save(leave);
            }
        }
    }
}
