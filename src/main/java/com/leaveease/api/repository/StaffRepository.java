package com.leaveease.api.repository;

import com.leaveease.api.entity.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<StaffEntity, Integer> {
    Optional<StaffEntity> findByStaffNumber(String staffNumber);
}
