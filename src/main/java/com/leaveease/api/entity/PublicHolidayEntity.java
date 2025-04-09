package com.leaveease.api.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "publicholiday")
@Data
public class PublicHolidayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PublicHolidayId")
    private Integer PublicHolidayId;

    private String name;

    private LocalDate date;

    private String day;
}
