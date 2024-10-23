package com.homebuilder.exception;

/**
 * @author André Heinen
 */
public class TokenExpiredException extends RuntimeException {
	public TokenExpiredException(String message) {
		super(message);
	}
}
