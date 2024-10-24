package com.homebuilder.repository;

import com.homebuilder.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author André Heinen
 */
public interface RoomRepository extends JpaRepository<Room, Long> {

	Optional<List<Room>> findByUserId(Long userId);

}
