package com.homebuilder.controller;

import com.homebuilder.dto.SmartConsumerRequest;
import com.homebuilder.dto.SmartConsumerResponse;
import com.homebuilder.entity.SmartConsumer;
import com.homebuilder.service.SmartConsumerService;
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
@RequestMapping("/api/smartconsumers")
public class SmartConsumerController {

	private final SmartConsumerService smartConsumerService;

	@Autowired
	public SmartConsumerController(SmartConsumerService smartConsumerService) {
		this.smartConsumerService = smartConsumerService;
	}

	@PostMapping
	public ResponseEntity<SmartConsumerResponse> createSmartConsumerForUser(@Valid @RequestBody SmartConsumerRequest request) {
		SmartConsumer smartConsumer = smartConsumerService.createSmartConsumerForUser(request);
		SmartConsumerResponse dto = new SmartConsumerResponse(smartConsumer);
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@GetMapping
	public ResponseEntity<List<SmartConsumerResponse>> getAllSmartConsumersFromUser() {
		List<SmartConsumer> smartConsumerList = smartConsumerService.getAllSmartConsumersFromUser();
		List<SmartConsumerResponse> dtoList = smartConsumerList.stream().map(SmartConsumerResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}

	@GetMapping("/{smartConsumerId}")
	public ResponseEntity<SmartConsumerResponse> getSmartConsumerByIdFromUser(@PathVariable Long smartConsumerId) {
		SmartConsumer smartConsumer = smartConsumerService.getSmartConsumerByIdFromUser(smartConsumerId);
		SmartConsumerResponse dto = new SmartConsumerResponse(smartConsumer);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/{smartConsumerId}")
	public ResponseEntity<SmartConsumerResponse> updateSmartConsumerForUser(@PathVariable Long smartConsumerId, @Valid @RequestBody SmartConsumerRequest request) {
		SmartConsumer smartConsumer = smartConsumerService.updateSmartConsumerForUser(smartConsumerId, request);
		SmartConsumerResponse dto = new SmartConsumerResponse(smartConsumer);
		return ResponseEntity.ok(dto);
	}

	@DeleteMapping("/{smartConsumerId}")
	public ResponseEntity<Map<String, String>> deleteSmartConsumerForUser(@PathVariable Long smartConsumerId) {
		Map<String, String> success = smartConsumerService.deleteSmartConsumerForUser(smartConsumerId);
		return ResponseEntity.ok().body(success);
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<SmartConsumer>> getAllSmartConsumers() {
		List<SmartConsumer> smartConsumerList = smartConsumerService.getAllSmartConsumers();
		return ResponseEntity.ok(smartConsumerList);
	}

	@GetMapping("/admin/{smartConsumerId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<SmartConsumer> getSmartConsumerById(@PathVariable Long smartConsumerId) {
		SmartConsumer smartConsumer = smartConsumerService.getSmartConsumerById(smartConsumerId);
		return ResponseEntity.ok(smartConsumer);
	}
}
