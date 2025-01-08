package com.homebuilder.repository;

import com.homebuilder.entity.SmartConsumer;
import com.homebuilder.entity.SmartConsumerTimeslot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author André Heinen
 */
public interface SmartConsumerTimeslotRepository extends JpaRepository<SmartConsumerTimeslot, Long> {

    List<SmartConsumerTimeslot> findByUserId(Long userId);
    List<SmartConsumerTimeslot> findBySmartConsumerAndStartTimeLessThanAndEndTimeGreaterThan(SmartConsumer smartConsumer, LocalDateTime endTime, LocalDateTime startTime);

}
