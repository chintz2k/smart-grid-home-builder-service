package com.homebuilder.controller;

import com.homebuilder.dto.ConsumerRequest;
import com.homebuilder.dto.ConsumerResponse;
import com.homebuilder.entity.Consumer;
import com.homebuilder.service.ConsumerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
	public ResponseEntity<ConsumerResponse> createConsumer(@Valid @RequestBody ConsumerRequest request) {
		Consumer consumer = consumerService.createConsumer(request);
		ConsumerResponse dto = new ConsumerResponse(consumer);
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@PostMapping("/createlist")
	@PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
	public ResponseEntity<List<ConsumerResponse>> createConsumerList(@RequestBody List<@Valid ConsumerRequest> request) {
		List<Consumer> consumerList = consumerService.createConsumerList(request);
		List<ConsumerResponse> dtos = new ArrayList<>();
		consumerList.forEach(consumer -> dtos.add(new ConsumerResponse(consumer)));
		return ResponseEntity.status(HttpStatus.CREATED).body(dtos);
	}

	@GetMapping
	public ResponseEntity<List<ConsumerResponse>> getAllUnarchivedConsumers() {
		List<Consumer> consumerList = consumerService.getAllUnarchivedConsumers();
		List<ConsumerResponse> dtoList = consumerList.stream().map(ConsumerResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}

	@GetMapping("/list/self/noroom")
	public ResponseEntity<Page<ConsumerResponse>> getAllConsumersBySelfAndNoRoom(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size
	) {
		Page<ConsumerResponse> pages = consumerService.getAllConsumersByOwnerAndRoomIsNull(PageRequest.of(page, size));
		return ResponseEntity.ok(pages);
	}

	@GetMapping("/list/self")
	public ResponseEntity<Page<ConsumerResponse>> getAllConsumersBySelf(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size
	) {
		Page<ConsumerResponse> pages = consumerService.getAllUnarchivedByUser(PageRequest.of(page, size));
		return ResponseEntity.ok(pages);
	}

	@GetMapping("/all")
	public ResponseEntity<List<ConsumerResponse>> getAllConsumers() {
		List<Consumer> consumerList = consumerService.getAllConsumers();
		List<ConsumerResponse> dtoList = consumerList.stream().map(ConsumerResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}

	@GetMapping("/{consumerId}")
	public ResponseEntity<ConsumerResponse> getConsumerById(@PathVariable Long consumerId) {
		Consumer consumer = consumerService.getConsumerById(consumerId);
		ConsumerResponse dto = new ConsumerResponse(consumer);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/update")
	public ResponseEntity<ConsumerResponse> updateConsumer(@Valid @RequestBody ConsumerRequest request) {
		Consumer consumer = consumerService.updateConsumer(request);
		ConsumerResponse dto = new ConsumerResponse(consumer);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/{consumerId}/archive")
	public ResponseEntity<Map<String, String>> archiveConsumer(@PathVariable Long consumerId) {
		Map<String, String> success = consumerService.archiveConsumer(consumerId);
		return ResponseEntity.ok().body(success);
	}

	@DeleteMapping("/{consumerId}")
	public ResponseEntity<Map<String, String>> deleteConsumer(@PathVariable Long consumerId) {
		Map<String, String> success = consumerService.deleteConsumer(consumerId);
		return ResponseEntity.ok().body(success);
	}

	@GetMapping("/owner")
	@PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
	public ResponseEntity<List<ConsumerResponse>> getAllConsumersByOwner(@RequestParam Long ownerId) {
		List<Consumer> consumerList = consumerService.getAllConsumersByOwner(ownerId);
		List<ConsumerResponse> dtoList = consumerList.stream().map(ConsumerResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}
}
