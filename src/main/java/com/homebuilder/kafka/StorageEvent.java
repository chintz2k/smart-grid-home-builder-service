package com.homebuilder.kafka;

import java.time.Instant;

/**
 * @author André Heinen
 */
public class StorageEvent {

	private Long deviceId;
	private Long ownerId;
	private boolean commercial;
	private boolean active;
	private double capacity;
	private double currentCharge;
	private int chargingPriority;
	private int consumingPriority;
	private Instant timestamp;

	public StorageEvent() {

	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public boolean isCommercial() {
		return commercial;
	}

	public void setCommercial(boolean commercial) {
		this.commercial = commercial;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}
}
