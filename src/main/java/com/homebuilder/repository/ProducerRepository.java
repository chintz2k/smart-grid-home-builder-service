package com.homebuilder.repository;

import com.homebuilder.entity.Producer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author André Heinen
 */
public interface ProducerRepository extends JpaRepository<Producer, Long> {

	Optional<List<Producer>> findByUserId(Long userId);

}
