package com.homebuilder.controller;

import com.homebuilder.entity.Storage;
import com.homebuilder.exception.InvalidJwtException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.security.JwtUtil;
import com.homebuilder.service.StorageService;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
@RestController
@RequestMapping("/api/storages")
public class StorageController {

	private final StorageService storageService;

	private final JwtUtil jwtUtil;

	@Autowired
	public StorageController(StorageService storageService, JwtUtil jwtUtil) {
		this.storageService = storageService;
		this.jwtUtil = jwtUtil;
	}

	// CRUD-Endpoints für SH-Nutzer
	@PostMapping
	public ResponseEntity<Storage> createStorageForUser(@Valid @RequestBody Storage storage, Principal principal) {
		if (principal != null) {
			String token = ((UsernamePasswordAuthenticationToken) principal).getCredentials().toString();
			Long userId = jwtUtil.extractUserId(token);
			return ResponseEntity.status(HttpStatus.CREATED).body(storageService.createStorageForUser(storage, userId));
		} else {
			throw new UnauthorizedAccessException("Unauthorized try to create storage for user");
		}
	}

	@GetMapping
	public ResponseEntity<?> getAllStoragesFromUser(Principal principal) {
		if (principal != null) {
			try {
				String token = ((UsernamePasswordAuthenticationToken) principal).getCredentials().toString();
				Long userId = jwtUtil.extractUserId(token);
				return ResponseEntity.ok(storageService.getAllStoragesFromUser(userId));
			} catch (SignatureException ex) {
				throw new InvalidJwtException("Invalid JWT token signature");
			} catch (Exception ex) {
				throw new UnauthorizedAccessException("Unauthorized access to storages for user");
			}
		} else {
			throw new UnauthorizedAccessException("Unauthorized access to storages for user");
		}
	}


	@GetMapping("/{storageId}")
	public ResponseEntity<Storage> getStorageByIdFromUser(@PathVariable Long storageId, Principal principal) {
		if (principal != null) {
			String token = ((UsernamePasswordAuthenticationToken) principal).getCredentials().toString();
			Long userId = jwtUtil.extractUserId(token);
			return ResponseEntity.ok(storageService.getStorageByIdFromUser(storageId, userId));
		} else {
			throw new UnauthorizedAccessException("Unauthorized access to storage for user");
		}
	}

	@PutMapping("/{storageId}")
	public ResponseEntity<Storage> updateStorageForUser(@PathVariable Long storageId, @Valid @RequestBody Storage storageDetails, Principal principal) {
		if (principal != null) {
			String token = ((UsernamePasswordAuthenticationToken) principal).getCredentials().toString();
			Long userId = jwtUtil.extractUserId(token);
			return ResponseEntity.ok(storageService.updateStorageForUser(storageId, userId, storageDetails));
		} else {
			throw new UnauthorizedAccessException("Unauthorized try to update a storage for user");
		}
	}

	@DeleteMapping("/{storageId}")
	public ResponseEntity<?> deleteStorageForUser(@PathVariable Long storageId, Principal principal) {
		if (principal != null) {
			String token = ((UsernamePasswordAuthenticationToken) principal).getCredentials().toString();
			Long userId = jwtUtil.extractUserId(token);
			storageService.deleteStorageForUser(storageId, userId);
			Map<String, String> success = new HashMap<>();
			success.put("success", "Successfully deleted storage with ID " + storageId);
			return ResponseEntity.ok().body(success);
		} else {
			throw new UnauthorizedAccessException("Unauthorized try to delete a storage for user");
		}
	}

	// CRUD-Endpoints für administrative Aufgaben
	@GetMapping("/admin")
	public ResponseEntity<List<Storage>> getAllStorages() {
		return ResponseEntity.ok(storageService.getAllStorages());
	}

	@GetMapping("/admin/{storageId}")
	public ResponseEntity<Storage> getStorageById(@PathVariable Long storageId) {
		return ResponseEntity.ok(storageService.getStorageById(storageId));
	}
}
