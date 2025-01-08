package com.homebuilder.dto;

import com.homebuilder.entity.Device;

/**
 * @author André Heinen
 */
public class DeviceResponse {

	private final Long id;
	private final String name;
	private final boolean active;
	private final boolean archived;

	public DeviceResponse(Device device) {
		this.id = device.getId();
		this.name = device.getName();
		this.active = device.isActive();
		this.archived = device.isArchived();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isActive() {
		return active;
	}

	public boolean isArchived() {
		return archived;
	}
}
