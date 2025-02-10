package com.homebuilder.controller;

import com.homebuilder.dto.SmartConsumerTimeslotRequest;
import com.homebuilder.dto.SmartConsumerTimeslotResponse;
import com.homebuilder.entity.SmartConsumerTimeslot;
import com.homebuilder.service.SmartConsumerTimeslotService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
@RestController
@RequestMapping("/api/smartconsumertimeslots")
public class SmartConsumerTimeslotController {

	private final SmartConsumerTimeslotService smartConsumerTimeslotService;

	@Autowired
	public SmartConsumerTimeslotController(SmartConsumerTimeslotService smartConsumerTimeslotService) {
		this.smartConsumerTimeslotService = smartConsumerTimeslotService;
	}

	@PostMapping
	public ResponseEntity<SmartConsumerTimeslotResponse> createSmartConsumerTimeslotForUser(@Valid @RequestBody SmartConsumerTimeslotRequest request, @RequestHeader(value = "Time-Zone", required = false) String timeZone) {
		SmartConsumerTimeslot smartConsumerTimeslot = smartConsumerTimeslotService.createSmartConsumerTimeslotForUser(request);
		SmartConsumerTimeslotResponse dto = new SmartConsumerTimeslotResponse(smartConsumerTimeslot, timeZone);
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@GetMapping
	public ResponseEntity<List<SmartConsumerTimeslotResponse>> getAllSmartConsumerTimeslotsFromUser(@RequestHeader(value = "Time-Zone", required = false) String timeZone) {
		List<SmartConsumerTimeslot> smartConsumerTimeslotList = smartConsumerTimeslotService.getAllSmartConsumerTimeslotsFromUser();
		List<SmartConsumerTimeslotResponse> dtoList = smartConsumerTimeslotList.stream().map(smartConsumerTimeslot -> new SmartConsumerTimeslotResponse(smartConsumerTimeslot, timeZone)).toList();
		return ResponseEntity.ok(dtoList);
	}

	@GetMapping("/{smartConsumerTimeslotId}")
	public ResponseEntity<SmartConsumerTimeslotResponse> getSmartConsumerTimeslotByIdFromUser(@PathVariable Long smartConsumerTimeslotId, @RequestHeader(value = "Time-Zone", required = false) String timeZone) {
		SmartConsumerTimeslot smartConsumerTimeslot = smartConsumerTimeslotService.getSmartConsumerTimeslotByIdFromUser(smartConsumerTimeslotId);
		SmartConsumerTimeslotResponse dto = new SmartConsumerTimeslotResponse(smartConsumerTimeslot, timeZone);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/{smartConsumerTimeslotId}")
	public ResponseEntity<SmartConsumerTimeslotResponse> updateSmartConsumerTimeslotForUser(@PathVariable Long smartConsumerTimeslotId, @Valid @RequestBody SmartConsumerTimeslotRequest request, @RequestHeader(value = "Time-Zone", required = false) String timeZone) {
		SmartConsumerTimeslot smartConsumerTimeslot = smartConsumerTimeslotService.updateSmartConsumerTimeslotForUser(smartConsumerTimeslotId, request);
		SmartConsumerTimeslotResponse dto = new SmartConsumerTimeslotResponse(smartConsumerTimeslot, timeZone);
		return ResponseEntity.ok(dto);
	}

	@DeleteMapping("/{smartConsumerTimeslotId}")
	public ResponseEntity<Map<String, String>> deleteSmartConsumerTimeslotForUser(@PathVariable Long smartConsumerTimeslotId) {
		Map<String, String> success = smartConsumerTimeslotService.deleteSmartConsumerTimeslotForUser(smartConsumerTimeslotId);
		return ResponseEntity.ok().body(success);
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<SmartConsumerTimeslot>> getAllSmartConsumerTimeslots() {
		List<SmartConsumerTimeslot> smartConsumerTimeslotList = smartConsumerTimeslotService.getAllSmartConsumerTimeslots();
		return ResponseEntity.ok(smartConsumerTimeslotList);
	}

	@GetMapping("/admin/{smartConsumerTimeslotId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<SmartConsumerTimeslot> getSmartConsumerTimeslotById(@PathVariable Long smartConsumerTimeslotId) {
		SmartConsumerTimeslot smartConsumerTimeslot = smartConsumerTimeslotService.getSmartConsumerTimeslotById(smartConsumerTimeslotId);
		return ResponseEntity.ok(smartConsumerTimeslot);
	}
}
