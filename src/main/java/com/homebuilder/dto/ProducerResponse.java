package com.homebuilder.dto;

import com.homebuilder.entity.Producer;
import com.homebuilder.exception.DeviceNotFoundException;

/**
 * @author André Heinen
 */
public class ProducerResponse {

	private final Long id;
	private final String name;
	private final Long ownerId;
	private final boolean active;
	private final boolean archived;
	private final double powerProduction;
	private final boolean renewable;
	private final String powerType;
	private final Long roomId;

	public ProducerResponse(Producer producer) {
		if (producer == null) {
			throw new DeviceNotFoundException("Producer not found");
		}
		this.id = producer.getId();
		this.name = producer.getName();
		this.ownerId = producer.getUserId();
		this.active = producer.isActive();
		this.archived = producer.isArchived();
		this.powerProduction = producer.getPowerProduction();
		this.renewable = producer.isRenewable();
		this.powerType = producer.getPowerType();
		this.roomId = producer.getRoom().getId();
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

	public double getPowerProduction() {
		return powerProduction;
	}

	public boolean isRenewable() {
		return renewable;
	}

	public String getPowerType() {
		return powerType;
	}

	public Long getRoomId() {
		return roomId;
	}
}
