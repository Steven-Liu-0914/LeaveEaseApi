package com.leaveease.api.util;

import lombok.Getter;

@Getter
public enum ErrorMessages {

    // Login related errors
    LOGIN_INVALID_CREDENTIALS("Invalid staff number or password."),
    
    // Staff related errors
    STAFF_NOT_FOUND("Staff not found"),
    
    // Authorization related errors
    UNAUTHORIZED_INVALID_STAFF("Unauthorized: Invalid staff."),
    UNAUTHORIZED_NOT_HR_ADMIN("Unauthorized: Not an HR admin."),
    
    // Leave application related errors
    LEAVE_APPLICATION_NOT_FOUND("Leave application not found"),
    LEAVE_ONLY_PENDING_CAN_UPDATE("Only pending leave can be updated"),
    LEAVE_CONFLICT_EXISTS("Leave application conflicts with existing applied leave. Please cancel and reapply."),
    LEAVE_ONLY_NONWORKING_DAYS("The requested leave period includes only non-working days (weekends or public holidays). No leave was submitted."),
    LEAVE_ONLY_PENDING_APPROVED_CAN_CANCEL("Only pending or approved leaves can be cancelled"),
    LEAVE_ALREADY_STARTED("Approved leave that has already started cannot be cancelled"),
    
    // Holiday related errors
    HOLIDAY_NOT_FOUND("Holiday not found"),
    
    //PASSWORD HASHING
    PASSWORD_HASHING_ERROR("Error hashing password."),

    // General errors
    GENERAL_ERROR("An unexpected error occurred. Please contact the administrator.");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }
}