package com.leaveease.api.util;

/**
 * Common enums used throughout the application
 */
public class CommonEnums {

    /**
     * Represents the possible statuses of a leave application
     */
    public enum LeaveStatus {
        APPROVED("Approved"),
        REJECTED("Rejected"),
        CANCELLED("Cancelled"),
        PENDING("Pending");

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
        ADMIN("Admin"),
        USER("User");

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
        CHILDREN("Children"),
        ANNUAL("Annual"),
        SICK("Sick"),
        EMERGENCY("Emergency");

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
        HUMAN_RESOURCE("Human Resources"),
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
