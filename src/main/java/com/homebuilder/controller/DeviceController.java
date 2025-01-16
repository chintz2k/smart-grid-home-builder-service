package com.homebuilder.controller;

import com.homebuilder.dto.DeviceResponse;
import com.homebuilder.entity.Device;
import com.homebuilder.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
@RestController
@RequestMapping("/api/devices")
public class DeviceController {

	private final DeviceService deviceService;

	@Autowired
	public DeviceController(DeviceService deviceService) {
		this.deviceService = deviceService;
	}

	@GetMapping
	public ResponseEntity<List<DeviceResponse>> getAllDevicesFromUser() {
		List<Device> deviceList = deviceService.getAllDevicesFromUser();
		List<DeviceResponse> dtoList = deviceList.stream().map(DeviceResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}

	@GetMapping("/{deviceId}")
	public ResponseEntity<DeviceResponse> getDeviceByIdFromUser(@PathVariable Long deviceId) {
		Device device = deviceService.getDeviceByIdFromUser(deviceId);
		DeviceResponse dto = new DeviceResponse(device);
		return ResponseEntity.ok(dto);
	}

	@PostMapping("/{deviceId}/toggle")
	public ResponseEntity<Map<String, String>> toggleDeviceOnOffForUser(@PathVariable Long deviceId, @RequestParam boolean active) {
		Map<String, String> success = deviceService.toggleDeviceOnOffForUser(deviceId, active);
		return ResponseEntity.ok().body(success);
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<Device>> getAllDevices() {
		List<Device> deviceList = deviceService.getAllDevices();
		return ResponseEntity.ok(deviceList);
	}

	@GetMapping("/admin/{deviceId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Device> getDeviceById(@PathVariable Long deviceId) {
		Device device = deviceService.getDeviceById(deviceId);
		return ResponseEntity.ok(device);
	}

	@PostMapping("/system/{deviceId}/toggle")
	@PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
	public ResponseEntity<Map<String, String>> toggleDeviceOnOff(@PathVariable Long deviceId, @RequestParam boolean active) {
		Map<String, String> success = deviceService.toggleDeviceOnOff(deviceId, active);
		return ResponseEntity.ok().body(success);
	}
}
