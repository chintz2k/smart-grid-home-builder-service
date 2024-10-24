package com.homebuilder.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * @author André Heinen
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Device {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "name is required")
	private String name;

	private Long userId;

	public Device() {

	}

	public Device(String name, Long userId) {
		this.name = name;
		this.userId = userId;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
