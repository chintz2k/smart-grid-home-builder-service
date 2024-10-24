package com.homebuilder.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/**
 * @author André Heinen
 */
@Entity
public class Producer extends Device {

	@NotNull(message = "production is required")
	@DecimalMin(value = "0.001", inclusive = true, message = "Power production must be at least 0.001")
	private double powerProduction;

	public Producer() {

	}

	public double getPowerProduction() {
		return powerProduction;
	}

	public void setPowerProduction(double powerProduction) {
		this.powerProduction = powerProduction;
	}
}
