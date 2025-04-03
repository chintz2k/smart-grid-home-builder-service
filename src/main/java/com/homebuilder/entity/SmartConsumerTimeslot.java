package com.homebuilder.entity;

import jakarta.persistence.*;

import java.time.Instant;

/**
 * @author André Heinen
 */
@Entity
public class SmartConsumerTimeslot {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Instant startTime;
	private Instant endTime;
	private Instant cancelledAt;

	@Enumerated(EnumType.STRING)
	private SmartConsumerTimeslotStatusCodes status;

	@ManyToOne
	private SmartConsumer smartConsumer;

	@ManyToOne
	private SmartConsumerProgram smartConsumerProgram;

	private Long userId;

	private boolean archived = false;

	private Long energyTrackerId = null;

	public SmartConsumerTimeslot() {
		this.status = SmartConsumerTimeslotStatusCodes.FRESH;
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

	public Instant getEndTime() {
		return endTime;
	}

	public void setEndTime(Instant endTime) {
		this.endTime = endTime;
	}

	public Instant getCancelledAt() {
		return cancelledAt;
	}

	public void setCancelledAt(Instant cancelledAt) {
		this.cancelledAt = cancelledAt;
	}

	public SmartConsumerTimeslotStatusCodes getStatus() {
		return status;
	}

	public void setStatus(SmartConsumerTimeslotStatusCodes status) {
		this.status = status;
	}

	public SmartConsumer getSmartConsumer() {
		return smartConsumer;
	}

	public void setSmartConsumer(SmartConsumer smartConsumer) {
		this.smartConsumer = smartConsumer;
	}

	public SmartConsumerProgram getSmartConsumerProgram() {
		return smartConsumerProgram;
	}

	public void setSmartConsumerProgram(SmartConsumerProgram smartConsumerProgram) {
		this.smartConsumerProgram = smartConsumerProgram;
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

	public Long getEnergyTrackerId() {
		return energyTrackerId;
	}

	public void setEnergyTrackerId(Long energyTrackerId) {
		this.energyTrackerId = energyTrackerId;
	}
}
