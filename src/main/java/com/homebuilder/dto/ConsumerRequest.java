package com.homebuilder.dto;

import com.homebuilder.entity.Consumer;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author André Heinen
 */
public class ConsumerRequest {

	private Long id;

	@NotBlank(message = "name is required")
	private String name;

	private boolean active = false;

	private boolean archived = false;

	@NotNull(message = "Power consumption is required")
	@DecimalMin(value = "0.001", message = "Power consumption must be at least 0.001")
	private double powerConsumption;

	public ConsumerRequest() {

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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public double getPowerConsumption() {
		return powerConsumption;
	}

	public void setPowerConsumption(double powerConsumption) {
		this.powerConsumption = powerConsumption;
	}

	public Consumer toEntity() {
		Consumer consumer = new Consumer();
		consumer.setName(name);
		consumer.setPowerConsumption(powerConsumption);
		return consumer;
	}
}
