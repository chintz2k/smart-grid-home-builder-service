package com.homebuilder.dto;

import com.homebuilder.entity.SmartConsumerTimeslot;
import com.homebuilder.entity.SmartConsumerTimeslotStatusCodes;

import java.time.Instant;

/**
 * @author André Heinen
 */
public class SmartConsumerTimeslotResponse {

	private final Long id;
	private final Instant startTime;
	private final Instant endTime;
	private final Instant cancelledAt;
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

	public Instant getStartTime() {
		return startTime;
	}

	public Instant getEndTime() {
		return endTime;
	}

	public Instant getCancelledAt() {
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
