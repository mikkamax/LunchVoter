package com.mike.lunchvoter.controller.advice;

import com.mike.lunchvoter.exception.CustomConstraintViolationException;
import com.mike.lunchvoter.exception.IllegalRequestDataException;
import com.mike.lunchvoter.exception.ObjectNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    public ErrorDetails handleObjectNotFound(ObjectNotFoundException e) {
        logOnException(e);
        return new ErrorDetails(
                LocalDateTime.now(),
                "ObjectNotFound",
                e.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalRequestDataException.class)
    public ErrorDetails handleIllegalRequestData(IllegalRequestDataException e) {
        logOnException(e);

        return new ErrorDetails(
                LocalDateTime.now(),
                "IllegalRequestData",
                e.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorDetails handleConstraintViolation(ConstraintViolationException e) {
        logOnException(e);

        return new ErrorDetails(
                LocalDateTime.now(),
                "ConstraintViolation",
                e.getConstraintViolations().stream()
                        .map(constraint -> formatErrorMessage(
                                Objects.toString(constraint.getLeafBean()),
                                Objects.toString(constraint.getPropertyPath()),
                                constraint.getMessage())
                        )
                        .collect(Collectors.joining(", ", "Validation error: ", "."))
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorDetails handleDataIntegrityViolation(DataIntegrityViolationException e) {
        logOnException(e);

        return new ErrorDetails(
                LocalDateTime.now(),
                "DataIntegrityViolation",
                e.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomConstraintViolationException.class)
    public ErrorDetails handleCustomConstraintViolation(CustomConstraintViolationException e) {
        logOnException(e);

        return new ErrorDetails(
                LocalDateTime.now(),
                "CustomConstraintViolation",
                e.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorDetails handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        logOnException(e);

        String message = e.getAllErrors().stream()
                .map(objectError -> formatErrorMessage(
                        objectError.getObjectName(),
                        ((FieldError) objectError).getField(),
                        objectError.getDefaultMessage())
                )
                .collect(Collectors.joining(", ", "Validation error: ", "."));

        return new ErrorDetails(
                LocalDateTime.now(),
                "MethodArgumentNotValid",
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
}
