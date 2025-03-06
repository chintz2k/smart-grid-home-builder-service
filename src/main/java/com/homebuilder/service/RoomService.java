package com.homebuilder.service;

import com.homebuilder.dto.RoomRequest;
import com.homebuilder.entity.Room;

import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
public interface RoomService {

	Room createRoom(RoomRequest request);
	List<Room> getAllRooms();
	List<Room> getAllRoomsByOwner(Long ownerId);
	Room getRoomById(Long roomId);
	Room updateRoom(RoomRequest request);
	Map<String, String> deleteRoom(Long roomId);
	Map<String, String> deleteAllRoomsByOwnerId(Long ownerId);
	Map<String, String> assignDeviceToRoom(Long roomId, Long deviceId);
	Map<String, String> removeDeviceFromRoom(Long roomId, Long deviceId);

}
