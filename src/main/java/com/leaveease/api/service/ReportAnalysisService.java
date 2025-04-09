package com.leaveease.api.service;

import com.leaveease.api.dto.request.ReportAnalysisRequestDto;
import com.leaveease.api.dto.response.ReportAnalysisResponseDto;
import com.leaveease.api.repository.ReportAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportAnalysisService {

    private final ReportAnalysisRepository repository;

    public List<ReportAnalysisResponseDto> getReport(ReportAnalysisRequestDto request) {
        return repository.searchAnalysis(request);
    }
}
