package com.leaveease.api.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "LeaveQuota")
@Data
public class LeaveQuotaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LeaveQuotaId")
    private Integer leaveQuotaId;

    @Column(name = "StaffId")
    private Integer staffId;

    @Column(name = "Children")
    private int children;

    @Column(name = "Annual")
    private int annual;

    @Column(name = "Sick")
    private int sick;

    @Column(name = "Emergency")
    private int emergency;
}
