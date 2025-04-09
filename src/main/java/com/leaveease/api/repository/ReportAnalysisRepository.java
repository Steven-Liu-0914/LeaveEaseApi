package com.leaveease.api.repository;

import com.leaveease.api.dto.response.ReportAnalysisResponseDto;
import com.leaveease.api.dto.request.ReportAnalysisRequestDto;
import java.util.List;

public interface ReportAnalysisRepository {
    List<ReportAnalysisResponseDto> searchAnalysis(ReportAnalysisRequestDto request);
}
