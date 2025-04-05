package com.leaveease.api.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "LeaveApplication")  // ✅ Table name in camelCase
@Data
public class LeaveApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LeaveApplicationId")  // ✅ camelCase
    private Integer leaveApplicationId;

    @Column(name = "StaffId")
    private Integer staffId;

    @Column(name = "LeaveType")
    private String leaveType;

    @Column(name = "StartDate")
    private LocalDate startDate;

    @Column(name = "EndDate")
    private LocalDate endDate;

    @Column(name = "Reason")
    private String reason;

    @Column(name = "Status")
    private String status = "Pending";

    @Column(name = "CreatedAt")
    private LocalDate createdAt = LocalDate.now();
}
