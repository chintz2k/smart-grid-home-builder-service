package com.homebuilder.controller;

import com.homebuilder.dto.RoomRequest;
import com.homebuilder.dto.RoomResponse;
import com.homebuilder.entity.Room;
import com.homebuilder.service.RoomService;
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
@RequestMapping("/api/rooms")
public class RoomController {

	private final RoomService roomService;

	@Autowired
	public RoomController(RoomService roomService) {
		this.roomService = roomService;
	}

	@PostMapping
	public ResponseEntity<RoomResponse> createRoomForUser(@Valid @RequestBody RoomRequest request) {
		Room room = roomService.createRoomForUser(request);
		RoomResponse dto = new RoomResponse(room);
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@GetMapping
	public ResponseEntity<List<RoomResponse>> getAllRoomsFromUser() {
		List<Room> roomList = roomService.getAllRoomsFromUser();
		List<RoomResponse> dtoList = roomList.stream().map(RoomResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}

	@GetMapping("/{roomId}")
	public ResponseEntity<RoomResponse> getRoomByIdFromUser(@PathVariable Long roomId) {
		Room room = roomService.getRoomByIdFromUser(roomId);
		RoomResponse dto = new RoomResponse(room);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/{roomId}")
	public ResponseEntity<RoomResponse> updateRoomForUser(@PathVariable Long roomId, @Valid @RequestBody RoomRequest request) {
		Room room = roomService.updateRoomForUser(roomId, request);
		RoomResponse dto = new RoomResponse(room);
		return ResponseEntity.ok(dto);
	}

	@DeleteMapping("/{roomId}")
	public ResponseEntity<Map<String, String>> deleteRoomForUser(@PathVariable Long roomId) {
		Map<String, String> success = roomService.deleteRoomForUser(roomId);
		return ResponseEntity.ok().body(success);
	}

	@PutMapping("/{roomId}/add-device")
	public ResponseEntity<Map<String, String>> addDeviceToRoom(@PathVariable Long roomId, @RequestParam Long deviceId) {
		Map<String, String> success = roomService.assignDeviceToRoomForUser(roomId, deviceId);
		return ResponseEntity.ok().body(success);
	}

	@PutMapping("/{roomId}/remove-device")
	public ResponseEntity<Map<String, String>> removeDeviceFromRoom(@PathVariable Long roomId, @RequestParam Long deviceId) {
		Map<String, String> success = roomService.removeDeviceFromRoomForUser(roomId, deviceId);
		return ResponseEntity.ok().body(success);
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<Room>> getAllRooms() {
		return ResponseEntity.ok(roomService.getAllRooms());
	}

	@GetMapping("/admin/{roomId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Room> getRoomById(@PathVariable Long roomId) {
		return ResponseEntity.ok(roomService.getRoomById(roomId));
	}
}
