package com.homebuilder.controller;

import com.homebuilder.entity.Storage;
import com.homebuilder.service.StorageService;
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
@RequestMapping("/api/storages")
public class StorageController {

	private final StorageService storageService;

	@Autowired
	public StorageController(StorageService storageService) {
		this.storageService = storageService;
	}

	// CRUD-Endpoints für SH-Nutzer
	@PostMapping
	public ResponseEntity<Storage> createStorage(@RequestBody Storage storage, Principal principal) {
		return ResponseEntity.status(HttpStatus.CREATED).body(storageService.createStorage(storage, Long.valueOf(principal.getName())));
	}

	@GetMapping
	public ResponseEntity<List<Storage>> getStoragesForUser(Principal principal) {
		return ResponseEntity.ok(storageService.getStoragesForUser(Long.valueOf(principal.getName())));
	}

	@GetMapping("/{storageId}")
	public ResponseEntity<Storage> getStorageForUser(@PathVariable Long storageId, Principal principal) {
		return ResponseEntity.ok(storageService.getStorageForUser(storageId, Long.valueOf(principal.getName())));
	}

	@PutMapping("/{storageId}")
	public ResponseEntity<Storage> updateStorageForUser(@PathVariable Long storageId, @RequestBody Storage storageDetails, Principal principal) {
		return ResponseEntity.ok(storageService.updateStorageForUser(storageId, Long.valueOf(principal.getName()), storageDetails));
	}

	@DeleteMapping("/{storageId}")
	public ResponseEntity<?> deleteStorageForUser(@PathVariable Long storageId, Principal principal) {
		storageService.deleteStorageForUser(storageId, Long.valueOf(principal.getName()));
		return ResponseEntity.ok().build();
	}

	// CRUD-Endpoints für administrative Aufgaben
	@GetMapping("/admin/all")
	public ResponseEntity<List<Storage>> getAllStorages() {
		return ResponseEntity.ok(storageService.getAllStorages());
	}

	@GetMapping("/admin/{storageId}")
	public ResponseEntity<Storage> getStorageById(@PathVariable Long storageId) {
		return ResponseEntity.ok(storageService.getStorageById(storageId));
	}
}
