package com.mike.lunchvoter.controller.advice;

import com.mike.lunchvoter.exception.CustomConstraintViolationException;
import com.mike.lunchvoter.exception.IllegalRequestDataException;
import com.mike.lunchvoter.exception.ObjectNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Collector<CharSequence, ?, String> VALIDATION_ERROR_COLLECTOR =
            Collectors.joining(", ", "Validation error: ", ".");

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorDetails handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logOnException(e);
        return createErrorDetails(
                "HttpMessageNotReadableException",
                e.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    public ErrorDetails handleObjectNotFoundException(ObjectNotFoundException e) {
        logOnException(e);
        return createErrorDetails(
                "ObjectNotFoundException",
                e.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalRequestDataException.class)
    public ErrorDetails handleIllegalRequestDataException(IllegalRequestDataException e) {
        logOnException(e);

        return createErrorDetails(
                "IllegalRequestDataException",
                e.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorDetails handleConstraintViolationException(ConstraintViolationException e) {
        logOnException(e);

        return createErrorDetails(
                "ConstraintViolationException",
                e.getConstraintViolations().stream()
                        .map(constraint -> formatErrorMessage(
                                Objects.toString(constraint.getLeafBean()),
                                Objects.toString(constraint.getPropertyPath()),
                                constraint.getMessage())
                        )
                        .collect(VALIDATION_ERROR_COLLECTOR)
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorDetails handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        logOnException(e);

        return createErrorDetails(
                "DataIntegrityViolationException",
                e.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomConstraintViolationException.class)
    public ErrorDetails handleCustomConstraintViolationException(CustomConstraintViolationException e) {
        logOnException(e);

        return createErrorDetails(
                "CustomConstraintViolationException",
                e.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorDetails handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logOnException(e);

        String message = e.getAllErrors().stream()
                .map(objectError -> formatErrorMessage(
                        objectError.getObjectName(),
                        ((FieldError) objectError).getField(),
                        objectError.getDefaultMessage())
                )
                .collect(VALIDATION_ERROR_COLLECTOR);

        return createErrorDetails(
                "MethodArgumentNotValidException",
                message
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorDetails handleBindException(BindException e) {
        logOnException(e);

        String message = e.getAllErrors().stream()
                .map(objectError -> formatErrorMessage(
                        objectError.getObjectName(),
                        ((FieldError) objectError).getField(),
                        objectError.getDefaultMessage())
                )
                .collect(VALIDATION_ERROR_COLLECTOR);

        return createErrorDetails(
                "BindException",
                message
        );
    }

    private void logOnException(Exception e) {
        log.error("Service error: " + e);
    }

    private String formatErrorMessage(String errorBean, String errorProperty, String errorExplanation) {
        return String.format("%s '%s' property %s",
                errorBean,
                errorProperty,
                errorExplanation
        );
    }

    private ErrorDetails createErrorDetails(String message, String errorDetails) {
        return new ErrorDetails(
                LocalDateTime.now(),
                message,
                errorDetails
        );
    }
}
