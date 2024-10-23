package com.homebuilder.service;

import com.homebuilder.entity.Room;
import com.homebuilder.exception.RoomNotFoundException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author André Heinen
 */
@Service
public class RoomServiceImpl implements RoomService {

	private final RoomRepository roomRepository;

	@Autowired
	public RoomServiceImpl(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	// CRUD-Operationen für SH-Nutzer
	@Override
	public Room createRoom(Room room, Long ownerId) {
		room.setOwnerId(ownerId);
		return roomRepository.save(room);
	}

	@Override
	public List<Room> getRoomsForUser(Long ownerId) {
		return roomRepository.findByOwnerId(ownerId);
	}

	@Override
	public Room getRoomForUser(Long roomId, Long ownerId) {
		Room room = roomRepository.findById(roomId)
				.orElseThrow(() -> new RoomNotFoundException("Room with ID " + roomId + " not found"));
		if (!room.getOwnerId().equals(ownerId)) {
			throw new UnauthorizedAccessException("Unauthorized access to room with ID " + roomId);
		}
		return room;
	}

	@Override
	public Room updateRoomForUser(Long roomId, Long ownerId, Room roomDetails) {
		Room room = roomRepository.findById(roomId)
				.orElseThrow(() -> new RoomNotFoundException("Room with ID " + roomId + " not found"));
		if (!room.getOwnerId().equals(ownerId)) {
			throw new UnauthorizedAccessException("Unauthorized access to room with ID " + roomId);
		}
		room.setName(roomDetails.getName());
		return roomRepository.save(room);
	}

	@Override
	public void deleteRoomForUser(Long roomId, Long ownerId) {
		Room room = roomRepository.findById(roomId)
				.orElseThrow(() -> new RoomNotFoundException("Room with ID " + roomId + " not found"));
		if (!room.getOwnerId().equals(ownerId)) {
			throw new UnauthorizedAccessException("Unauthorized access to room with ID " + roomId);
		}
		roomRepository.delete(room);
	}

	// CRUD-Operationen für administrative Aufgaben
	@Override
	public List<Room> getAllRooms() {
		return roomRepository.findAll();
	}

	@Override
	public Room getRoomById(Long roomId) {
		return roomRepository.findById(roomId)
				.orElseThrow(() -> new RoomNotFoundException("Room with ID " + roomId + " not found"));
	}
}
