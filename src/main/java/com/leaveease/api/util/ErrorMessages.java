package com.leaveease.api.util;

import lombok.Getter;

@Getter
public enum ErrorMessages {

    LOGIN_INVALID_CREDENTIALS("Invalid staff number or password.");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }
}
