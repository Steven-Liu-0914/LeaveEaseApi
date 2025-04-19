package com.leaveease.api.entity;

import com.leaveease.api.util.CommonEnums;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "leaveApplication")  // ✅ Table name in camelCase
@Data
public class LeaveApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer leaveApplicationId;

    // ✅ Setup @ManyToOne mapping to StaffEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StaffId", referencedColumnName = "StaffId", insertable = false, updatable = false)
    private StaffEntity staff;

    private Integer staffId;

    private String leaveType;

    private LocalDate startDate;

    private LocalDate endDate;

    private String reason;

    private String status = CommonEnums.LeaveStatus.PENDING.getValue();

    private LocalDate createdAt = LocalDate.now();
}
