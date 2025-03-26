package com.homebuilder.service;

import com.homebuilder.dto.RoomRequest;
import com.homebuilder.entity.Device;
import com.homebuilder.entity.Room;
import com.homebuilder.exception.CreateDeviceFailedException;
import com.homebuilder.exception.DeviceNotFoundException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.repository.RoomRepository;
import com.homebuilder.security.SecurityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
	@Transactional
	public void saveRoom(Room room) {
		roomRepository.save(room);
	}

	@Override
	@Transactional
	public Room createRoom(@Valid RoomRequest request) {
		Room room = request.toEntity();
		if (securityService.isCurrentUserAdminOrSystem()) {
			if (request.getOwnerId() == null) {
				throw new CreateDeviceFailedException("Owner ID must be provided when creating Room as System User");
			} else {
				room.setUserId(request.getOwnerId());
			}
		} else {
			room.setUserId(securityService.getCurrentUserId());
		}
		roomRepository.save(room);
		return room;
	}

	@Override
	@Transactional
	public List<Room> createRoomList(List<@Valid RoomRequest> request) {
		List<Room> roomList = request.stream().map(RoomRequest::toEntity).toList();
		if (securityService.isCurrentUserAdminOrSystem()) {
			for (RoomRequest roomRequest : request) {
				if (roomRequest.getOwnerId() == null) {
					throw new CreateDeviceFailedException("Owner ID must be provided when creating Rooms as System User");
				} else {
					roomList.forEach(room -> room.setUserId(roomRequest.getOwnerId()));
				}
			}
		} else {
			roomList.forEach(room -> room.setUserId(securityService.getCurrentUserId()));
		}
		roomRepository.saveAll(roomList);
		return roomList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Room> getAllRooms() {
		if (securityService.isCurrentUserAdminOrSystem()) {
			return roomRepository.findAll(PageRequest.of(0, 1000)).getContent();
		}
		Long userId = securityService.getCurrentUserId();
		return roomRepository.findByUserId(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Room> getAllRoomsByOwner(Long ownerId) {
		if (securityService.isCurrentUserAdminOrSystem()) {
			return roomRepository.findByUserId(ownerId);
		} else {
			throw new UnauthorizedAccessException("Unauthorized access to Rooms for Owner with ID " + ownerId);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Room getRoomById(Long roomId) {
		Room room = roomRepository.findById(roomId).orElse(null);
		if (room != null) {
			if (securityService.canAccessRoom(room)) {
				return room;
			} else {
				throw new UnauthorizedAccessException("Unauthorized access to room with ID " + roomId);
			}
		}
		return null;
	}

	@Override
	@Transactional
	public Room updateRoom(@Valid RoomRequest request) {
		if (request.getId() == null) {
			throw new CreateDeviceFailedException("Room ID must be provided when updating Room");
		}
		Room room = roomRepository.findById(request.getId()).orElse(null);
		if (room != null) {
			if (securityService.canAccessRoom(room)) {
				if (!Objects.equals(request.getName(), room.getName())) {
					room.setName(request.getName());
					roomRepository.save(room);
					return room;
				}
				throw new CreateDeviceFailedException("No changes detected in Room");
			}
			throw new UnauthorizedAccessException("Unauthorized access to room with ID " + request.getId());
		}
		return null;
	}

	@Override
	@Transactional
	public Map<String, String> deleteRoom(Long roomId) {
		Room room = roomRepository.findById(roomId).orElse(null);
		if (room != null) {
			if (securityService.canAccessRoom(room)) {
				for (Device device : room.getDevices()) {
					device.setRoom(null);
				}
				room.setDevices(null);
				Map<String, String> response = Map.of(
						"message", "Successfully deleted room with ID " + roomId,
						"id", roomId.toString()
				);
				roomRepository.deleteById(roomId);
				return response;
			}
			throw new UnauthorizedAccessException("Unauthorized access to room with ID " + roomId);
		}
		return null;
	}

	@Override
	@Transactional
	public Map<String, String> deleteAllRoomsByOwnerId(Long ownerId) {
		if (securityService.isCurrentUserAdminOrSystem()) {
			roomRepository.deleteAllByUserId(ownerId);
		} else {
			throw new UnauthorizedAccessException("Unauthorized access to delete all Rooms for Owner with ID " + ownerId);
		}
		return Map.of(
				"message", "Successfully deleted all Rooms for Owner with ID " + ownerId,
				"id", ownerId.toString()
		);
	}

	@Override
	@Transactional
	public Map<String, String> assignDeviceToRoom(Long roomId, Long deviceId) {
		Room room = roomRepository.findById(roomId).orElse(null);
		if (room != null) {
			if (securityService.canAccessRoom(room)) {
				Device device = deviceService.getDeviceById(deviceId);
				if (device == null) {
					throw new DeviceNotFoundException("Device with ID " + deviceId + " not found");
				}
				if (room.getDevices().contains(device)) {
					throw new CreateDeviceFailedException("Device with ID " + deviceId + " is already assigned to room with ID " + roomId);
				}
				room.addDevice(device);
				roomRepository.save(room);
				return Map.of(
						"message", "Successfully assigned device with ID " + deviceId + " to room with ID " + roomId,
						"id", roomId.toString()
				);
			}
			throw new UnauthorizedAccessException("Unauthorized access to room with ID " + roomId);
		}
		throw new DeviceNotFoundException("Room with ID " + roomId + " not found");
	}

	@Override
	@Transactional
	public Map<String, String> removeDeviceFromRoom(Long roomId, Long deviceId) {
		Room room = roomRepository.findById(roomId).orElse(null);
		if (room != null) {
			if (securityService.canAccessRoom(room)) {
				Device device = deviceService.getDeviceById(deviceId);
				if (device == null) {
					throw new DeviceNotFoundException("Device with ID " + deviceId + " not found");
				}
				if (!room.getDevices().contains(device)) {
					throw new CreateDeviceFailedException("Device with ID " + deviceId + " is not assigned to room with ID " + roomId);
				}
				room.removeDevice(device);
				roomRepository.save(room);
				return Map.of(
						"message", "Successfully removed device with ID " + deviceId + " from room with ID " + roomId,
						"id", roomId.toString()
				);
			}
			throw new UnauthorizedAccessException("Unauthorized access to room with ID " + roomId);
		}
		throw new DeviceNotFoundException("Room with ID " + roomId + " not found");
	}
}
