package com.homebuilder.service;

import com.homebuilder.dto.SmartConsumerTimeslotRequest;
import com.homebuilder.entity.SmartConsumerTimeslot;

import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
public interface SmartConsumerTimeslotService {

	SmartConsumerTimeslot createSmartConsumerTimeslot(SmartConsumerTimeslotRequest request);
	List<SmartConsumerTimeslot> getAllSmartConsumerTimeslots();
	List<SmartConsumerTimeslot> getAllSmartConsumerTimeslotsByOwner(Long ownerId);
	SmartConsumerTimeslot getSmartConsumerTimeslotById(Long smartConsumerTimeslotId);
	SmartConsumerTimeslot updateSmartConsumerTimeslot(SmartConsumerTimeslotRequest request);
	Map<String, String> deleteSmartConsumerTimeslot(Long smartConsumerTimeslotId);

}
