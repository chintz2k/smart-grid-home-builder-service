package com.homebuilder.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author André Heinen
 */
@Entity
public class Room {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private Long userId;

	@OneToMany(mappedBy = "room", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Device> devices = new ArrayList<>();

	public Room() {

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

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	public void addDevice(Device device) {
		devices.add(device);
		device.setRoom(this);
	}

	public void removeDevice(Device device) {
		devices.remove(device);
		device.setRoom(null);
	}
}
