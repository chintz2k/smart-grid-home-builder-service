package com.homebuilder.controller;

import com.homebuilder.entity.Device;
import com.homebuilder.security.JwtUtil;
import com.homebuilder.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

/**
 * @author André Heinen
 */
@RestController
@RequestMapping("/api/devices")
public class DeviceController {

	private final DeviceService deviceService;

	@Autowired // TODO DEBUG
	private JwtUtil util;

	@Autowired
	public DeviceController(DeviceService deviceService) {
		this.deviceService = deviceService;
	}

	// Abrufen aller Geräte eines SH-Nutzers
	@GetMapping
	public ResponseEntity<List<Device>> getAllDevicesForUser(Principal principal) {
		return ResponseEntity.ok(deviceService.getAllDevicesForUser(Long.valueOf(principal.getName())));
	}

	// Nur Admins: Abrufen aller Geräte im System
	@GetMapping("/admin/all")
	public ResponseEntity<List<Device>> getAllDevices() {
		return ResponseEntity.ok(deviceService.getAllDevices());
	}
}
