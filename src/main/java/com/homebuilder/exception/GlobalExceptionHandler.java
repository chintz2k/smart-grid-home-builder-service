package com.homebuilder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * @author André Heinen
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DeviceNotFoundException.class)
	public ResponseEntity<String> handleDeviceNotFoundException(DeviceNotFoundException ex, WebRequest request) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UnauthorizedAccessException.class)
	public ResponseEntity<String> handleUnauthorizedAccessException(UnauthorizedAccessException ex, WebRequest request) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<String> handleTokenExpiredException(TokenExpiredException ex, WebRequest request) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<String> handleInvalidTokenException(InvalidTokenException ex, WebRequest request) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(InvalidJwtException.class)
	public ResponseEntity<String> handleInvalidJwtException(InvalidJwtException ex, WebRequest request) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	// Exception Handler für allgemeine Fehler
	/*@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGlobalException(Exception ex, WebRequest request) {
		return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);  // 500
	}*/
}
