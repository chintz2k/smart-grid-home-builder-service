package com.homebuilder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

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
	public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
		Map<String, String> error = new HashMap<>();
		error.put("error", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<Map<String, String>> handleNotFound(NoHandlerFoundException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put("error", "The requested resource was not found on the server");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
	}

}
