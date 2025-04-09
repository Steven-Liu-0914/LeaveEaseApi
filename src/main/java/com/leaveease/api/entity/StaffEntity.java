package com.leaveease.api.entity;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "staff")
public class StaffEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int staffId;

    private String staffNumber;

    private String fullName;

    private String email;

    private String phone;

    private String department;

    private String jobTitle;

    private String role;

    private String passwordHash;
    private String passwordSalt;

    // Getters/Setters or use Lombok @Data
}

