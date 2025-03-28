package com.homebuilder.repository;

import com.homebuilder.entity.SmartConsumerProgram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author André Heinen
 */
public interface SmartConsumerProgramRepository extends JpaRepository<SmartConsumerProgram, Long> {

    List<SmartConsumerProgram> findByUserId(Long userId);

    Page<SmartConsumerProgram> findAllByArchivedFalse(Pageable pageable);

    List<SmartConsumerProgram> findAllByUserIdAndArchivedFalse(Long userId);

    Page<SmartConsumerProgram> findAllByUserId(Long userId, Pageable pageable);

    Page<SmartConsumerProgram> findAllByUserIdAndSmartConsumerId(Long userId, Long smartConsumerId, Pageable pageable);

}
