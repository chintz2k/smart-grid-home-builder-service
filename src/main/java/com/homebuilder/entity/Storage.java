package com.homebuilder.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * @author André Heinen
 */
@Entity
public class Storage extends Device {

	@NotNull(message = "Capacity is required")
	@DecimalMin(value = "1.0", inclusive = true, message = "Capacity must be at least 1")
	private double capacity;

	@NotNull(message = "Current charge is required")
	@PositiveOrZero(message = "Current charge must be non-negative")
	private double currentCharge = 0.0;

	@NotNull(message = "Charging priority is required")
	@Max(value = 10, message = "Charging priority must be between 0 and 10")
	private int chargingPriority = 0;

	@NotNull(message = "Consuming priority is required")
	@Max(value = 10, message = "Consuming priority must be between 0 and 10")
	private int consumingPriority = 0;

	public Storage() {

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
}