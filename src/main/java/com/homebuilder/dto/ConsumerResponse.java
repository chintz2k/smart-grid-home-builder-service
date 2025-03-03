package com.homebuilder.dto;

import com.homebuilder.entity.Consumer;
import com.homebuilder.exception.DeviceNotFoundException;

/**
 * @author André Heinen
 */
public class ConsumerResponse {

	private final Long id;
	private final String name;
	private final Long ownerId;
	private final boolean active;
	private final boolean archived;
	private final double powerConsumption;

	public ConsumerResponse(Consumer consumer) {
		if (consumer == null) {
			throw new DeviceNotFoundException("Consumer not found");
		}
		this.id = consumer.getId();
		this.name = consumer.getName();
		this.ownerId = consumer.getUserId();
		this.active = consumer.isActive();
		this.archived = consumer.isArchived();
		this.powerConsumption = consumer.getPowerConsumption();
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

	public double getPowerConsumption() {
		return powerConsumption;
	}
}
