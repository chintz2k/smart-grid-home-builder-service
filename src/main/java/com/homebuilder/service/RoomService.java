package com.homebuilder.service;

import com.homebuilder.entity.Room;

import java.util.List;

/**
 * @author André Heinen
 */
public interface RoomService {

	// CRUD-Operationen für SH-Nutzer (basierend auf Benutzer-ID)
	Room createRoom(Room room, Long ownerId);
	List<Room> getRoomsForUser(Long ownerId);
	Room getRoomForUser(Long roomId, Long ownerId);
	Room updateRoomForUser(Long roomId, Long ownerId, Room roomDetails);
	void deleteRoomForUser(Long roomId, Long ownerId);

	// CRUD-Operationen für administrative Aufgaben (keine Einschränkung auf Benutzer-ID)
	List<Room> getAllRooms();
	Room getRoomById(Long roomId);

}
