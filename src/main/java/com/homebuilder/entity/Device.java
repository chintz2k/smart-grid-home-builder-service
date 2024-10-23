package com.homebuilder.entity;

import jakarta.persistence.*;

/**
 * @author André Heinen
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Device {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private Long ownerId;

	public Device() {

	}

	public Device(String name, Long ownerId) {
		this.name = name;
		this.ownerId = ownerId;
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
}
