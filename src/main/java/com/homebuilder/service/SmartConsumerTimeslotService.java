package com.homebuilder.service;

import com.homebuilder.dto.SmartConsumerTimeslotRequest;
import com.homebuilder.entity.SmartConsumerTimeslot;

import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
public interface SmartConsumerTimeslotService {

	SmartConsumerTimeslot createSmartConsumerTimeslotForUser(SmartConsumerTimeslotRequest request);
	List<SmartConsumerTimeslot> getAllSmartConsumerTimeslotsFromUser();
	SmartConsumerTimeslot getSmartConsumerTimeslotByIdFromUser(Long smartConsumerTimeslotId);
	SmartConsumerTimeslot updateSmartConsumerTimeslotForUser(Long smartConsumerTimeslotId, SmartConsumerTimeslotRequest request);
	Map<String, String> deleteSmartConsumerTimeslotForUser(Long smartConsumerTimeslotId);

	List<SmartConsumerTimeslot> getAllSmartConsumerTimeslots();
	SmartConsumerTimeslot getSmartConsumerTimeslotById(Long smartConsumerTimeslotId);
}
