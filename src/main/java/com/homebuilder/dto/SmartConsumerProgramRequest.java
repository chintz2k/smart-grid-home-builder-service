package com.homebuilder.dto;

import com.homebuilder.entity.SmartConsumerProgram;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author André Heinen
 */
public class SmartConsumerProgramRequest {

	private Long id;

	@NotBlank(message = "name is required")
	private String name;

	@NotNull(message = "Duration is required")
	@Min(value = 1, message = "Duration must be at least 1")
	private int durationInSeconds;

	@NotNull(message = "Power consumption is required")
	@DecimalMin(value = "0.001", message = "Power consumption must be at least 0.001")
	private double powerConsumption;

	@NotNull(message = "SmartConsumer ID is required")
	private Long smartConsumerId;

	private boolean archived = false;

	public SmartConsumerProgramRequest() {

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

	public Long getSmartConsumerId() {
		return smartConsumerId;
	}

	public void setSmartConsumerId(Long smartConsumerId) {
		this.smartConsumerId = smartConsumerId;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public SmartConsumerProgram toEntity() {
		SmartConsumerProgram smartConsumerProgram = new SmartConsumerProgram();
		smartConsumerProgram.setName(name);
		smartConsumerProgram.setDurationInSeconds(durationInSeconds);
		smartConsumerProgram.setPowerConsumption(powerConsumption);
		smartConsumerProgram.setArchived(archived);
		return smartConsumerProgram;
	}
}
