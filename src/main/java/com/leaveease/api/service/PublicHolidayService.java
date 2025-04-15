package com.leaveease.api.service;

import com.leaveease.api.dto.request.PublicHolidayRequestDto;
import com.leaveease.api.dto.request.ReportAnalysisRequestDto;
import com.leaveease.api.dto.response.PublicHolidayResponseDto;
import com.leaveease.api.dto.response.ReportAnalysisResponseDto;
import com.leaveease.api.entity.PublicHolidayEntity;
import com.leaveease.api.repository.PublicHolidayRepository;
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
    }

    public void update(PublicHolidayRequestDto dto) {
        PublicHolidayEntity entity = repository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException(ErrorMessages.HOLIDAY_NOT_FOUND.getMessage()));
        entity.setName(dto.getName());
        LocalDate date = LocalDate.parse(dto.getDate());
        entity.setDate(date);
        entity.setDay(dto.getDay());
        repository.save(entity);
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
}
