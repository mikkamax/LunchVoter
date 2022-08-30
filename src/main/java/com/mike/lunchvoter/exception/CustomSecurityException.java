package com.mike.lunchvoter.exception;

public class CustomSecurityException extends RuntimeException {
    public CustomSecurityException(String message) {
        super(message);
    }
}
