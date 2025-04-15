package com.leaveease.api.util;

/**
 * Common enums used throughout the application
 */
public class CommonEnums {

    /**
     * Represents the possible statuses of a leave application
     */
    public enum LeaveStatus {
        APPROVED(CommonEnums.LeaveStatus.APPROVED.getValue()),
        REJECTED(CommonEnums.LeaveStatus.REJECTED.getValue()),
        CANCELLED(CommonEnums.LeaveStatus.CANCELLED.getValue()),
        PENDING(CommonEnums.LeaveStatus.PENDING.getValue());

        private final String value;

        LeaveStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
        
        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * Represents the possible roles of staff members
     */
    public enum StaffRole {
        ADMIN(CommonEnums.StaffRole.ADMIN.getValue()),
        USER("User"),

        private final String value;

        StaffRole(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
        
        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * Represents the possible types of leave
     */
    public enum LeaveType {
        CHILDREN(CommonEnums.LeaveType.CHILDREN.getValue()),
        ANNUAL(CommonEnums.LeaveType.ANNUAL.getValue()),
        SICK(CommonEnums.LeaveType.SICK.getValue()),
        EMERGENCY(CommonEnums.LeaveType.EMERGENCY.getValue());

        private final String value;

        LeaveType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
        
        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * Represents the possible departments in the organization
     */
    public enum Department {
        HUMAN_RESOURCE(CommonEnums.Department.HUMAN_RESOURCE.getValue()),
        ENGINEERING("Engineering"),
        FINANCE("Finance"),
        OPERATIONS("Operations"),
        MARKETING("Marketing");

        private final String value;

        Department(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
        
        @Override
        public String toString() {
            return value;
        }
    }
}