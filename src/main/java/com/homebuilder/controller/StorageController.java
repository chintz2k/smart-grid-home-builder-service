package com.homebuilder.controller;

import com.homebuilder.dto.StorageRequest;
import com.homebuilder.dto.StorageResponse;
import com.homebuilder.entity.Storage;
import com.homebuilder.service.StorageService;
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
@RequestMapping("/api/storages")
public class StorageController {

	private final StorageService storageService;

	@Autowired
	public StorageController(StorageService storageService) {
		this.storageService = storageService;
	}

	@PostMapping
	public ResponseEntity<StorageResponse> createStorage(@Valid @RequestBody StorageRequest request) {
		Storage storage = storageService.createStorage(request);
		StorageResponse dto = new StorageResponse(storage);
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@PostMapping("/createlist")
	@PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
	public ResponseEntity<List<StorageResponse>> createStorageList(@RequestBody List<@Valid StorageRequest> request) {
		List<Storage> storageList = storageService.createStorageList(request);
		List<StorageResponse> dtos = new ArrayList<>();
		storageList.forEach(storage -> dtos.add(new StorageResponse(storage)));
		return ResponseEntity.status(HttpStatus.CREATED).body(dtos);
	}

	@GetMapping
	public ResponseEntity<List<StorageResponse>> getAllUnarchivedStorages() {
		List<Storage> storageList = storageService.getAllUnarchivedStorages();
		List<StorageResponse> dtoList = storageList.stream().map(StorageResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}

	@GetMapping("/list/self/noroom")
	public ResponseEntity<Page<StorageResponse>> getAllStoragesBySelfAndNoRoom(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size
	) {
		Page<StorageResponse> pages = storageService.getAllStoragesByOwnerAndRoomIsNull(PageRequest.of(page, size));
		return ResponseEntity.ok(pages);
	}

	@GetMapping("/list/self")
	public ResponseEntity<Page<StorageResponse>> getAllStoragesBySelf(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size
	) {
		Page<StorageResponse> pages = storageService.getAllUnarchivedByUser(PageRequest.of(page, size));
		return ResponseEntity.ok(pages);
	}

	@GetMapping("/all")
	public ResponseEntity<List<StorageResponse>> getAllStorages() {
		List<Storage> storageList = storageService.getAllStorages();
		List<StorageResponse> dtoList = storageList.stream().map(StorageResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}

	@GetMapping("/{storageId}")
	public ResponseEntity<StorageResponse> getStorageById(@PathVariable Long storageId) {
		Storage storage = storageService.getStorageById(storageId);
		StorageResponse dto = new StorageResponse(storage);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/update")
	public ResponseEntity<StorageResponse> updateStorage(@Valid @RequestBody StorageRequest request) {
		Storage storage = storageService.updateStorage(request);
		StorageResponse dto = new StorageResponse(storage);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/{storageId}/archive")
	public ResponseEntity<Map<String, String>> archiveStorage(@PathVariable Long storageId) {
		Map<String, String> success = storageService.archiveStorage(storageId);
		return ResponseEntity.ok().body(success);
	}

	@DeleteMapping("/{storageId}")
	public ResponseEntity<Map<String, String>> deleteStorage(@PathVariable Long storageId) {
		Map<String, String> success = storageService.deleteStorage(storageId);
		return ResponseEntity.ok().body(success);
	}

	@GetMapping("/owner")
	@PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
	public ResponseEntity<List<StorageResponse>> getAllStoragesByOwner(@RequestParam Long ownerId) {
		List<Storage> storageList = storageService.getAllStoragesByOwner(ownerId);
		List<StorageResponse> dtoList = storageList.stream().map(StorageResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}
}
