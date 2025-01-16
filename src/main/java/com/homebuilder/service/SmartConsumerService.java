package com.homebuilder.service;

import com.homebuilder.dto.SmartConsumerRequest;
import com.homebuilder.entity.SmartConsumer;

import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
public interface SmartConsumerService {

	SmartConsumer createSmartConsumerForUser(SmartConsumerRequest request);
	List<SmartConsumer> getAllSmartConsumersFromUser();
	SmartConsumer getSmartConsumerByIdFromUser(Long smartConsumerId);
	SmartConsumer updateSmartConsumerForUser(Long existingConsumerId, SmartConsumerRequest request);
	Map<String, String> deleteSmartConsumerForUser(Long smartConsumerId);

	List<SmartConsumer> getAllSmartConsumers();
	SmartConsumer getSmartConsumerById(Long smartConsumerId);

}
