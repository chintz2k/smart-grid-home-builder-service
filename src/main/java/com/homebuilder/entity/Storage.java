package com.homebuilder.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * @author André Heinen
 */
@Entity
public class Storage extends Device {

	@NotNull(message = "capacity is required")
	@Min(value = 1, message = "Capacity must greater than 1")
	private double capacity;

	@NotNull(message = "Current charge is required")
	@PositiveOrZero(message = "Current charge must be greater than or equal to 0")
	private double currentCharge = 0.0;

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
}
