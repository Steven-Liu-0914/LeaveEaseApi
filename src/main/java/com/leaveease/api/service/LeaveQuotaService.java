package com.leaveease.api.service;

import com.leaveease.api.dto.request.LeaveQuotaUpdateRequestDto;
import com.leaveease.api.dto.response.LeaveQuotaResponseDto;
import com.leaveease.api.entity.LeaveQuotaEntity;
import com.leaveease.api.repository.LeaveQuotaRepository;
import com.leaveease.api.util.AuthorizationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveQuotaService {

    private final LeaveQuotaRepository quotaRepository;
    private final AuthorizationUtil authUtil;

    public List<LeaveQuotaResponseDto> getAllQuotas(int requestedBy) {
        authUtil.validateAdminFromHR(requestedBy);
        List<LeaveQuotaEntity> entities = quotaRepository.findAllByOrderByStaff_DepartmentAscStaff_FullNameAsc(); // Join fetch
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public void updateQuota(LeaveQuotaUpdateRequestDto dto, int requestedBy) {
        authUtil.validateAdminFromHR(requestedBy);
        LeaveQuotaEntity entity = quotaRepository.findByStaffId(dto.getStaffId());

        entity.setChildren(dto.getQuotas().getChildren());
        entity.setAnnual(dto.getQuotas().getAnnual());
        entity.setSick(dto.getQuotas().getSick());
        entity.setEmergency(dto.getQuotas().getEmergency());
        quotaRepository.save(entity);
    }

    private LeaveQuotaResponseDto toDto(LeaveQuotaEntity entity) {
        LeaveQuotaResponseDto dto = new LeaveQuotaResponseDto();
        dto.setStaffId(entity.getStaff().getStaffId());
        dto.setStaffNumber(entity.getStaff().getStaffNumber());
        dto.setStaffName(entity.getStaff().getFullName());

        LeaveQuotaResponseDto.LeaveQuotas quotas = new LeaveQuotaResponseDto.LeaveQuotas();
        quotas.setChildren(entity.getChildren());
        quotas.setAnnual(entity.getAnnual());
        quotas.setSick(entity.getSick());
        quotas.setEmergency(entity.getEmergency());
        dto.setQuotas(quotas);

        return dto;
    }

}
