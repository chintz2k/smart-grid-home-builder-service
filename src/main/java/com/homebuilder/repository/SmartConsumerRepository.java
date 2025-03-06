package com.homebuilder.repository;

import com.homebuilder.entity.SmartConsumer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author André Heinen
 */
public interface SmartConsumerRepository extends JpaRepository<SmartConsumer, Long> {

    List<SmartConsumer> findByUserId(Long userId);

    Page<SmartConsumer> findAllByArchivedFalse(Pageable pageable);

    List<SmartConsumer> findAllByUserIdAndArchivedFalse(Long userId);

	void deleteAllByUserId(Long userId);
}
