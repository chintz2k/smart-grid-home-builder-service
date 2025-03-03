package com.homebuilder.dto;

import com.homebuilder.entity.SmartConsumerProgram;
import com.homebuilder.exception.DeviceNotFoundException;

/**
 * @author André Heinen
 */
public class SmartConsumerProgramResponse {

	private final Long id;
	private final String name;
	private final Long ownerId;
	private final boolean archived;
	private final Long smartConsumerId;
	private final int durationInSeconds;
	private final double powerConsumption;

	public SmartConsumerProgramResponse(SmartConsumerProgram smartConsumerProgram) {
		if (smartConsumerProgram == null) {
			throw new DeviceNotFoundException("SmartConsumerProgram not found");
		}
		this.id = smartConsumerProgram.getId();
		this.name = smartConsumerProgram.getName();
		this.ownerId = smartConsumerProgram.getUserId();
		this.archived = smartConsumerProgram.isArchived();
		this.smartConsumerId = smartConsumerProgram.getSmartConsumer().getId();
		this.durationInSeconds = smartConsumerProgram.getDurationInSeconds();
		this.powerConsumption = smartConsumerProgram.getPowerConsumption();
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

	public boolean isArchived() {
		return archived;
	}

	public Long getSmartConsumerId() {
		return smartConsumerId;
	}

	public int getDurationInSeconds() {
		return durationInSeconds;
	}

	public double getPowerConsumption() {
		return powerConsumption;
	}
}
