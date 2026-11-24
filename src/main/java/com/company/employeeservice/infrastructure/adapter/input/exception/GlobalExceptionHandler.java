package com.company.employeeservice.infrastructure.adapter.input.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolationException;

import com.company.employeeservice.domain.exception.EmployeeNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger =
	        LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(EmployeeNotFoundException.class)
	public ResponseEntity<String> handleEmployeeNotFound(
	        EmployeeNotFoundException exception) {

	    return ResponseEntity
	            .status(HttpStatus.NOT_FOUND)
	            .body(exception.getMessage());
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<String> handleValidationException(
	        ConstraintViolationException exception) {

	    return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(exception.getMessage());
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgument(
	        IllegalArgumentException exception) {

	    return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(exception.getMessage());
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleMethodArgumentNotValid(
	        MethodArgumentNotValidException exception) {

	    String message = exception.getBindingResult()
	            .getFieldErrors()
	            .stream()
	            .map(error -> error.getField() + ": " + error.getDefaultMessage())
	            .findFirst()
	            .orElse("Invalid request data");

	    return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(message);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGeneralException(
	        Exception exception) {

	    logger.error("Unexpected application error", exception);

	    return ResponseEntity
	            .status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body("An unexpected internal error occurred");
	}

}
