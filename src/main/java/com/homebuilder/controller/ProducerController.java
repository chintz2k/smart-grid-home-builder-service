package com.homebuilder.controller;

import com.homebuilder.dto.ProducerRequest;
import com.homebuilder.dto.ProducerResponse;
import com.homebuilder.entity.Producer;
import com.homebuilder.service.ProducerService;
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
import java.util.Set;

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

	@PostMapping("/createlist")
	@PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
	public ResponseEntity<List<ProducerResponse>> createProducerList(@RequestBody List<@Valid ProducerRequest> request) {
		List<Producer> producerList = producerService.createProducerList(request);
		List<ProducerResponse> dtos = new ArrayList<>();
		producerList.forEach(producer -> dtos.add(new ProducerResponse(producer)));
		return ResponseEntity.status(HttpStatus.CREATED).body(dtos);
	}

	@GetMapping
	public ResponseEntity<List<ProducerResponse>> getAllUnarchivedProducers() {
		List<Producer> producerList = producerService.getAllUnarchivedProducers();
		List<ProducerResponse> dtoList = producerList.stream().map(ProducerResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}

	@GetMapping("/list/self/noroom")
	public ResponseEntity<Page<ProducerResponse>> getAllProducersBySelfAndNoRoom(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size
	) {
		Page<ProducerResponse> pages = producerService.getAllProducersByOwnerAndRoomIsNull(PageRequest.of(page, size));
		return ResponseEntity.ok(pages);
	}

	@GetMapping("/list/self/room/{roomId}")
	public ResponseEntity<Page<ProducerResponse>> getAllProducersBySelfAndRoom(
			@PathVariable Long roomId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size
	) {
		Page<ProducerResponse> pages = producerService.getAllProducersByOwnerAndRoomId(roomId, PageRequest.of(page, size));
		return ResponseEntity.ok(pages);
	}

	@GetMapping("/list/self")
	public ResponseEntity<Page<ProducerResponse>> getAllProducersBySelf(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size
	) {
		Page<ProducerResponse> pages = producerService.getAllUnarchivedByUser(PageRequest.of(page, size));
		return ResponseEntity.ok(pages);
	}

	@GetMapping("/all")
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

	@PutMapping("/setActiveByListNoSendEvent")
	public ResponseEntity<Map<String, String>> setActiveByListAndNoSendEvent(@RequestBody Set<Long> idSet, @RequestParam boolean active) {
		Map<String, String> map = producerService.setActiveByListAndNoSendEvent(idSet, active);
		return ResponseEntity.ok().body(map);
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
