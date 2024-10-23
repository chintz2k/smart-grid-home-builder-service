package com.homebuilder.entity;

import jakarta.persistence.Entity;

/**
 * @author André Heinen
 */
@Entity
public class Producer extends Device {

	private int powerProduction;

	public Producer() {

	}

	public int getPowerProduction() {
		return powerProduction;
	}

	public void setPowerProduction(int powerProduction) {
		this.powerProduction = powerProduction;
	}
}
