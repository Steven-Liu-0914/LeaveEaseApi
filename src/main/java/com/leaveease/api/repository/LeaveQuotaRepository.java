package com.leaveease.api.repository;

import com.leaveease.api.entity.LeaveQuotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveQuotaRepository extends JpaRepository<LeaveQuotaEntity, Integer> {
    LeaveQuotaEntity findByStaffId(int staffId);
    List<LeaveQuotaEntity> findAllByOrderByStaff_DepartmentAscStaff_FullNameAsc();

}
