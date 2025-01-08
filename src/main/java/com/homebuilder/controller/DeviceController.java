package com.homebuilder.controller;

import com.homebuilder.dto.DeviceResponse;
import com.homebuilder.entity.Device;
import com.homebuilder.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

	@GetMapping("/admin")
	public ResponseEntity<List<Device>> getAllDevices() {
		return ResponseEntity.ok(deviceService.getAllDevices());
	}

	@GetMapping("/admin/{deviceId}")
	public ResponseEntity<Device> getDeviceById(@PathVariable Long deviceId) {
		return ResponseEntity.ok(deviceService.getDeviceById(deviceId));
	}
}
