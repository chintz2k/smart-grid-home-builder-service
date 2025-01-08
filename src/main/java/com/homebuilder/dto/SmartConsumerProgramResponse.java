package com.homebuilder.dto;

import com.homebuilder.entity.SmartConsumerProgram;

/**
 * @author André Heinen
 */
public class SmartConsumerProgramResponse {

	private final Long id;
	private final String name;
	private final int durationInSeconds;
	private final double powerConsumption;
	private final Long smartConsumerId;
	private final boolean archived;

	public SmartConsumerProgramResponse(SmartConsumerProgram smartConsumerProgram) {
		this.id = smartConsumerProgram.getId();
		this.name = smartConsumerProgram.getName();
		this.durationInSeconds = smartConsumerProgram.getDurationInSeconds();
		this.powerConsumption = smartConsumerProgram.getPowerConsumption();
		this.smartConsumerId = smartConsumerProgram.getSmartConsumer().getId();
		this.archived = smartConsumerProgram.isArchived();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getDurationInSeconds() {
		return durationInSeconds;
	}

	public double getPowerConsumption() {
		return powerConsumption;
	}

	public Long getSmartConsumerId() {
		return smartConsumerId;
	}

	public boolean isArchived() {
		return archived;
	}
}
