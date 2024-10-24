package com.homebuilder.controller;

import com.homebuilder.entity.Device;
import com.homebuilder.exception.InvalidJwtException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.security.JwtUtil;
import com.homebuilder.service.DeviceService;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

	private final JwtUtil jwtUtil;

	@Autowired
	public DeviceController(DeviceService deviceService, JwtUtil jwtUtil) {
		this.deviceService = deviceService;
		this.jwtUtil = jwtUtil;
	}

	// CRUD-Endpoints für SH-Nutzer
	@GetMapping
	public ResponseEntity<List<Device>> getAllDevicesFromUser(Principal principal) {
		if (principal != null) {
			try {
				String token = ((UsernamePasswordAuthenticationToken) principal).getCredentials().toString();
				Long userId = jwtUtil.extractUserId(token);
				return ResponseEntity.ok(deviceService.getAllDevicesFromUser(userId));
			} catch (SignatureException ex) {
				throw new InvalidJwtException("Invalid JWT token signature");
			} catch (Exception ex) {
				throw new UnauthorizedAccessException("Unauthorized access to consumers for user");
			}
		} else {
			throw new UnauthorizedAccessException("Unauthorized access to consumers for user");
		}
	}

	// CRUD-Endpoints für administrative Aufgaben
	@GetMapping("/admin/")
	public ResponseEntity<List<Device>> getAllDevices() {
		return ResponseEntity.ok(deviceService.getAllDevices());
	}
}
