package com.homebuilder.exception;

/**
 * @author André Heinen
 */
public class InvalidJwtException extends RuntimeException {
	public InvalidJwtException(String message) {
		super(message);
	}
}
