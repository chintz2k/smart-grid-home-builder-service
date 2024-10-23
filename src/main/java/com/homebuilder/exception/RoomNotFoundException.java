package com.homebuilder.exception;

/**
 * @author André Heinen
 */
public class RoomNotFoundException extends RuntimeException {
	public RoomNotFoundException(String message) {
		super(message);
	}
}
