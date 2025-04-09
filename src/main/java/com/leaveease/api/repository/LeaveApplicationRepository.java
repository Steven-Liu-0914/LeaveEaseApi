package com.leaveease.api.repository;

import com.leaveease.api.entity.LeaveApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplicationEntity, Integer> {

    long countByStaffIdAndStatus(int staffId, String status);

    long countByStaffIdAndStatusAndStartDateAfter(int staffId, String status, LocalDate today);

    List<LeaveApplicationEntity> findTop10ByStaffIdAndStatusAndStartDateAfterOrderByStartDateAsc(
            int staffId, String status, LocalDate today
    );

    List<LeaveApplicationEntity> findByStaffIdOrderByStartDateDesc(Integer staffId);

    List<LeaveApplicationEntity> findByStaffIdAndStatusAndStartDateGreaterThanEqual(
            int staffId, String status, LocalDate startDate);

    List<LeaveApplicationEntity> findTop10ByStaffIdAndStatusAndStartDateAfter(
            int staffId, String status, LocalDate startDate);

    List<LeaveApplicationEntity> findByStatusAndStaff_DepartmentAndStaff_RoleOrderByStaff_StaffNumberAsc(String status, String department, String role);

    List<LeaveApplicationEntity> findByStatusAndStartDateBetween(String status, LocalDate start, LocalDate end);

}
