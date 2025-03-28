package com.homebuilder.controller;

import com.homebuilder.dto.SmartConsumerRequest;
import com.homebuilder.dto.SmartConsumerResponse;
import com.homebuilder.entity.SmartConsumer;
import com.homebuilder.service.SmartConsumerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
	public ResponseEntity<SmartConsumerResponse> createSmartConsumer(@Valid @RequestBody SmartConsumerRequest request) {
		SmartConsumer smartConsumer = smartConsumerService.createSmartConsumer(request);
		SmartConsumerResponse dto = new SmartConsumerResponse(smartConsumer);
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@GetMapping
	public ResponseEntity<List<SmartConsumerResponse>> getAllUnarchivedSmartConsumers() {
		List<SmartConsumer> smartConsumerList = smartConsumerService.getAllUnarchivedSmartConsumers();
		List<SmartConsumerResponse> dtoList = smartConsumerList.stream().map(SmartConsumerResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}

	@GetMapping("/list/self")
	public ResponseEntity<Page<SmartConsumerResponse>> getAllSmartConsumersBySelf(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size
	) {
		Page<SmartConsumerResponse> pages = smartConsumerService.getAllUnarchivedByUser(PageRequest.of(page, size));
		return ResponseEntity.ok(pages);
	}

	@GetMapping("/all")
	public ResponseEntity<List<SmartConsumerResponse>> getAllSmartConsumers() {
		List<SmartConsumer> smartConsumerList = smartConsumerService.getAllSmartConsumers();
		List<SmartConsumerResponse> dtoList = smartConsumerList.stream().map(SmartConsumerResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}

	@GetMapping("/{smartConsumerId}")
	public ResponseEntity<SmartConsumerResponse> getSmartConsumerById(@PathVariable Long smartConsumerId) {
		SmartConsumer smartConsumer = smartConsumerService.getSmartConsumerById(smartConsumerId);
		SmartConsumerResponse dto = new SmartConsumerResponse(smartConsumer);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/update")
	public ResponseEntity<SmartConsumerResponse> updateSmartConsumer(@Valid @RequestBody SmartConsumerRequest request) {
		SmartConsumer smartConsumer = smartConsumerService.updateSmartConsumer(request);
		SmartConsumerResponse dto = new SmartConsumerResponse(smartConsumer);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/{smartConsumerId}/archive")
	public ResponseEntity<Map<String, String>> archiveSmartConsumer(@PathVariable Long smartConsumerId) {
		Map<String, String> success = smartConsumerService.archiveSmartConsumer(smartConsumerId);
		return ResponseEntity.ok().body(success);
	}

	@DeleteMapping("/{smartConsumerId}")
	public ResponseEntity<Map<String, String>> deleteSmartConsumer(@PathVariable Long smartConsumerId) {
		Map<String, String> success = smartConsumerService.deleteSmartConsumer(smartConsumerId);
		return ResponseEntity.ok().body(success);
	}

	@GetMapping("/owner")
	@PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
	public ResponseEntity<List<SmartConsumerResponse>> getAllSmartConsumersByOwner(@RequestParam Long ownerId) {
		List<SmartConsumer> smartConsumerList = smartConsumerService.getAllSmartConsumersByOwner(ownerId);
		List<SmartConsumerResponse> dtoList = smartConsumerList.stream().map(SmartConsumerResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}
}
