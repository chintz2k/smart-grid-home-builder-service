package com.homebuilder.repository;

import com.homebuilder.entity.Producer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author André Heinen
 */
public interface ProducerRepository extends JpaRepository<Producer, Long> {

	List<Producer> findByUserId(Long userId);

}
