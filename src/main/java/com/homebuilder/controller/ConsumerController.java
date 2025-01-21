package com.homebuilder.controller;

import com.homebuilder.dto.ConsumerRequest;
import com.homebuilder.dto.ConsumerResponse;
import com.homebuilder.entity.Consumer;
import com.homebuilder.service.ConsumerService;
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
@RequestMapping("/api/consumers")
public class ConsumerController {

	private final ConsumerService consumerService;

	@Autowired
	public ConsumerController(ConsumerService consumerService) {
		this.consumerService = consumerService;
	}

	@PostMapping
	public ResponseEntity<ConsumerResponse> createConsumerForUser(@Valid @RequestBody ConsumerRequest request) {
		Consumer consumer = consumerService.createConsumerForUser(request);
		ConsumerResponse dto = new ConsumerResponse(consumer);
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@GetMapping
	public ResponseEntity<List<ConsumerResponse>> getAllConsumersFromUser() {
		List<Consumer> consumerList = consumerService.getAllConsumersFromUser();
		List<ConsumerResponse> dtoList = consumerList.stream().map(ConsumerResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}

	@GetMapping("/{consumerId}")
	public ResponseEntity<ConsumerResponse> getConsumerByIdFromUser(@PathVariable Long consumerId) {
		Consumer consumer = consumerService.getConsumerByIdFromUser(consumerId);
		ConsumerResponse dto = new ConsumerResponse(consumer);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/{consumerId}")
	public ResponseEntity<ConsumerResponse> updateConsumerForUser(@PathVariable Long consumerId, @Valid @RequestBody ConsumerRequest request) {
		Consumer consumer = consumerService.updateConsumerForUser(consumerId, request);
		ConsumerResponse dto = new ConsumerResponse(consumer);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/{consumerId}/archive")
	public ResponseEntity<Map<String, String>> archiveConsumerForUser(@PathVariable Long consumerId) {
		Map<String, String> success = consumerService.archiveConsumerForUser(consumerId);
		return ResponseEntity.ok().body(success);
	}

	@DeleteMapping("/{consumerId}")
	public ResponseEntity<Map<String, String>> deleteConsumerForUser(@PathVariable Long consumerId) {
		Map<String, String> success = consumerService.deleteConsumerForUser(consumerId);
		return ResponseEntity.ok().body(success);
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<Consumer>> getAllConsumers() {
		List<Consumer> consumerList = consumerService.getAllConsumers();
		return ResponseEntity.ok(consumerList);
	}

	@GetMapping("/admin/{consumerId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Consumer> getConsumerById(@PathVariable Long consumerId) {
		Consumer consumer = consumerService.getConsumerById(consumerId);
		return ResponseEntity.ok(consumer);
	}
}
