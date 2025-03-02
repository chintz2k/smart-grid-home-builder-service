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
	public ResponseEntity<ProducerResponse> createProducer(@Valid @RequestBody ProducerRequest request) {
		Producer producer = producerService.createProducer(request);
		ProducerResponse dto = new ProducerResponse(producer);
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@GetMapping
	public ResponseEntity<List<ProducerResponse>> getAllProducers() {
		List<Producer> producerList = producerService.getAllProducers();
		List<ProducerResponse> dtoList = producerList.stream().map(ProducerResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}

	@GetMapping("/{producerId}")
	public ResponseEntity<ProducerResponse> getProducerById(@PathVariable Long producerId) {
		Producer producer = producerService.getProducerById(producerId);
		ProducerResponse dto = new ProducerResponse(producer);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/update")
	public ResponseEntity<ProducerResponse> updateProducer(@Valid @RequestBody ProducerRequest request) {
		Producer producer = producerService.updateProducer(request);
		ProducerResponse dto = new ProducerResponse(producer);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/{producerId}/archive")
	public ResponseEntity<Map<String, String>> archiveProducer(@PathVariable Long producerId) {
		Map<String, String> success = producerService.archiveProducer(producerId);
		return ResponseEntity.ok().body(success);
	}

	@DeleteMapping("/{producerId}")
	public ResponseEntity<Map<String, String>> deleteProducer(@PathVariable Long producerId) {
		Map<String, String> success = producerService.deleteProducer(producerId);
		return ResponseEntity.ok().body(success);
	}

	@GetMapping("/owner")
	@PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
	public ResponseEntity<List<ProducerResponse>> getAllProducersByOwner(@RequestParam Long ownerId) {
		List<Producer> producerList = producerService.getAllProducersByOwner(ownerId);
		List<ProducerResponse> dtoList = producerList.stream().map(ProducerResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}
}
