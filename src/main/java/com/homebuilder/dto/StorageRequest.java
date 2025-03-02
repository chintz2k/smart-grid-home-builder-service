package com.homebuilder.dto;

import com.homebuilder.entity.Storage;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author André Heinen
 */
public class StorageRequest {

	private Long id = null;

	@NotBlank(message = "name is required")
	private String name;

	private Long ownerId = null;

	private boolean active = false;

	private boolean archived = false;

	@NotNull(message = "Capacity is required")
	@DecimalMin(value = "1.0", inclusive = true, message = "Capacity must be at least 1")
	private double capacity;

	private double currentCharge = 0.0;

	@NotNull(message = "Charging priority is required")
	@Max(value = 10, message = "Charging priority must be between 0 and 10")
	private int chargingPriority = 0;

	@NotNull(message = "Consuming priority is required")
	@Max(value = 10, message = "Consuming priority must be between 0 and 10")
	private int consumingPriority = 0;

	public StorageRequest() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public double getCapacity() {
		return capacity;
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public double getCurrentCharge() {
		return currentCharge;
	}

	public void setCurrentCharge(double currentCharge) {
		this.currentCharge = currentCharge;
	}

	public int getChargingPriority() {
		return chargingPriority;
	}

	public void setChargingPriority(int chargingPriority) {
		this.chargingPriority = chargingPriority;
	}

	public int getConsumingPriority() {
		return consumingPriority;
	}

	public void setConsumingPriority(int consumingPriority) {
		this.consumingPriority = consumingPriority;
	}

	/**
	 * Converts this StorageRequest object into a Storage entity object.
	 * Only for new Storages. If you want to update a Storage, refer directly to the fields of the StorageRequest.
	 *
	 * @return a new Storage entity, only for creating new Storages.
	 */
	public Storage toEntity() {
		Storage storage = new Storage();
		storage.setName(name);
		storage.setCapacity(capacity);
		storage.setCurrentCharge(currentCharge);
		storage.setChargingPriority(chargingPriority);
		storage.setConsumingPriority(consumingPriority);
		return storage;
	}
}
