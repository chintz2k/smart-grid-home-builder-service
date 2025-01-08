package com.homebuilder.dto;

import com.homebuilder.entity.Storage;

/**
 * @author André Heinen
 */
public class StorageResponse {

	private final Long id;
	private final String name;
	private final boolean active;
	private final boolean archived;
	private final double capacity;
	private final double currentCharge;
	private final int chargingPriority;
	private final int consumingPriority;

	public StorageResponse(Storage storage) {
		this.id = storage.getId();
		this.name = storage.getName();
		this.active = storage.isActive();
		this.archived = storage.isArchived();
		this.capacity = storage.getCapacity();
		this.currentCharge = storage.getCurrentCharge();
		this.chargingPriority = storage.getChargingPriority();
		this.consumingPriority = storage.getConsumingPriority();
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

	public double getCapacity() {
		return capacity;
	}

	public double getCurrentCharge() {
		return currentCharge;
	}

	public int getChargingPriority() {
		return chargingPriority;
	}

	public int getConsumingPriority() {
		return consumingPriority;
	}
}
