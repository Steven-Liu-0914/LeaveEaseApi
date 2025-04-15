package com.leaveease.api.service;

import com.leaveease.api.dto.response.CalendarEventResponseDto;
import com.leaveease.api.entity.LeaveApplicationEntity;
import com.leaveease.api.entity.PublicHolidayEntity;
import com.leaveease.api.entity.StaffEntity;
import com.leaveease.api.repository.LeaveApplicationRepository;
import com.leaveease.api.repository.PublicHolidayRepository;
import com.leaveease.api.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveCalendarService {

    private final LeaveApplicationRepository leaveRepo;
    private final StaffRepository staffRepo;
    private final PublicHolidayRepository publicHolidayRepo;

    public List<CalendarEventResponseDto> getCalendarByMonth(int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<LeaveApplicationEntity> applications = leaveRepo
                .findByStatusAndStartDateBetween(CommonEnums.LeaveStatus.APPROVED.getValue(), start, end);

        List<CalendarEventResponseDto> events = new ArrayList<>();

        for (LeaveApplicationEntity app : applications) {
            StaffEntity staff = staffRepo.findById(app.getStaffId()).orElse(null);
            if (staff != null) {
                CalendarEventResponseDto dto = new CalendarEventResponseDto();
                dto.setStart(app.getStartDate().toString());
                dto.setEnd(app.getEndDate().toString());
                dto.setTitle(staff.getFullName() + " - " + app.getLeaveType());
                events.add(dto);
            }
        }

        // 2. Add Public Holidays
        List<PublicHolidayEntity> holidays = publicHolidayRepo.findByDateBetween(start, end);
        for (PublicHolidayEntity holiday : holidays) {
            CalendarEventResponseDto dto = new CalendarEventResponseDto();
            dto.setStart(holiday.getDate().toString());
            dto.setEnd(holiday.getDate().toString());
            dto.setTitle("Public Holiday – " + holiday.getName());

            events.add(dto);
        }

        return events;
    }

}
