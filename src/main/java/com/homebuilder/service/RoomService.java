package com.homebuilder.service;

import com.homebuilder.dto.RoomRequest;
import com.homebuilder.entity.Room;

import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
public interface RoomService {

	Room createRoomForUser(RoomRequest request);
	List<Room> getAllRoomsFromUser();
	Room getRoomByIdFromUser(Long roomId);
	Room updateRoomForUser(Long roomId, RoomRequest request);
	Map<String, String> deleteRoomForUser(Long roomId);
	Map<String, String> assignDeviceToRoomForUser(Long roomId, Long deviceId);
	Map<String, String> removeDeviceFromRoomForUser(Long roomId, Long deviceId);

	List<Room> getAllRooms();
	Room getRoomById(Long roomId);

}
