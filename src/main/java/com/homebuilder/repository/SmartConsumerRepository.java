package com.homebuilder.repository;

import com.homebuilder.entity.SmartConsumer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author André Heinen
 */
public interface SmartConsumerRepository extends JpaRepository<SmartConsumer, Long> {

    List<SmartConsumer> findByUserId(Long userId);

}
