package com.homebuilder.repository;

import com.homebuilder.entity.SmartConsumer;
import com.homebuilder.entity.SmartConsumerTimeslot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

/**
 * @author André Heinen
 */
public interface SmartConsumerTimeslotRepository extends JpaRepository<SmartConsumerTimeslot, Long> {

    List<SmartConsumerTimeslot> findByUserId(Long userId);

    List<SmartConsumerTimeslot> findBySmartConsumerAndStartTimeLessThanAndEndTimeGreaterThan(SmartConsumer smartConsumer, Instant end, Instant start);

	Page<SmartConsumerTimeslot> findAllByUserIdOrderByStartTimeAsc(Long userId, Pageable pageable);

	Page<SmartConsumerTimeslot> findAllByUserIdAndSmartConsumerIdOrderByStartTimeAsc(Long userId, Long smartConsumerId, Pageable pageable);

}
