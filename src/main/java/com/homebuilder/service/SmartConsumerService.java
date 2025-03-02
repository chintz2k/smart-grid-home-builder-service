package com.homebuilder.service;

import com.homebuilder.dto.SmartConsumerRequest;
import com.homebuilder.entity.SmartConsumer;

import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
public interface SmartConsumerService {

	SmartConsumer createSmartConsumer(SmartConsumerRequest request);
	List<SmartConsumer> getAllSmartConsumers();
	List<SmartConsumer> getAllSmartConsumersByOwner(Long ownerId);
	SmartConsumer getSmartConsumerById(Long smartConsumerId);
	SmartConsumer updateSmartConsumer(SmartConsumerRequest request);
	Map<String, String> setActive(SmartConsumer smartConsumer, boolean active);
	Map<String, String> archiveSmartConsumer(Long smartConsumerId);
	Map<String, String> deleteSmartConsumer(Long smartConsumerId);

}
