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

	private boolean active = false;

	private boolean archived = false;

	@ManyToOne
	@JoinColumn(name = "room_id", nullable = true)
	private Room room;

	public Device() {

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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

}
