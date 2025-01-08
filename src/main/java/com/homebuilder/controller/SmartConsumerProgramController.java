package com.homebuilder.controller;

import com.homebuilder.dto.SmartConsumerProgramRequest;
import com.homebuilder.dto.SmartConsumerProgramResponse;
import com.homebuilder.entity.SmartConsumerProgram;
import com.homebuilder.service.SmartConsumerProgramService;
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
@RequestMapping("/api/smartconsumerprograms")
public class SmartConsumerProgramController {

	private final SmartConsumerProgramService smartConsumerProgramService;

	@Autowired
	public SmartConsumerProgramController(SmartConsumerProgramService smartConsumerProgramService) {
		this.smartConsumerProgramService = smartConsumerProgramService;
	}

	@PostMapping
	public ResponseEntity<SmartConsumerProgramResponse> createSmartConsumerProgramForUser(@Valid @RequestBody SmartConsumerProgramRequest request) {
		SmartConsumerProgram smartConsumerProgram = smartConsumerProgramService.createSmartConsumerProgramForUser(request);
		SmartConsumerProgramResponse dto = new SmartConsumerProgramResponse(smartConsumerProgram);
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@GetMapping
	public ResponseEntity<List<SmartConsumerProgramResponse>> getAllSmartConsumerProgramsFromUser() {
		List<SmartConsumerProgram> smartConsumerProgramList = smartConsumerProgramService.getAllSmartConsumerProgramsFromUser();
		List<SmartConsumerProgramResponse> dtoList = smartConsumerProgramList.stream().map(SmartConsumerProgramResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}

	@GetMapping("/{smartConsumerProgramId}")
	public ResponseEntity<SmartConsumerProgramResponse> getSmartConsumerProgramByIdFromUser(@PathVariable Long smartConsumerProgramId) {
		SmartConsumerProgram smartConsumerProgram = smartConsumerProgramService.getSmartConsumerProgramByIdFromUser(smartConsumerProgramId);
		SmartConsumerProgramResponse dto = new SmartConsumerProgramResponse(smartConsumerProgram);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/{smartConsumerProgramId}")
	public ResponseEntity<SmartConsumerProgramResponse> updateSmartConsumerProgramForUser(@PathVariable Long smartConsumerProgramId, @Valid @RequestBody SmartConsumerProgramRequest request) {
		SmartConsumerProgram smartConsumerProgram = smartConsumerProgramService.updateSmartConsumerProgramForUser(smartConsumerProgramId, request);
		SmartConsumerProgramResponse dto = new SmartConsumerProgramResponse(smartConsumerProgram);
		return ResponseEntity.ok(dto);
	}

	@DeleteMapping("/{smartConsumerProgramId}")
	public ResponseEntity<Map<String, String>> deleteSmartConsumerProgramForUser(@PathVariable Long smartConsumerProgramId) {
		Map<String, String> success = smartConsumerProgramService.deleteSmartConsumerProgramForUser(smartConsumerProgramId);
		return ResponseEntity.ok().body(success);
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<SmartConsumerProgram>> getAllSmartConsumerPrograms() {
		List<SmartConsumerProgram> smartConsumerProgramList = smartConsumerProgramService.getAllSmartConsumerPrograms();
		return ResponseEntity.ok(smartConsumerProgramList);
	}

	@GetMapping("/admin/{smartConsumerProgramId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<SmartConsumerProgram> getSmartConsumerProgramById(@PathVariable Long smartConsumerProgramId) {
		SmartConsumerProgram smartConsumerProgram = smartConsumerProgramService.getSmartConsumerProgramById(smartConsumerProgramId);
		return ResponseEntity.ok(smartConsumerProgram);
	}
}
