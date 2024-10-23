package com.homebuilder.controller;

import com.homebuilder.entity.Consumer;
import com.homebuilder.exception.InvalidJwtException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.security.JwtUtil;
import com.homebuilder.service.ConsumerService;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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
	public ResponseEntity<Consumer> createConsumer(@RequestBody Consumer consumer, Principal principal) {
		return ResponseEntity.status(HttpStatus.CREATED).body(consumerService.createConsumerForUser(consumer, Long.valueOf(principal.getName())));
	}

	@GetMapping
	public ResponseEntity<?> getConsumersForUser(Principal principal) {
		if (principal != null) {
			try {
				String token = ((UsernamePasswordAuthenticationToken) principal).getCredentials().toString();
				Long userId = jwtUtil.extractUserId(token);
				System.out.println(jwtUtil.getClaimsFromToken(token)); // TODO DEBUG
				return ResponseEntity.ok(consumerService.getAllConsumersForUser(userId));
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
	public ResponseEntity<Consumer> getConsumerForUser(@PathVariable Long consumerId, Principal principal) {
		return ResponseEntity.ok(consumerService.getConsumerByIdForUser(consumerId, Long.valueOf(principal.getName())));
	}

	@PutMapping("/{consumerId}")
	public ResponseEntity<Consumer> updateConsumerForUser(@PathVariable Long consumerId, @RequestBody Consumer consumerDetails, Principal principal) {
		return ResponseEntity.ok(consumerService.updateConsumerForUser(consumerId, Long.valueOf(principal.getName()), consumerDetails));
	}

	@DeleteMapping("/{consumerId}")
	public ResponseEntity<?> deleteConsumerForUser(@PathVariable Long consumerId, Principal principal) {
		consumerService.deleteConsumerForUser(consumerId, Long.valueOf(principal.getName()));
		return ResponseEntity.ok().build();
	}

	// CRUD-Endpoints für administrative Aufgaben
	@GetMapping("/admin")
	public ResponseEntity<List<Consumer>> getAllConsumers() {
		return ResponseEntity.ok(consumerService.getAllConsumers());
	}

	@GetMapping("/admin/{consumerId}")
	public ResponseEntity<Consumer> getConsumerById(@PathVariable Long consumerId) {
		return ResponseEntity.ok(consumerService.getConsumerById(consumerId));
	}
}
