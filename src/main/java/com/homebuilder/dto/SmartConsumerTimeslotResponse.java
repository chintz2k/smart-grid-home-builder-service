package com.homebuilder.dto;

import com.homebuilder.entity.SmartConsumerTimeslot;
import com.homebuilder.entity.SmartConsumerTimeslotStatusCodes;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

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

	public SmartConsumerTimeslotResponse(SmartConsumerTimeslot smartConsumerTimeslot, String timeZone) {
		this.id = smartConsumerTimeslot.getId();
		this.startTime = convertInstantToLocalDateTime(smartConsumerTimeslot.getStartTime(), ZoneId.of(timeZone));
		this.endTime = convertInstantToLocalDateTime(smartConsumerTimeslot.getEndTime(), ZoneId.of(timeZone));
		this.cancelledAt = convertInstantToLocalDateTime(smartConsumerTimeslot.getCancelledAt(), ZoneId.of(timeZone));
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

	private LocalDateTime convertInstantToLocalDateTime(Instant instant, ZoneId zoneId) {
		if (instant == null) {
			return null;
		}
		return LocalDateTime.ofInstant(instant, zoneId);
	}
}
