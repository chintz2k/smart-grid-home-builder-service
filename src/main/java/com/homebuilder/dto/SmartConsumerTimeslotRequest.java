package com.homebuilder.dto;

import com.homebuilder.entity.SmartConsumerTimeslot;
import com.homebuilder.entity.SmartConsumerTimeslotStatusCodes;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

/**
 * @author André Heinen
 */
public class SmartConsumerTimeslotRequest {

	private Long id;

	@NotNull(message = "startTime is required")
	@Future(message = "startTime must be in the future")
	private Instant startTime;

	private SmartConsumerTimeslotStatusCodes status;

	@NotNull(message = "SmartConsumer ID is required")
	private Long smartConsumerId;

	@NotNull(message = "SmartConsumerProgram ID is required")
	private Long smartConsumerProgramId;

	private boolean archived = false;

	public SmartConsumerTimeslotRequest() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Instant getStartTime() {
		return startTime;
	}

	public void setStartTime(Instant startTime) {
		this.startTime = startTime;
	}

	public SmartConsumerTimeslotStatusCodes getStatus() {
		return status;
	}

	public void setStatus(SmartConsumerTimeslotStatusCodes status) {
		this.status = status;
	}

	public Long getSmartConsumerId() {
		return smartConsumerId;
	}

	public void setSmartConsumerId(Long smartConsumerId) {
		this.smartConsumerId = smartConsumerId;
	}

	public Long getSmartConsumerProgramId() {
		return smartConsumerProgramId;
	}

	public void setSmartConsumerProgramId(Long smartConsumerProgramId) {
		this.smartConsumerProgramId = smartConsumerProgramId;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public SmartConsumerTimeslot toEntity() {
		SmartConsumerTimeslot smartConsumerTimeslot = new SmartConsumerTimeslot();
		smartConsumerTimeslot.setStartTime(startTime);
		smartConsumerTimeslot.setArchived(archived);
		return smartConsumerTimeslot;
	}
}
