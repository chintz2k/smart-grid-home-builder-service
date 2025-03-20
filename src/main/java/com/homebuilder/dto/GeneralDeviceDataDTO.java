package com.homebuilder.dto;

import com.homebuilder.entity.Consumer;
import com.homebuilder.entity.Producer;
import com.homebuilder.entity.Storage;

/**
 * @author André Heinen
 */
public class GeneralDeviceDataDTO {

	private final Long id;
	private final String name;
	private final Long userId;
	private final boolean active;
	private final boolean archived;
	private double powerConsumption;
	private double powerProduction;
	private boolean renewable;
	private String powerType;
	private double capacity;
	private int chargingPriority;
	private int consumingPriority;

	public GeneralDeviceDataDTO(Consumer consumer) {
		this.id = consumer.getId();
		this.name = consumer.getName();
		this.userId = consumer.getUserId();
		this.active = consumer.isActive();
		this.archived = consumer.isArchived();
		this.powerConsumption = consumer.getPowerConsumption();
	}

	public GeneralDeviceDataDTO(Producer producer) {
		this.id = producer.getId();
		this.name = producer.getName();
		this.userId = producer.getUserId();
		this.active = producer.isActive();
		this.archived = producer.isArchived();
		this.powerProduction = producer.getPowerProduction();
		this.renewable = producer.isRenewable();
		this.powerType = producer.getPowerType();
	}

	public GeneralDeviceDataDTO(Storage storage) {
		this.id = storage.getId();
		this.name = storage.getName();
		this.userId = storage.getUserId();
		this.active = storage.isActive();
		this.archived = storage.isArchived();
		this.capacity = storage.getCapacity();
		this.chargingPriority = storage.getChargingPriority();
		this.consumingPriority = storage.getConsumingPriority();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Long getUserId() {
		return userId;
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

	public double getPowerProduction() {
		return powerProduction;
	}

	public boolean isRenewable() {
		return renewable;
	}

	public String getPowerType() {
		return powerType;
	}

	public double getCapacity() {
		return capacity;
	}

	public int getChargingPriority() {
		return chargingPriority;
	}

	public int getConsumingPriority() {
		return consumingPriority;
	}
}
