package com.leaveease.api.repository;

import com.leaveease.api.entity.LeaveApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    List<LeaveApplicationEntity> findByStatusAndStaff_RoleOrderByStaff_StaffNumberAsc(
            String status, String role);

    @Query("SELECT l FROM LeaveApplicationEntity l " +
            "WHERE l.staffId = :staffId AND l.status IN :statuses " +
            "AND l.startDate <= :endDate AND l.endDate >= :startDate")
    List<LeaveApplicationEntity> findByStaffIdAndStatusInAndDateRangeOverlap(
            @Param("staffId") int staffId,
            @Param("statuses") List<String> statuses,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT l FROM LeaveApplicationEntity l WHERE l.startDate <= :ph AND l.endDate >= :ph")
    List<LeaveApplicationEntity> findByDateRangeIncluding(@Param("ph") LocalDate ph);

    @Query("SELECT l FROM LeaveApplicationEntity l WHERE l.staffId = :staffId AND l.leaveType = :leaveType AND l.status IN :statuses AND YEAR(l.startDate) = :year")
    List<LeaveApplicationEntity> findByStaffIdAndLeaveTypeAndStatusInAndYear(
            @Param("staffId") int staffId,
            @Param("leaveType") String leaveType,
            @Param("statuses") List<String> statuses,
            @Param("year") int year
    );
}
