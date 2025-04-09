package com.leaveease.api.repository;

import com.leaveease.api.entity.PublicHolidayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PublicHolidayRepository extends JpaRepository<PublicHolidayEntity, Integer> {

    List<PublicHolidayEntity> findByDateBetween(LocalDate start, LocalDate end);

}
