package com.mike.lunchvoter.exception;

public class AlreadyVotedTodayException extends RuntimeException {
    public AlreadyVotedTodayException(String message) {
        super(message);
    }
}
