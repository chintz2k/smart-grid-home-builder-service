package com.homebuilder.service;

import com.homebuilder.entity.Device;
import com.homebuilder.entity.Room;
import com.homebuilder.exception.DeviceNotFoundException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.repository.RoomRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author André Heinen
 */
@Service
public class RoomServiceImpl implements RoomService {

	private final RoomRepository roomRepository;

	private final DeviceService deviceService;

	@Autowired
	public RoomServiceImpl(RoomRepository roomRepository, DeviceService deviceService) {
		this.roomRepository = roomRepository;
		this.deviceService = deviceService;
	}

	// CRUD-Operationen für SH-Nutzer
	@Override
	public Room createRoomForUser(@Valid Room room, Long userId) {
		room.setUserId(userId);
		return roomRepository.save(room);
	}

	@Override
	public List<Room> getAllRoomsFromUser(Long userId) {
		return roomRepository.findByUserId(userId).orElseThrow(() -> new DeviceNotFoundException("No Rooms found for User with ID " + userId));
	}

	@Override
	public Room getRoomByIdFromUser(Long roomId, Long userId) {
		Room room = roomRepository.findById(roomId).orElseThrow(() -> new DeviceNotFoundException("Room with ID " + roomId + " not found"));
		if (!room.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to room with ID " + roomId);
		}
		return room;
	}

	@Override
	public Room updateRoomForUser(Long roomId, Long userId, @Valid Room roomDetails) {
		Room room = roomRepository.findById(roomId).orElseThrow(() -> new DeviceNotFoundException("Room with ID " + roomId + " not found"));
		if (!room.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to room with ID " + roomId);
		}
		room.setName(roomDetails.getName());
		return roomRepository.save(room);
	}

	@Override
	public void deleteRoomForUser(Long roomId, Long userId) {
		Room room = roomRepository.findById(roomId).orElseThrow(() -> new DeviceNotFoundException("Room with ID " + roomId + " not found"));
		if (!room.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to room with ID " + roomId);
		}
		roomRepository.delete(room);
	}

	@Override
	public void assignDeviceToRoomForUser(Long roomId, Long deviceId, Long userId) {
		Room room = roomRepository.findById(roomId)
				.orElseThrow(() -> new DeviceNotFoundException("Room with ID " + roomId + " not found"));

		if (!room.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to room with ID " + roomId);
		}

		Device device = deviceService.getDeviceByIdAndUser(deviceId, userId);
		room.addDevice(device);
		roomRepository.save(room);
	}

	@Override
	public void removeDeviceFromRoomForUser(Long roomId, Long deviceId, Long userId) {
		Room room = roomRepository.findById(roomId)
				.orElseThrow(() -> new DeviceNotFoundException("Room with ID " + roomId + " not found"));

		if (!room.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to room with ID " + roomId);
		}

		Device device = deviceService.getDeviceByIdAndUser(deviceId, userId);
		room.removeDevice(device);
		roomRepository.save(room);
	}

	// CRUD-Operationen für administrative Aufgaben
	@Override
	public List<Room> getAllRooms() {
		return roomRepository.findAll();
	}

	@Override
	public Room getRoomById(Long roomId) {
		return roomRepository.findById(roomId).orElseThrow(() -> new DeviceNotFoundException("Room with ID " + roomId + " not found"));
	}
}
