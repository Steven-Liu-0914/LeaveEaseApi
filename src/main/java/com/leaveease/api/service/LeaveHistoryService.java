package com.leaveease.api.service;

import com.leaveease.api.dto.response.LeaveHistoryResponseDto;
import com.leaveease.api.entity.LeaveApplicationEntity;
import com.leaveease.api.repository.LeaveApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveHistoryService {

    private final LeaveApplicationRepository leaveApplicationRepository;

    public List<LeaveHistoryResponseDto> getLeaveHistory(Integer staffId) {
        return leaveApplicationRepository.findByStaffIdOrderByStartDateDesc(staffId)
                .stream()
                .map(LeaveHistoryResponseDto::toDto)
                .collect(Collectors.toList());
    }
}

