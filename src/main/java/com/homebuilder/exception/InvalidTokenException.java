package com.homebuilder.exception;

/**
 * @author André Heinen
 */
public class InvalidTokenException extends RuntimeException {
	public InvalidTokenException(String message) {
		super(message);
	}
}
