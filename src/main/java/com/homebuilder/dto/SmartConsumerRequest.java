package com.homebuilder.dto;

import com.homebuilder.entity.SmartConsumer;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author André Heinen
 */
public class SmartConsumerRequest {

	private Long id = null;

	@NotBlank(message = "name is required")
	private String name;

	private Long ownerId = null;

	@NotNull(message = "Power consumption is required")
	@DecimalMin(value = "0.001", message = "Power consumption must be at least 0.001")
	private double powerConsumption;

	private boolean active = false;

	private boolean archived = false;

	private Long roomId = null;

	private String smartConsumerProgramName = null;
	private int smartConsumerProgramDurationInSeconds;
	private double smartConsumerProgramPowerConsumption;

	public SmartConsumerRequest() {

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

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public double getPowerConsumption() {
		return powerConsumption;
	}

	public void setPowerConsumption(double powerConsumption) {
		this.powerConsumption = powerConsumption;
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

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public String getSmartConsumerProgramName() {
		return smartConsumerProgramName;
	}

	public void setSmartConsumerProgramName(String smartConsumerProgramName) {
		this.smartConsumerProgramName = smartConsumerProgramName;
	}

	public int getSmartConsumerProgramDurationInSeconds() {
		return smartConsumerProgramDurationInSeconds;
	}

	public void setSmartConsumerProgramDurationInSeconds(int smartConsumerProgramDurationInSeconds) {
		this.smartConsumerProgramDurationInSeconds = smartConsumerProgramDurationInSeconds;
	}

	public double getSmartConsumerProgramPowerConsumption() {
		return smartConsumerProgramPowerConsumption;
	}

	public void setSmartConsumerProgramPowerConsumption(double smartConsumerProgramPowerConsumption) {
		this.smartConsumerProgramPowerConsumption = smartConsumerProgramPowerConsumption;
	}

	/**
	 * Converts this SmartConsumerRequest object into a SmartConsumer entity object.
	 * Only for new SmartConsumers. If you want to update a SmartConsumer, refer directly to the fields of the SmartConsumerRequest.
	 *
	 * @return a new SmartConsumer entity, only for creating new SmartConsumers.
	 */
	public SmartConsumer toEntity() {
		SmartConsumer smartConsumer = new SmartConsumer();
		smartConsumer.setName(name);
		smartConsumer.setPowerConsumption(powerConsumption);
		return smartConsumer;
	}
}
