package com.homebuilder.dto;

import com.homebuilder.entity.SmartConsumerTimeslot;
import com.homebuilder.entity.SmartConsumerTimeslotStatusCodes;
import com.homebuilder.exception.DeviceNotFoundException;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author André Heinen
 */
public class SmartConsumerTimeslotResponse {

	private final Long id;
	private final Long ownerId;
	private final boolean archived;
	private final Long smartConsumerId;
	private final ZonedDateTime startTime;
	private final ZonedDateTime endTime;
	private final ZonedDateTime cancelledAt;
	private final SmartConsumerTimeslotStatusCodes status;

	public SmartConsumerTimeslotResponse(SmartConsumerTimeslot smartConsumerTimeslot, String timeZone) {
		if (smartConsumerTimeslot == null) {
			throw new DeviceNotFoundException("SmartConsumerTimeslot not found");
		}
		this.id = smartConsumerTimeslot.getId();
		this.ownerId = smartConsumerTimeslot.getUserId();
		this.archived = smartConsumerTimeslot.isArchived();
		this.smartConsumerId = smartConsumerTimeslot.getSmartConsumer().getId();
		this.startTime = convertInstantToZonedDateTime(smartConsumerTimeslot.getStartTime(), ZoneId.of(timeZone));
		this.endTime = convertInstantToZonedDateTime(smartConsumerTimeslot.getEndTime(), ZoneId.of(timeZone));
		this.cancelledAt = convertInstantToZonedDateTime(smartConsumerTimeslot.getCancelledAt(), ZoneId.of(timeZone));
		this.status = smartConsumerTimeslot.getStatus();
	}

	public Long getId() {
		return id;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public boolean isArchived() {
		return archived;
	}

	public Long getSmartConsumerId() {
		return smartConsumerId;
	}

	public ZonedDateTime getStartTime() {
		return startTime;
	}

	public ZonedDateTime getEndTime() {
		return endTime;
	}

	public ZonedDateTime getCancelledAt() {
		return cancelledAt;
	}

	public SmartConsumerTimeslotStatusCodes getStatus() {
		return status;
	}

	private ZonedDateTime convertInstantToZonedDateTime(Instant instant, ZoneId zoneId) {
		if (instant == null) {
			return null;
		}
		return ZonedDateTime.ofInstant(instant, zoneId);
	}
}
