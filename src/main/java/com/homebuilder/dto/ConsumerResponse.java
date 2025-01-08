package com.homebuilder.dto;

import com.homebuilder.entity.Consumer;

/**
 * @author André Heinen
 */
public class ConsumerResponse {

	private final Long id;
	private final String name;
	private final boolean active;
	private final boolean archived;
	private final double powerConsumption;

	public ConsumerResponse(Consumer consumer) {
		this.id = consumer.getId();
		this.name = consumer.getName();
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
