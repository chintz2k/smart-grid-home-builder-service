package com.homebuilder.service;

import com.homebuilder.entity.Room;

import java.util.List;

/**
 * @author André Heinen
 */
public interface RoomService {

	// CRUD-Operationen für SH-Nutzer (basierend auf Benutzer-ID)
	Room createRoomForUser(Room room, Long userId);
	List<Room> getAllRoomsFromUser(Long userId);
	Room getRoomByIdFromUser(Long roomId, Long userId);
	Room updateRoomForUser(Long roomId, Long userId, Room roomDetails);
	void deleteRoomForUser(Long roomId, Long userId);

	// CRUD-Operationen für administrative Aufgaben (keine Einschränkung auf Benutzer-ID)
	List<Room> getAllRooms();
	Room getRoomById(Long roomId);

}
