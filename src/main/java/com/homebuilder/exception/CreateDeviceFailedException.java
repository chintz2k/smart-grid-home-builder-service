package com.homebuilder.exception;

/**
 * @author André Heinen
 */
public class CreateDeviceFailedException extends RuntimeException {
	public CreateDeviceFailedException(String message) {
		super(message);
	}
}
