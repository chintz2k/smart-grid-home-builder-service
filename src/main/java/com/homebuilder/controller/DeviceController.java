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
	public ResponseEntity<List<DeviceResponse>> getAllUnarchivedDevices() {
		List<Device> deviceList = deviceService.getAllUnarchivedDevices();
		List<DeviceResponse> dtoList = deviceList.stream().map(DeviceResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}

	@GetMapping("/all")
	public ResponseEntity<List<DeviceResponse>> getAllDevices() {
		List<Device> deviceList = deviceService.getAllDevices();
		List<DeviceResponse> dtoList = deviceList.stream().map(DeviceResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}

	@GetMapping("/{deviceId}")
	public ResponseEntity<DeviceResponse> getDeviceById(@PathVariable Long deviceId) {
		Device device = deviceService.getDeviceById(deviceId);
		DeviceResponse dto = new DeviceResponse(device);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/{deviceId}/toggle")
	public ResponseEntity<Map<String, String>> setActive(@PathVariable Long deviceId, @RequestParam boolean active) {
		Map<String, String> success = deviceService.setActive(deviceId, active);
		return ResponseEntity.ok().body(success);
	}

	@DeleteMapping("/delete/{ownerId}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
	public ResponseEntity<List<Map<String, String>>> deleteAllDevicesByOwner(@PathVariable Long ownerId) {
		List<Map<String, String>> success = deviceService.deleteAllDevicesByOwner(ownerId);
		return ResponseEntity.ok().body(success);
	}
}
