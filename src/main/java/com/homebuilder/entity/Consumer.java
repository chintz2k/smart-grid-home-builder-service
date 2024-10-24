package com.homebuilder.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/**
 * @author André Heinen
 */
@Entity
public class Consumer extends Device {

	@NotNull(message = "Power consumption is required")
	@DecimalMin(value = "0.001", inclusive = true, message = "Power consumption must be at least 0.001")
	private double powerConsumption;

	public Consumer() {

	}

	public double getPowerConsumption() {
		return powerConsumption;
	}

	public void setPowerConsumption(double powerConsumption) {
		this.powerConsumption = powerConsumption;
	}
}
