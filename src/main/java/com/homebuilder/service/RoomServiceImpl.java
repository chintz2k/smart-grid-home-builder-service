package com.homebuilder.service;

import com.homebuilder.dto.RoomRequest;
import com.homebuilder.entity.Device;
import com.homebuilder.entity.Room;
import com.homebuilder.exception.DeviceNotFoundException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.repository.RoomRepository;
import com.homebuilder.security.SecurityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
@Service
public class RoomServiceImpl implements RoomService {

	private final RoomRepository roomRepository;

	private final DeviceService deviceService;
	private final SecurityService securityService;

	@Autowired
	public RoomServiceImpl(RoomRepository roomRepository, DeviceService deviceService, SecurityService securityService) {
		this.roomRepository = roomRepository;
		this.deviceService = deviceService;
		this.securityService = securityService;
	}

	@Override
	public Room createRoomForUser(@Valid RoomRequest request) {
		Long userId = securityService.getCurrentUserId();
		Room room = request.toEntity();
		room.setUserId(userId);
		roomRepository.save(room);
		return room;
	}

	@Override
	public List<Room> getAllRoomsFromUser() {
		Long userId = securityService.getCurrentUserId();
		List<Room> roomList = roomRepository.findByUserId(userId);
		if (roomList.isEmpty()) {
			throw new DeviceNotFoundException("No Rooms found for User with ID " + userId);
		}
		return roomList;
	}

	@Override
	public Room getRoomByIdFromUser(Long roomId) {
		Long userId = securityService.getCurrentUserId();
		Room room = roomRepository.findById(roomId).orElseThrow(() -> new DeviceNotFoundException("Room with ID " + roomId + " not found"));
		if (!room.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to room with ID " + roomId);
		}
		return room;
	}

	@Override
	public Room updateRoomForUser(Long existingRoomId, @Valid RoomRequest request) {
		Long userId = securityService.getCurrentUserId();
		Room existingRoom = getRoomByIdFromUser(existingRoomId);
		if (!existingRoom.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to room with ID " + existingRoomId);
		}
		existingRoom.setName(request.getName());
		roomRepository.save(existingRoom);
		return existingRoom;
	}

	@Override
	public Map<String, String> deleteRoomForUser(Long roomId) {
		Long userId = securityService.getCurrentUserId();
		Room room = getRoomByIdFromUser(roomId);
		if (!room.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to room with ID " + roomId);
		}
		Map<String, String> response = Map.of(
				"message", "Successfully deleted SmartConsumer with ID " + roomId,
				"id", roomId.toString()
		);
		roomRepository.delete(room);
		return response;
	}

	@Override
	public Map<String, String> assignDeviceToRoomForUser(Long roomId, Long deviceId) {
		Long userId = securityService.getCurrentUserId();
		Room room = getRoomByIdFromUser(roomId);
		if (!room.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to room with ID " + roomId);
		}
		Device device = deviceService.getDeviceByIdFromUser(deviceId);
		room.addDevice(device);
		Map<String, String> response = Map.of(
				"message", "Successfully assigned device with ID " + deviceId + " to room with ID " + roomId,
				"id", roomId.toString()
		);
		roomRepository.save(room);
		return response;
	}

	@Override
	public Map<String, String> removeDeviceFromRoomForUser(Long roomId, Long deviceId) {
		Long userId = securityService.getCurrentUserId();
		Room room = getRoomByIdFromUser(roomId);
		if (!room.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to room with ID " + roomId);
		}
		Device device = deviceService.getDeviceByIdFromUser(deviceId);
		room.removeDevice(device);
		Map<String, String> response = Map.of(
				"message", "Successfully removed device with ID " + deviceId + " from room with ID " + roomId,
				"id", roomId.toString()
		);
		roomRepository.save(room);
		return response;
	}

	@Override
	public List<Room> getAllRooms() {
		return roomRepository.findAll();
	}

	@Override
	public Room getRoomById(Long roomId) {
		return roomRepository.findById(roomId).orElseThrow(() -> new DeviceNotFoundException("Room with ID " + roomId + " not found"));
	}
}
