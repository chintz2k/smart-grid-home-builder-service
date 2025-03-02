package com.homebuilder.dto;

import com.homebuilder.entity.SmartConsumer;
import com.homebuilder.exception.DeviceNotFoundException;

/**
 * @author André Heinen
 */
public class SmartConsumerResponse {

	private final Long id;
	private final String name;
	private final double powerConsumption;
	private final boolean active;
	private final boolean archived;

	public SmartConsumerResponse(SmartConsumer smartConsumer) {
		if (smartConsumer == null) {
			throw new DeviceNotFoundException("SmartConsumer not found");
		}
		this.id = smartConsumer.getId();
		this.name = smartConsumer.getName();
		this.powerConsumption = smartConsumer.getPowerConsumption();
		this.active = smartConsumer.isActive();
		this.archived = smartConsumer.isArchived();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getPowerConsumption() {
		return powerConsumption;
	}

	public boolean isActive() {
		return active;
	}

	public boolean isArchived() {
		return archived;
	}
}
