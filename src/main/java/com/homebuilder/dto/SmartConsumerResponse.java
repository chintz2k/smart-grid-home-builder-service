package com.homebuilder.dto;

import com.homebuilder.entity.SmartConsumer;
import com.homebuilder.entity.SmartConsumerProgram;
import com.homebuilder.entity.SmartConsumerTimeslot;
import com.homebuilder.exception.DeviceNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author André Heinen
 */
public class SmartConsumerResponse {

	private final Long id;
	private final String name;
	private final Long ownerId;
	private final boolean active;
	private final boolean archived;
	private final double powerConsumption;
	private final List<Long> smartConsumerPrograms = new ArrayList<>();
	private final List<Long> timeslots = new ArrayList<>();

	public SmartConsumerResponse(SmartConsumer smartConsumer) {
		if (smartConsumer == null) {
			throw new DeviceNotFoundException("SmartConsumer not found");
		}
		this.id = smartConsumer.getId();
		this.name = smartConsumer.getName();
		this.ownerId = smartConsumer.getUserId();
		this.active = smartConsumer.isActive();
		this.archived = smartConsumer.isArchived();
		this.powerConsumption = smartConsumer.getPowerConsumption();
		for (SmartConsumerProgram program : smartConsumer.getProgramList()) {
			smartConsumerPrograms.add(program.getId());
		}
		for (SmartConsumerTimeslot timeslot : smartConsumer.getTimeslotList()) {
			timeslots.add(timeslot.getId());
		}
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public double getPowerConsumption() {
		return powerConsumption;
	}

	public boolean isActive() {
		return active;
	}

	public boolean isArchived() {
		return archived;
	}

	public List<Long> getSmartConsumerPrograms() {
		return smartConsumerPrograms;
	}

	public List<Long> getTimeslots() {
		return timeslots;
	}
}
