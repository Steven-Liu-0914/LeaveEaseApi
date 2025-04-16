package com.leaveease.api.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "leaveQuota") // Use lowercase with underscores if following MySQL naming
@Data
public class LeaveQuotaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer leaveQuotaId;

    private Integer staffId;

    private int children;

    private int annual;

    private int sick;

    private int emergency;

    // 🧩 New: Join with StaffEntity
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staffId", referencedColumnName = "staffId", insertable = false, updatable = false)
    private StaffEntity staff;
}
