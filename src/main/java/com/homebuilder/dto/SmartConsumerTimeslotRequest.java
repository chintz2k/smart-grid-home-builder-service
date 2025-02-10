package com.homebuilder.dto;

import com.homebuilder.entity.SmartConsumerTimeslot;
import com.homebuilder.entity.SmartConsumerTimeslotStatusCodes;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author André Heinen
 */
public class SmartConsumerTimeslotRequest {

	private Long id;

	@NotNull(message = "startTime is required")
	@Future(message = "startTime must be in the future")
	private LocalDateTime startTime;

	@NotNull(message = "timeZone is required")
	private String timeZone;

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

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
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

	public Instant getStartTimeAsInstant() {
		if (startTime == null || timeZone == null) {
			throw new IllegalArgumentException("localStartTime and timeZone must not be null");
		}

		ZonedDateTime zonedDateTime = startTime.atZone(ZoneId.of(timeZone));

		return zonedDateTime.toInstant();
	}

	public SmartConsumerTimeslot toEntity() {
		SmartConsumerTimeslot smartConsumerTimeslot = new SmartConsumerTimeslot();
		smartConsumerTimeslot.setStartTime(getStartTimeAsInstant());
		smartConsumerTimeslot.setArchived(archived);
		return smartConsumerTimeslot;
	}
}
