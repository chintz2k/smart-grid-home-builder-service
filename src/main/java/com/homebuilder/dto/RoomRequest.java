package com.homebuilder.dto;

import com.homebuilder.entity.Room;
import com.homebuilder.validation.UniqueRoomName;
import jakarta.validation.constraints.NotBlank;

/**
 * @author André Heinen
 */
@UniqueRoomName
public class RoomRequest {

	private Long id;

	@NotBlank(message = "name is required")
	private String name;

	public RoomRequest() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Room toEntity() {
		Room room = new Room();
		room.setName(name);
		return room;
	}
}
