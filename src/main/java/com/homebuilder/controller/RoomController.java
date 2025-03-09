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

import java.util.ArrayList;
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
	public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody RoomRequest request) {
		Room room = roomService.createRoom(request);
		RoomResponse dto = new RoomResponse(room);
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@PostMapping("/createlist")
	@PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
	public ResponseEntity<List<RoomResponse>> createRoomList(@RequestBody List<@Valid RoomRequest> request) {
		List<Room> roomList = roomService.createRoomList(request);
		List<RoomResponse> dtos = new ArrayList<>();
		roomList.forEach(room -> dtos.add(new RoomResponse(room)));
		return ResponseEntity.status(HttpStatus.CREATED).body(dtos);
	}

	@GetMapping
	public ResponseEntity<List<RoomResponse>> getAllRooms() {
		List<Room> roomList = roomService.getAllRooms();
		List<RoomResponse> dtoList = roomList.stream().map(RoomResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}

	@GetMapping("/{roomId}")
	public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long roomId) {
		Room room = roomService.getRoomById(roomId);
		RoomResponse dto = new RoomResponse(room);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/update")
	public ResponseEntity<RoomResponse> updateRoom(@Valid @RequestBody RoomRequest request) {
		Room room = roomService.updateRoom(request);
		RoomResponse dto = new RoomResponse(room);
		return ResponseEntity.ok(dto);
	}

	@DeleteMapping("/{roomId}")
	public ResponseEntity<Map<String, String>> deleteRoom(@PathVariable Long roomId) {
		Map<String, String> success = roomService.deleteRoom(roomId);
		return ResponseEntity.ok().body(success);
	}

	@PutMapping("/{roomId}/add-device")
	public ResponseEntity<Map<String, String>> addDeviceToRoom(@PathVariable Long roomId, @RequestParam Long deviceId) {
		Map<String, String> success = roomService.assignDeviceToRoom(roomId, deviceId);
		return ResponseEntity.ok().body(success);
	}

	@PutMapping("/{roomId}/remove-device")
	public ResponseEntity<Map<String, String>> removeDeviceFromRoom(@PathVariable Long roomId, @RequestParam Long deviceId) {
		Map<String, String> success = roomService.removeDeviceFromRoom(roomId, deviceId);
		return ResponseEntity.ok().body(success);
	}

	@GetMapping("/owner")
	@PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
	public ResponseEntity<List<RoomResponse>> getAllRoomsByOwner(@RequestParam Long ownerId) {
		List<Room> roomList = roomService.getAllRoomsByOwner(ownerId);
		List<RoomResponse> dtoList = roomList.stream().map(RoomResponse::new).toList();
		return ResponseEntity.ok(dtoList);
	}

	@DeleteMapping("/delete/{ownerId}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
	public ResponseEntity<Map<String, String>> deleteAllRoomsByOwner(@PathVariable Long ownerId) {
		Map<String, String> success = roomService.deleteAllRoomsByOwnerId(ownerId);
		return ResponseEntity.ok(success);
	}
}
