package com.homebuilder.exception;

/**
 * @author André Heinen
 */
public class DuplicateRoomNameException extends RuntimeException {
	public DuplicateRoomNameException(String message) {
		super(message);
	}
}
