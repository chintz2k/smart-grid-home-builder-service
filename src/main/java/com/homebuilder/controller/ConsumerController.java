package com.homebuilder.controller;

import com.homebuilder.entity.Consumer;
import com.homebuilder.exception.InvalidJwtException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.security.JwtUtil;
import com.homebuilder.service.ConsumerService;
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
@RequestMapping("/api/consumers")
public class ConsumerController {

	private final ConsumerService consumerService;

	private final JwtUtil jwtUtil;

	@Autowired
	public ConsumerController(ConsumerService consumerService, JwtUtil jwtUtil) {
		this.consumerService = consumerService;
		this.jwtUtil = jwtUtil;
	}

	// CRUD-Endpoints für SH-Nutzer
	@PostMapping
	public ResponseEntity<Consumer> createConsumerForUser(@Valid @RequestBody Consumer consumer, Principal principal) {
		if (principal != null) {
			String token = ((UsernamePasswordAuthenticationToken) principal).getCredentials().toString();
			Long userId = jwtUtil.extractUserId(token);
			return ResponseEntity.status(HttpStatus.CREATED).body(consumerService.createConsumerForUser(consumer, userId));
		} else {
			throw new UnauthorizedAccessException("Unauthorized try to create consumer for user");
		}
	}

	@GetMapping
	public ResponseEntity<?> getAllConsumersFromUser(Principal principal) {
		if (principal != null) {
			try {
				String token = ((UsernamePasswordAuthenticationToken) principal).getCredentials().toString();
				Long userId = jwtUtil.extractUserId(token);
				return ResponseEntity.ok(consumerService.getAllConsumersFromUser(userId));
			} catch (SignatureException ex) {
				throw new InvalidJwtException("Invalid JWT token signature");
			} catch (Exception ex) {
				throw new UnauthorizedAccessException("Unauthorized access to consumers for user");
			}
		} else {
			throw new UnauthorizedAccessException("Unauthorized access to consumers for user");
		}
	}


	@GetMapping("/{consumerId}")
	public ResponseEntity<Consumer> getConsumerByIdFromUser(@PathVariable Long consumerId, Principal principal) {
		if (principal != null) {
			String token = ((UsernamePasswordAuthenticationToken) principal).getCredentials().toString();
			Long userId = jwtUtil.extractUserId(token);
			return ResponseEntity.ok(consumerService.getConsumerByIdFromUser(consumerId, userId));
		} else {
			throw new UnauthorizedAccessException("Unauthorized access to consumer for user");
		}
	}

	@PutMapping("/{consumerId}")
	public ResponseEntity<Consumer> updateConsumerForUser(@PathVariable Long consumerId, @Valid @RequestBody Consumer consumerDetails, Principal principal) {
		if (principal != null) {
			String token = ((UsernamePasswordAuthenticationToken) principal).getCredentials().toString();
			Long userId = jwtUtil.extractUserId(token);
			return ResponseEntity.ok(consumerService.updateConsumerForUser(consumerId, userId, consumerDetails));
		} else {
			throw new UnauthorizedAccessException("Unauthorized try to update a consumer for user");
		}
	}

	@DeleteMapping("/{consumerId}")
	public ResponseEntity<?> deleteConsumerForUser(@PathVariable Long consumerId, Principal principal) {
		if (principal != null) {
			String token = ((UsernamePasswordAuthenticationToken) principal).getCredentials().toString();
			Long userId = jwtUtil.extractUserId(token);
			consumerService.deleteConsumerForUser(consumerId, userId);
			Map<String, String> success = new HashMap<>();
			success.put("success", "Successfully deleted consumer with ID " + consumerId);
			return ResponseEntity.ok().body(success);
		} else {
			throw new UnauthorizedAccessException("Unauthorized try to delete a consumer for user");
		}
	}

	// CRUD-Endpoints für administrative Aufgaben
	@GetMapping("/admin/")
	public ResponseEntity<List<Consumer>> getAllConsumers() {
		return ResponseEntity.ok(consumerService.getAllConsumers());
	}

	@GetMapping("/admin/{consumerId}")
	public ResponseEntity<Consumer> getConsumerById(@PathVariable Long consumerId) {
		return ResponseEntity.ok(consumerService.getConsumerById(consumerId));
	}
}
