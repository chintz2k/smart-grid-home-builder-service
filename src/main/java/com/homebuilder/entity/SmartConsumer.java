package com.homebuilder.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * @author André Heinen
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
public class SmartConsumer extends Consumer {

	@OneToMany(mappedBy = "smartConsumer", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SmartConsumerProgram> programList;

	@OneToMany(mappedBy = "smartConsumer", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SmartConsumerTimeslot> timeslotList;

	public SmartConsumer() {
		this.programList = new ArrayList<>();
		this.timeslotList = new ArrayList<>();
	}

	public List<SmartConsumerProgram> getProgramList() {
		return programList;
	}

	public void setProgramList(List<SmartConsumerProgram> programList) {
		this.programList = programList;
	}

	public List<SmartConsumerTimeslot> getTimeslotList() {
		return timeslotList;
	}

	public void setTimeslotList(List<SmartConsumerTimeslot> timeslotList) {
		this.timeslotList = timeslotList;
	}
}
