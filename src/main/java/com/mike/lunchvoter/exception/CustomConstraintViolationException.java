package com.mike.lunchvoter.exception;

public class CustomConstraintViolationException extends RuntimeException {
    public CustomConstraintViolationException(String message) {
        super(message);
    }
}
