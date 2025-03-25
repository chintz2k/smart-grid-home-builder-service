package com.homebuilder.repository;

import com.homebuilder.entity.Consumer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author André Heinen
 */
public interface ConsumerRepository extends JpaRepository<Consumer, Long> {

	List<Consumer> findByUserId(Long userId);

	Page<Consumer> findAllByArchivedFalse(Pageable pageable);

	List<Consumer> findAllByUserIdAndArchivedFalse(Long userId);

	void deleteAllByUserId(Long userId);

	Page<Consumer> findByUserIdAndArchivedFalse(Long userId, Pageable pageable);

	Page<Consumer> findByUserIdAndRoomIsNull(Long userId, Pageable pageable);

}
