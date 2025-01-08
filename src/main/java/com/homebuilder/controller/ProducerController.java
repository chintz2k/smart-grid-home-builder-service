package com.homebuilder.controller;

import com.homebuilder.dto.ProducerRequest;
import com.homebuilder.dto.ProducerResponse;
import com.homebuilder.entity.Producer;
import com.homebuilder.service.ProducerService;
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
@RequestMapping("/api/producers")
public class ProducerController {

	private final ProducerService producerService;

	@Autowired
	public ProducerController(ProducerService producerService) {
		this.producerService = producerService;
	}

	@PostMapping
	public ResponseEntity<ProducerResponse> createProducerForUser(@Valid @RequestBody ProducerRequest request) {
		Producer producer = producerService.createProducerForUser(request);
		ProducerResponse dto = new ProducerResponse(producer);
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@GetMapping
	public ResponseEntity<List<ProducerResponse>> getAllProducersFromUser() {
		List<Producer> producerList = producerService.getAllProducersFromUser();
		List<ProducerResponse> dtoList = producerList.stream().map(ProducerResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}

	@GetMapping("/{producerId}")
	public ResponseEntity<ProducerResponse> getProducerByIdFromUser(@PathVariable Long producerId) {
		Producer producer = producerService.getProducerByIdFromUser(producerId);
		ProducerResponse dto = new ProducerResponse(producer);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/{producerId}")
	public ResponseEntity<ProducerResponse> updateProducerForUser(@PathVariable Long producerId, @Valid @RequestBody ProducerRequest request) {
		Producer producer = producerService.updateProducerForUser(producerId, request);
		ProducerResponse dto = new ProducerResponse(producer);
		return ResponseEntity.ok(dto);
	}

	@DeleteMapping("/{producerId}")
	public ResponseEntity<Map<String, String>> deleteProducerForUser(@PathVariable Long producerId) {
		Map<String, String> success = producerService.deleteProducerForUser(producerId);
		return ResponseEntity.ok().body(success);
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<Producer>> getAllProducers() {
		List<Producer> producerList = producerService.getAllProducers();
		return ResponseEntity.ok(producerList);
	}

	@GetMapping("/admin/{producerId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Producer> getProducerById(@PathVariable Long producerId) {
		Producer producer = producerService.getProducerById(producerId);
		return ResponseEntity.ok(producer);
	}
}
