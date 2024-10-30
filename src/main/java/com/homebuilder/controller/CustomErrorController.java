package com.homebuilder.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author André Heinen
 */
@RestController
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {

	@RequestMapping
	public ResponseEntity<String> handleError(HttpServletRequest request) {
		int statusCode = (int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		if (statusCode == HttpStatus.NOT_FOUND.value()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("The requested resource was not found on the server.");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("An unexpected error occurred.");
	}
}