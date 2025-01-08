package com.homebuilder.repository;

import com.homebuilder.entity.SmartConsumerProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author André Heinen
 */
public interface SmartConsumerProgramRepository extends JpaRepository<SmartConsumerProgram, Long> {

    List<SmartConsumerProgram> findByUserId(Long userId);

}
