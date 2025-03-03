package com.homebuilder.dto;

import com.homebuilder.entity.Device;
import com.homebuilder.entity.Room;
import com.homebuilder.exception.DeviceNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author André Heinen
 */
public class RoomResponse {

	private final Long id;
	private final String name;
	private final Long ownerId;

	private final List<Long> deviceIds = new ArrayList<>();

	public RoomResponse(Room room) {
		if (room == null) {
			throw new DeviceNotFoundException("Room not found");
		}
		this.id = room.getId();
		this.name = room.getName();
		this.ownerId = room.getUserId();
		for (Device device : room.getDevices()) {
			this.deviceIds.add(device.getId());
		}
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public List<Long> getDeviceIds() {
		return deviceIds;
	}
}
