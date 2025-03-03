package com.homebuilder.dto;

import com.homebuilder.entity.Device;
import com.homebuilder.exception.DeviceNotFoundException;

/**
 * @author André Heinen
 */
public class DeviceResponse {

	private final Long id;
	private final String name;
	private final Long ownerId;
	private final boolean active;
	private final boolean archived;

	public DeviceResponse(Device device) {
		if (device == null) {
			throw new DeviceNotFoundException("Device not found");
		}
		this.id = device.getId();
		this.name = device.getName();
		this.ownerId = device.getUserId();
		this.active = device.isActive();
		this.archived = device.isArchived();
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

	public boolean isActive() {
		return active;
	}

	public boolean isArchived() {
		return archived;
	}
}
