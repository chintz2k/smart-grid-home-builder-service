package com.homebuilder.controller;

import com.homebuilder.dto.StorageRequest;
import com.homebuilder.dto.StorageResponse;
import com.homebuilder.entity.Storage;
import com.homebuilder.service.StorageService;
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
@RequestMapping("/api/storages")
public class StorageController {

	private final StorageService storageService;

	@Autowired
	public StorageController(StorageService storageService) {
		this.storageService = storageService;
	}

	@PostMapping
	public ResponseEntity<StorageResponse> createStorageForUser(@Valid @RequestBody StorageRequest request) {
		Storage storage = storageService.createStorageForUser(request);
		StorageResponse dto = new StorageResponse(storage);
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@GetMapping
	public ResponseEntity<List<StorageResponse>> getAllStoragesFromUser() {
		List<Storage> storageList = storageService.getAllStoragesFromUser();
		List<StorageResponse> dtoList = storageList.stream().map(StorageResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}

	@GetMapping("/{storageId}")
	public ResponseEntity<StorageResponse> getStorageByIdFromUser(@PathVariable Long storageId) {
		Storage storage = storageService.getStorageByIdFromUser(storageId);
		StorageResponse dto = new StorageResponse(storage);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/{storageId}")
	public ResponseEntity<StorageResponse> updateStorageForUser(@PathVariable Long storageId, @Valid @RequestBody StorageRequest request) {
		Storage storage = storageService.updateStorageForUser(storageId, request);
		StorageResponse dto = new StorageResponse(storage);
		return ResponseEntity.ok(dto);
	}

	@DeleteMapping("/{storageId}")
	public ResponseEntity<Map<String, String>> deleteStorageForUser(@PathVariable Long storageId) {
		Map<String, String> success = storageService.deleteStorageForUser(storageId);
		return ResponseEntity.ok().body(success);
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<Storage>> getAllStorages() {
		List<Storage> storageList = storageService.getAllStorages();
		return ResponseEntity.ok(storageList);
	}

	@GetMapping("/admin/{storageId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Storage> getStorageById(@PathVariable Long storageId) {
		Storage storage = storageService.getStorageById(storageId);
		return ResponseEntity.ok(storage);
	}
}
