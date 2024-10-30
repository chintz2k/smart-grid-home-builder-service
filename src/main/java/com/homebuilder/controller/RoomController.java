package com.homebuilder.controller;

import com.homebuilder.entity.Room;
import com.homebuilder.exception.InvalidJwtException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.security.JwtUtil;
import com.homebuilder.service.RoomService;
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
@RequestMapping("/api/rooms")
public class RoomController {

	private final RoomService roomService;

	private final JwtUtil jwtUtil;

	@Autowired
	public RoomController(RoomService roomService, JwtUtil jwtUtil) {
		this.roomService = roomService;
		this.jwtUtil = jwtUtil;
	}

	// CRUD-Endpoints für SH-Nutzer
	@PostMapping
	public ResponseEntity<Room> createRoomForUser(@Valid @RequestBody Room room, Principal principal) {
		if (principal != null) {
			String token = ((UsernamePasswordAuthenticationToken) principal).getCredentials().toString();
			Long userId = jwtUtil.extractUserId(token);
			return ResponseEntity.status(HttpStatus.CREATED).body(roomService.createRoomForUser(room, userId));
		} else {
			throw new UnauthorizedAccessException("Unauthorized try to create room for user");
		}
	}

	@GetMapping
	public ResponseEntity<?> getAllRoomsFromUser(Principal principal) {
		if (principal != null) {
			try {
				String token = ((UsernamePasswordAuthenticationToken) principal).getCredentials().toString();
				Long userId = jwtUtil.extractUserId(token);
				return ResponseEntity.ok(roomService.getAllRoomsFromUser(userId));
			} catch (SignatureException ex) {
				throw new InvalidJwtException("Invalid JWT token signature");
			} catch (Exception ex) {
				throw new UnauthorizedAccessException("Unauthorized access to rooms for user");
			}
		} else {
			throw new UnauthorizedAccessException("Unauthorized access to rooms for user");
		}
	}


	@GetMapping("/{roomId}")
	public ResponseEntity<Room> getRoomByIdFromUser(@PathVariable Long roomId, Principal principal) {
		if (principal != null) {
			String token = ((UsernamePasswordAuthenticationToken) principal).getCredentials().toString();
			Long userId = jwtUtil.extractUserId(token);
			return ResponseEntity.ok(roomService.getRoomByIdFromUser(roomId, userId));
		} else {
			throw new UnauthorizedAccessException("Unauthorized access to room for user");
		}
	}

	@PutMapping("/{roomId}")
	public ResponseEntity<Room> updateRoomForUser(@PathVariable Long roomId, @Valid @RequestBody Room roomDetails, Principal principal) {
		if (principal != null) {
			String token = ((UsernamePasswordAuthenticationToken) principal).getCredentials().toString();
			Long userId = jwtUtil.extractUserId(token);
			return ResponseEntity.ok(roomService.updateRoomForUser(roomId, userId, roomDetails));
		} else {
			throw new UnauthorizedAccessException("Unauthorized try to update a room for user");
		}
	}

	@DeleteMapping("/{roomId}")
	public ResponseEntity<?> deleteRoomForUser(@PathVariable Long roomId, Principal principal) {
		if (principal != null) {
			String token = ((UsernamePasswordAuthenticationToken) principal).getCredentials().toString();
			Long userId = jwtUtil.extractUserId(token);
			roomService.deleteRoomForUser(roomId, userId);
			Map<String, String> success = new HashMap<>();
			success.put("success", "Successfully deleted room with ID " + roomId);
			return ResponseEntity.ok().body(success);
		} else {
			throw new UnauthorizedAccessException("Unauthorized try to delete a room for user");
		}
	}

	@PutMapping("/{roomId}/add-device")
	public ResponseEntity<Map<String, String>> addDeviceToRoom(
			@PathVariable Long roomId,
			@RequestParam Long deviceId,
			Principal principal) {

		if (principal != null) {
			String token = ((UsernamePasswordAuthenticationToken) principal).getCredentials().toString();
			Long userId = jwtUtil.extractUserId(token);

			roomService.assignDeviceToRoomForUser(roomId, deviceId, userId);
			Map<String, String> success = new HashMap<>();
			success.put("success", "Device added to room successfully");
			return ResponseEntity.ok().body(success);
		} else {
			throw new UnauthorizedAccessException("Unauthorized access to add device to room");
		}
	}

	@PutMapping("/{roomId}/remove-device")
	public ResponseEntity<Map<String, String>> removeDeviceFromRoom(
			@PathVariable Long roomId,
			@RequestParam Long deviceId,
			Principal principal) {

		if (principal != null) {
			String token = ((UsernamePasswordAuthenticationToken) principal).getCredentials().toString();
			Long userId = jwtUtil.extractUserId(token);

			roomService.removeDeviceFromRoomForUser(roomId, deviceId, userId);
			Map<String, String> success = new HashMap<>();
			success.put("success", "Device removed from room successfully");
			return ResponseEntity.ok().body(success);
		} else {
			throw new UnauthorizedAccessException("Unauthorized access to remove device from room");
		}
	}

	// CRUD-Endpoints für administrative Aufgaben
	@GetMapping("/admin")
	public ResponseEntity<List<Room>> getAllRooms() {
		return ResponseEntity.ok(roomService.getAllRooms());
	}

	@GetMapping("/admin/{roomId}")
	public ResponseEntity<Room> getRoomById(@PathVariable Long roomId) {
		return ResponseEntity.ok(roomService.getRoomById(roomId));
	}
}
