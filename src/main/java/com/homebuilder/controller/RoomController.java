package com.homebuilder.controller;

import com.homebuilder.entity.Room;
import com.homebuilder.service.RoomService;
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
@RequestMapping("/api/rooms")
public class RoomController {

	private final RoomService roomService;

	@Autowired
	public RoomController(RoomService roomService) {
		this.roomService = roomService;
	}

	// CRUD-Endpoints für SH-Nutzer
	@PostMapping
	public ResponseEntity<Room> createRoomForUser(@RequestBody Room room, Principal principal) {
		return ResponseEntity.status(HttpStatus.CREATED).body(roomService.createRoom(room, Long.valueOf(principal.getName())));
	}

	@GetMapping
	public ResponseEntity<List<Room>> getRoomsForUser(Principal principal) {
		return ResponseEntity.ok(roomService.getRoomsForUser(Long.valueOf(principal.getName())));
	}

	@GetMapping("/{roomId}")
	public ResponseEntity<Room> getRoomForUser(@PathVariable Long roomId, Principal principal) {
		return ResponseEntity.ok(roomService.getRoomForUser(roomId, Long.valueOf(principal.getName())));
	}

	@PutMapping("/{roomId}")
	public ResponseEntity<Room> updateRoomForUser(@PathVariable Long roomId, @RequestBody Room roomDetails, Principal principal) {
		return ResponseEntity.ok(roomService.updateRoomForUser(roomId, Long.valueOf(principal.getName()), roomDetails));
	}

	@DeleteMapping("/{roomId}")
	public ResponseEntity<?> deleteRoomForUser(@PathVariable Long roomId, Principal principal) {
		roomService.deleteRoomForUser(roomId, Long.valueOf(principal.getName()));
		return ResponseEntity.ok().build();
	}

	// CRUD-Operationen für administrative Aufgaben
	@GetMapping("/admin/all")
	public ResponseEntity<List<Room>> getAllRooms() {
		return ResponseEntity.ok(roomService.getAllRooms());
	}

	@GetMapping("/admin/{roomId}")
	public ResponseEntity<Room> getRoomById(@PathVariable Long roomId) {
		return ResponseEntity.ok(roomService.getRoomById(roomId));
	}
}
