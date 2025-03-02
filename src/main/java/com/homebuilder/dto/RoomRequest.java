package com.homebuilder.dto;

import com.homebuilder.entity.Room;
import com.homebuilder.validation.UniqueRoomName;
import jakarta.validation.constraints.NotBlank;

/**
 * @author André Heinen
 */
@UniqueRoomName
public class RoomRequest {

	private Long id = null;

	@NotBlank(message = "name is required")
	private String name;

	private Long ownerId = null;

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

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * Converts this RoomRequest object into a Room entity object.
	 * Only for new Rooms. If you want to update a Room, refer directly to the fields of the RoomRequest.
	 *
	 * @return a new Room entity, only for creating new Rooms.
	 */
	public Room toEntity() {
		Room room = new Room();
		room.setName(name);
		return room;
	}
}
