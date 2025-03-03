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
	public ResponseEntity<SmartConsumerProgramResponse> createSmartConsumerProgram(@Valid @RequestBody SmartConsumerProgramRequest request) {
		SmartConsumerProgram smartConsumerProgram = smartConsumerProgramService.createSmartConsumerProgram(request);
		SmartConsumerProgramResponse dto = new SmartConsumerProgramResponse(smartConsumerProgram);
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@GetMapping()
	public ResponseEntity<List<SmartConsumerProgramResponse>> getAllUnarchivedSmartConsumerPrograms() {
		List<SmartConsumerProgram> smartConsumerProgramList = smartConsumerProgramService.getAllUnarchivedSmartConsumerPrograms();
		List<SmartConsumerProgramResponse> dtoList = smartConsumerProgramList.stream().map(SmartConsumerProgramResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}

	@GetMapping("/all")
	public ResponseEntity<List<SmartConsumerProgramResponse>> getAllSmartConsumerPrograms() {
		List<SmartConsumerProgram> smartConsumerProgramList = smartConsumerProgramService.getAllSmartConsumerPrograms();
		List<SmartConsumerProgramResponse> dtoList = smartConsumerProgramList.stream().map(SmartConsumerProgramResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}

	@GetMapping("/{smartConsumerProgramId}")
	public ResponseEntity<SmartConsumerProgramResponse> getSmartConsumerProgramById(@PathVariable Long smartConsumerProgramId) {
		SmartConsumerProgram smartConsumerProgram = smartConsumerProgramService.getSmartConsumerProgramById(smartConsumerProgramId);
		SmartConsumerProgramResponse dto = new SmartConsumerProgramResponse(smartConsumerProgram);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/update")
	public ResponseEntity<SmartConsumerProgramResponse> updateSmartConsumerProgramForUser(@Valid @RequestBody SmartConsumerProgramRequest request) {
		SmartConsumerProgram smartConsumerProgram = smartConsumerProgramService.updateSmartConsumerProgram(request);
		SmartConsumerProgramResponse dto = new SmartConsumerProgramResponse(smartConsumerProgram);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/{smartConsumerProgramId}/archive")
	public ResponseEntity<Map<String, String>> archiveSmartConsumerProgramForUser(@PathVariable Long smartConsumerProgramId) {
		Map<String, String> success = smartConsumerProgramService.archiveSmartConsumerProgram(smartConsumerProgramId);
		return ResponseEntity.ok().body(success);
	}

	@DeleteMapping("/{smartConsumerProgramId}")
	public ResponseEntity<Map<String, String>> deleteSmartConsumerProgramForUser(@PathVariable Long smartConsumerProgramId) {
		Map<String, String> success = smartConsumerProgramService.deleteSmartConsumerProgram(smartConsumerProgramId);
		return ResponseEntity.ok().body(success);
	}

	@GetMapping("/owner")
	@PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
	public ResponseEntity<List<SmartConsumerProgramResponse>> getAllSmartConsumerProgramsByOwner(@RequestParam Long ownerId) {
		List<SmartConsumerProgram> smartConsumerProgramList = smartConsumerProgramService.getAllSmartConsumerProgramsByOwner(ownerId);
		List<SmartConsumerProgramResponse> dtoList = smartConsumerProgramList.stream().map(SmartConsumerProgramResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}
}
