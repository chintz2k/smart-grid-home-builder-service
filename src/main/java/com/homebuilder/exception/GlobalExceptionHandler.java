package com.homebuilder.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author André Heinen
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DeviceNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleDeviceNotFoundException(DeviceNotFoundException ex, WebRequest request) {
		Map<String, String> error = new HashMap<>();
		error.put("error", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(UnauthorizedAccessException.class)
	public ResponseEntity<Map<String, String>> handleUnauthorizedAccessException(UnauthorizedAccessException ex, WebRequest request) {
		Map<String, String> error = new HashMap<>();
		error.put("error", ex.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
	}

	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<Map<String, String>> handleTokenExpiredException(TokenExpiredException ex, WebRequest request) {
		Map<String, String> error = new HashMap<>();
		error.put("error", ex.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<Map<String, String>> handleInvalidTokenException(InvalidTokenException ex, WebRequest request) {
		Map<String, String> error = new HashMap<>();
		error.put("error", ex.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}

	@ExceptionHandler(InvalidJwtException.class)
	public ResponseEntity<Map<String, String>> handleInvalidJwtException(InvalidJwtException ex, WebRequest request) {
		Map<String, String> error = new HashMap<>();
		error.put("error", ex.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		Map<String, Object> errorMap = new HashMap<>();
		errorMap.put("error", "Validation failed");
		List<Map<String, String>> fieldErrors = ex.getBindingResult().getFieldErrors().stream().map(error -> {
			Map<String, String> fieldError = new HashMap<>();
			fieldError.put("field", error.getField());
			fieldError.put("message", error.getDefaultMessage());
			return fieldError;
		}).toList();
		List<Map<String, String>> objectErrors = ex.getBindingResult().getGlobalErrors().stream().map(error -> {
			Map<String, String> globalError = new HashMap<>();
			globalError.put("object", error.getObjectName());
			globalError.put("message", error.getDefaultMessage());
			return globalError;
		}).toList();
		List<Map<String, String>> allErrors = new ArrayList<>();
		allErrors.addAll(fieldErrors);
		allErrors.addAll(objectErrors);
		errorMap.put("details", allErrors);
		return ResponseEntity.badRequest().body(errorMap);
	}

	@ExceptionHandler(SecurityException.class)
	public ResponseEntity<Map<String, String>> handleSecurityException(SecurityException ex) {
		Map<String, String> error = new HashMap<>();
		error.put("error", ex.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}

	@ExceptionHandler(TimeslotOverlapException.class)
	public ResponseEntity<Map<String, Object>> handleTimeslotOverlapException(TimeslotOverlapException ex, WebRequest request) {
		Map<String, Object> error = new HashMap<>();
		error.put("error", ex.getMessage());
		error.put("overlappingTimeslots", ex.getOverlappingTimeslots().stream().map(timeslot -> {
			Map<String, Object> timeslotInfo = new HashMap<>();
			timeslotInfo.put("id", timeslot.getId());
			timeslotInfo.put("startTime", timeslot.getStartTime());
			timeslotInfo.put("endTime", timeslot.getEndTime());
			return timeslotInfo;
		}));
		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
		String errorMessage = ex.getConstraintViolations().stream()
				.map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
				.collect(Collectors.joining(", "));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<Map<String, String>> handleNotFound(NoHandlerFoundException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put("error", "The requested resource was not found on the server");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
	}

}
