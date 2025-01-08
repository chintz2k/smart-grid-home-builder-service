package com.homebuilder.dto;

import com.homebuilder.entity.Room;

/**
 * @author André Heinen
 */
public class RoomResponse {

	private final Long id;
	private final String name;

	public RoomResponse(Room room) {
		this.id = room.getId();
		this.name = room.getName();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
