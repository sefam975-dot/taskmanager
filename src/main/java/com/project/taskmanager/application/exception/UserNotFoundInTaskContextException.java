package com.project.taskmanager.application.exception;

public class UserNotFoundInTaskContextException extends RuntimeException {
    public UserNotFoundInTaskContextException(String message) {
        super(message);
    }
}
