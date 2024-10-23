package com.homebuilder.controller;

import com.homebuilder.entity.Producer;
import com.homebuilder.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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

	// CRUD-Endpoints für SH-Nutzer
	@PostMapping
	public ResponseEntity<Producer> createProducer(@RequestBody Producer producer, Principal principal) {
		return ResponseEntity.status(HttpStatus.CREATED).body(producerService.createProducer(producer, Long.valueOf(principal.getName())));
	}

	@GetMapping
	public ResponseEntity<List<Producer>> getProducersForUser(Principal principal) {
		return ResponseEntity.ok(producerService.getProducersForUser(Long.valueOf(principal.getName())));
	}

	@GetMapping("/{producerId}")
	public ResponseEntity<Producer> getProducerForUser(@PathVariable Long producerId, Principal principal) {
		return ResponseEntity.ok(producerService.getProducerForUser(producerId, Long.valueOf(principal.getName())));
	}

	@PutMapping("/{producerId}")
	public ResponseEntity<Producer> updateProducerForUser(@PathVariable Long producerId, @RequestBody Producer producerDetails, Principal principal) {
		return ResponseEntity.ok(producerService.updateProducerForUser(producerId, Long.valueOf(principal.getName()), producerDetails));
	}

	@DeleteMapping("/{producerId}")
	public ResponseEntity<?> deleteProducerForUser(@PathVariable Long producerId, Principal principal) {
		producerService.deleteProducerForUser(producerId, Long.valueOf(principal.getName()));
		return ResponseEntity.ok().build();
	}

	// CRUD-Endpoints für administrative Aufgaben
	@GetMapping("/admin/all")
	public ResponseEntity<List<Producer>> getAllProducers() {
		return ResponseEntity.ok(producerService.getAllProducers());
	}

	@GetMapping("/admin/{producerId}")
	public ResponseEntity<Producer> getProducerById(@PathVariable Long producerId) {
		return ResponseEntity.ok(producerService.getProducerById(producerId));
	}
}
