package com.homebuilder.dto;

import com.homebuilder.entity.SmartConsumerTimeslot;
import com.homebuilder.entity.SmartConsumerTimeslotStatusCodes;

import java.time.LocalDateTime;

/**
 * @author André Heinen
 */
public class SmartConsumerTimeslotResponse {

	private final Long id;
	private final LocalDateTime startTime;
	private final LocalDateTime endTime;
	private final LocalDateTime cancelledAt;
	private final SmartConsumerTimeslotStatusCodes status;
	private final Long smartConsumerId;
	private final boolean archived;

	public SmartConsumerTimeslotResponse(SmartConsumerTimeslot smartConsumerTimeslot) {
		this.id = smartConsumerTimeslot.getId();
		this.startTime = smartConsumerTimeslot.getStartTime();
		this.endTime = smartConsumerTimeslot.getEndTime();
		this.cancelledAt = smartConsumerTimeslot.getCancelledAt();
		this.status = smartConsumerTimeslot.getStatus();
		this.smartConsumerId = smartConsumerTimeslot.getSmartConsumer().getId();
		this.archived = smartConsumerTimeslot.isArchived();
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public LocalDateTime getCancelledAt() {
		return cancelledAt;
	}

	public SmartConsumerTimeslotStatusCodes getStatus() {
		return status;
	}

	public Long getSmartConsumerId() {
		return smartConsumerId;
	}

	public boolean isArchived() {
		return archived;
	}
}
