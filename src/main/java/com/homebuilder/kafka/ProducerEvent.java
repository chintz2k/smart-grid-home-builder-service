package com.homebuilder.kafka;

import java.time.Instant;

/**
 * @author André Heinen
 */
public class ProducerEvent {

	private Long deviceId;
	private Long ownerId;
	private boolean commercial;
	private boolean active;
	private String powerType;
	private boolean renewable;
	private double powerProduction;
	private Instant timestamp;

	public ProducerEvent() {

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

	public String getPowerType() {
		return powerType;
	}

	public void setPowerType(String powerType) {
		this.powerType = powerType;
	}

	public boolean isRenewable() {
		return renewable;
	}

	public void setRenewable(boolean renewable) {
		this.renewable = renewable;
	}

	public double getPowerProduction() {
		return powerProduction;
	}

	public void setPowerProduction(double powerProduction) {
		this.powerProduction = powerProduction;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}
}
