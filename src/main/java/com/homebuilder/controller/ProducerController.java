package com.homebuilder.controller;

import com.homebuilder.entity.Producer;
import com.homebuilder.exception.InvalidJwtException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.security.JwtUtil;
import com.homebuilder.service.ProducerService;
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
@RequestMapping("/api/producers")
public class ProducerController {

	private final ProducerService producerService;

	private final JwtUtil jwtUtil;

	@Autowired
	public ProducerController(ProducerService producerService, JwtUtil jwtUtil) {
		this.producerService = producerService;
		this.jwtUtil = jwtUtil;
	}

	// CRUD-Endpoints für SH-Nutzer
	@PostMapping
	public ResponseEntity<Producer> createProducerForUser(@Valid @RequestBody Producer producer, Principal principal) {
		if (principal != null) {
			String token = ((UsernamePasswordAuthenticationToken) principal).getCredentials().toString();
			Long userId = jwtUtil.extractUserId(token);
			return ResponseEntity.status(HttpStatus.CREATED).body(producerService.createProducerForUser(producer, userId));
		} else {
			throw new UnauthorizedAccessException("Unauthorized try to create producer for user");
		}
	}

	@GetMapping
	public ResponseEntity<?> getAllProducersFromUser(Principal principal) {
		if (principal != null) {
			try {
				String token = ((UsernamePasswordAuthenticationToken) principal).getCredentials().toString();
				Long userId = jwtUtil.extractUserId(token);
				return ResponseEntity.ok(producerService.getAllProducersFromUser(userId));
			} catch (SignatureException ex) {
				throw new InvalidJwtException("Invalid JWT token signature");
			} catch (Exception ex) {
				throw new UnauthorizedAccessException("Unauthorized access to producers for user");
			}
		} else {
			throw new UnauthorizedAccessException("Unauthorized access to producers for user");
		}
	}


	@GetMapping("/{producerId}")
	public ResponseEntity<Producer> getProducerByIdFromUser(@PathVariable Long producerId, Principal principal) {
		if (principal != null) {
			String token = ((UsernamePasswordAuthenticationToken) principal).getCredentials().toString();
			Long userId = jwtUtil.extractUserId(token);
			return ResponseEntity.ok(producerService.getProducerByIdFromUser(producerId, userId));
		} else {
			throw new UnauthorizedAccessException("Unauthorized access to producer for user");
		}
	}

	@PutMapping("/{producerId}")
	public ResponseEntity<Producer> updateProducerForUser(@PathVariable Long producerId, @Valid @RequestBody Producer producerDetails, Principal principal) {
		if (principal != null) {
			String token = ((UsernamePasswordAuthenticationToken) principal).getCredentials().toString();
			Long userId = jwtUtil.extractUserId(token);
			return ResponseEntity.ok(producerService.updateProducerForUser(producerId, userId, producerDetails));
		} else {
			throw new UnauthorizedAccessException("Unauthorized try to update a producer for user");
		}
	}

	@DeleteMapping("/{producerId}")
	public ResponseEntity<?> deleteProducerForUser(@PathVariable Long producerId, Principal principal) {
		if (principal != null) {
			String token = ((UsernamePasswordAuthenticationToken) principal).getCredentials().toString();
			Long userId = jwtUtil.extractUserId(token);
			producerService.deleteProducerForUser(producerId, userId);
			Map<String, String> success = new HashMap<>();
			success.put("success", "Successfully deleted producer with ID " + producerId);
			return ResponseEntity.ok().body(success);
		} else {
			throw new UnauthorizedAccessException("Unauthorized try to delete a producer for user");
		}
	}

	// CRUD-Endpoints für administrative Aufgaben
	@GetMapping("/admin/")
	public ResponseEntity<List<Producer>> getAllProducers() {
		return ResponseEntity.ok(producerService.getAllProducers());
	}

	@GetMapping("/admin/{producerId}")
	public ResponseEntity<Producer> getProducerById(@PathVariable Long producerId) {
		return ResponseEntity.ok(producerService.getProducerById(producerId));
	}
}
