package com.example.TaskManager.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.TaskManager.dto.ApiResponse;
import com.example.TaskManager.exception.TaskNotFoundException;
import com.example.TaskManager.exception.TaskUpdateException;

import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({ TaskNotFoundException.class, TaskUpdateException.class })
    public ResponseEntity<ApiResponse<String>> handleTaskExceptions(RuntimeException e) {
        ApiResponse<String> response = ApiResponse.failure(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleConstraintViolationException(ConstraintViolationException e) {
        String errorMessage = "Validation error: " + e.getMessage();
        ApiResponse<String> response = ApiResponse.failure(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleTaskValidationExceptions(MethodArgumentNotValidException e) {
        String errorMessage = "Validation error: " + e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ApiResponse<String> response = ApiResponse.failure(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleGenericRuntimeException(RuntimeException e) {
        logger.error("Unexpected error occurred : {}", e.getMessage(), e);
        ApiResponse<String> response = ApiResponse.failure("An unexpected error ocurred, Please try again later");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
