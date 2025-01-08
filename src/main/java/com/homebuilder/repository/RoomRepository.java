package com.homebuilder.repository;

import com.homebuilder.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author André Heinen
 */
public interface RoomRepository extends JpaRepository<Room, Long> {

	List<Room> findByUserId(Long userId);

}
