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
	public ResponseEntity<SmartConsumerTimeslotResponse> createSmartConsumerTimeslot(@Valid @RequestBody SmartConsumerTimeslotRequest request, @RequestHeader(value = "Time-Zone") String timeZone) {
		SmartConsumerTimeslot smartConsumerTimeslot = smartConsumerTimeslotService.createSmartConsumerTimeslot(request);
		SmartConsumerTimeslotResponse dto = new SmartConsumerTimeslotResponse(smartConsumerTimeslot, timeZone);
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@GetMapping
	public ResponseEntity<List<SmartConsumerTimeslotResponse>> getAllSmartConsumerTimeslots(@RequestHeader(value = "Time-Zone") String timeZone) {
		List<SmartConsumerTimeslot> smartConsumerTimeslotList = smartConsumerTimeslotService.getAllSmartConsumerTimeslots();
		List<SmartConsumerTimeslotResponse> dtoList = smartConsumerTimeslotList.stream().map(smartConsumerTimeslot -> new SmartConsumerTimeslotResponse(smartConsumerTimeslot, timeZone)).toList();
		return ResponseEntity.ok(dtoList);
	}

	@GetMapping("/{smartConsumerTimeslotId}")
	public ResponseEntity<SmartConsumerTimeslotResponse> getSmartConsumerTimeslotById(@PathVariable Long smartConsumerTimeslotId, @RequestHeader(value = "Time-Zone") String timeZone) {
		SmartConsumerTimeslot smartConsumerTimeslot = smartConsumerTimeslotService.getSmartConsumerTimeslotById(smartConsumerTimeslotId);
		SmartConsumerTimeslotResponse dto = new SmartConsumerTimeslotResponse(smartConsumerTimeslot, timeZone);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/update")
	public ResponseEntity<SmartConsumerTimeslotResponse> updateSmartConsumerTimeslot(@Valid @RequestBody SmartConsumerTimeslotRequest request, @RequestHeader(value = "Time-Zone") String timeZone) {
		SmartConsumerTimeslot smartConsumerTimeslot = smartConsumerTimeslotService.updateSmartConsumerTimeslot(request);
		SmartConsumerTimeslotResponse dto = new SmartConsumerTimeslotResponse(smartConsumerTimeslot, timeZone);
		return ResponseEntity.ok(dto);
	}

	@DeleteMapping("/{smartConsumerTimeslotId}")
	public ResponseEntity<Map<String, String>> deleteSmartConsumerTimeslot(@PathVariable Long smartConsumerTimeslotId) {
		Map<String, String> success = smartConsumerTimeslotService.deleteSmartConsumerTimeslot(smartConsumerTimeslotId);
		return ResponseEntity.ok().body(success);
	}

	@GetMapping("/owner")
	@PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
	public ResponseEntity<List<SmartConsumerTimeslotResponse>> getAllSmartConsumerTimeslotsByOwner(@RequestParam Long ownerId, @RequestHeader(value = "Time-Zone") String timeZone) {
		List<SmartConsumerTimeslot> smartConsumerTimeslotList = smartConsumerTimeslotService.getAllSmartConsumerTimeslotsByOwner(ownerId);
		List<SmartConsumerTimeslotResponse> dtoList = smartConsumerTimeslotList.stream().map(smartConsumerTimeslot -> new SmartConsumerTimeslotResponse(smartConsumerTimeslot, timeZone)).toList();
		return ResponseEntity.ok(dtoList);
	}
}
