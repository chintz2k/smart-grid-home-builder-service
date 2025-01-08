package com.homebuilder.entity;

import jakarta.persistence.*;

/**
 * @author André Heinen
 */
@Entity
public class SmartConsumerProgram {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private int durationInSeconds;

	private double powerConsumption;

	@ManyToOne
	private SmartConsumer smartConsumer;

	private Long userId;

	private boolean archived = false;

	public SmartConsumerProgram() {

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

	public int getDurationInSeconds() {
		return durationInSeconds;
	}

	public void setDurationInSeconds(int durationInSeconds) {
		this.durationInSeconds = durationInSeconds;
	}

	public double getPowerConsumption() {
		return powerConsumption;
	}

	public void setPowerConsumption(double powerConsumption) {
		this.powerConsumption = powerConsumption;
	}

	public SmartConsumer getSmartConsumer() {
		return smartConsumer;
	}

	public void setSmartConsumer(SmartConsumer smartConsumer) {
		this.smartConsumer = smartConsumer;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}
}
