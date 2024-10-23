package com.homebuilder.entity;

import jakarta.persistence.Entity;

/**
 * @author André Heinen
 */
@Entity
public class Consumer extends Device {

	private double powerConsumption;

	public Consumer() {

	}

	public Consumer(int powerConsumption) {
		this.powerConsumption = powerConsumption;
	}

	public Consumer(String name, Long ownerId, double powerConsumption) {
		super(name, ownerId);
		this.powerConsumption = powerConsumption;
	}

	public double getPowerConsumption() {
		return powerConsumption;
	}

	public void setPowerConsumption(double powerConsumption) {
		this.powerConsumption = powerConsumption;
	}
}
