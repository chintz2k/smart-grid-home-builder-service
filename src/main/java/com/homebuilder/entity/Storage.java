package com.homebuilder.entity;

import jakarta.persistence.Entity;

/**
 * @author André Heinen
 */
@Entity
public class Storage extends Device {

	private int capacity;
	private int currentCharge;

	public Storage() {

	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getCurrentCharge() {
		return currentCharge;
	}

	public void setCurrentCharge(int currentCharge) {
		this.currentCharge = currentCharge;
	}
}
