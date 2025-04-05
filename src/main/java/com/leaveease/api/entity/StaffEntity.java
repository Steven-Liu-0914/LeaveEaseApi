package com.leaveease.api.entity;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "staff")
public class StaffEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StaffId") // 👈 match DB column name
    private int staffId;

    @Column(name = "StaffNumber")
    private String staffNumber;

    @Column(name = "FullName")
    private String fullName;

    @Column(name = "Email")
    private String email;

    @Column(name = "Phone")
    private String phone;

    @Column(name = "Department")
    private String department;

    @Column(name = "JobTitle")
    private String jobTitle;

    @Column(name = "Role")
    private String role;

    @Column(name = "PasswordHash")
    private String passwordHash;

    @Column(name = "PasswordSalt")
    private String passwordSalt;

    // Getters/Setters or use Lombok @Data
}

