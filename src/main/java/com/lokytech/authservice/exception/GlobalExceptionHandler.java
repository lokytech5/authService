package com.lokytech.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<String> handleExternalServiceError(ExternalServiceException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_GATEWAY); // or another appropriate HTTP status
    }
}
