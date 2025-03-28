package com.homebuilder.service;

import com.homebuilder.dto.SmartConsumerTimeslotRequest;
import com.homebuilder.entity.SmartConsumerTimeslot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
public interface SmartConsumerTimeslotService {

	SmartConsumerTimeslot createSmartConsumerTimeslot(SmartConsumerTimeslotRequest request);
	List<SmartConsumerTimeslot> getAllSmartConsumerTimeslots();
	List<SmartConsumerTimeslot> getAllSmartConsumerTimeslotsByOwner(Long ownerId);
	Page<SmartConsumerTimeslot> getAllByUserId(Long userId, Pageable pageable);
	Page<SmartConsumerTimeslot> getAllByUserIdAndByConsumerId(Long userId, Long consumerId, Pageable pageable);
	SmartConsumerTimeslot getSmartConsumerTimeslotById(Long smartConsumerTimeslotId);
	SmartConsumerTimeslot updateSmartConsumerTimeslot(SmartConsumerTimeslotRequest request);
	Map<String, String> deleteSmartConsumerTimeslot(Long smartConsumerTimeslotId);

}
