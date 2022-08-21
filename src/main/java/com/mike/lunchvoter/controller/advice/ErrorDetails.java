package com.mike.lunchvoter.controller.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ErrorDetails {
    private LocalDateTime serverDateTime;
    private String message;
    private String errorDetails;
}