package com.homebuilder.repository;

import com.homebuilder.entity.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author André Heinen
 */
public interface ConsumerRepository extends JpaRepository<Consumer, Long> {

	Optional<List<Consumer>> findByUserId(Long userId);

}
