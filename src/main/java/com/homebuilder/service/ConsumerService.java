package com.homebuilder.service;

import com.homebuilder.dto.ConsumerRequest;
import com.homebuilder.entity.Consumer;

import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
public interface ConsumerService {

	Consumer createConsumer(ConsumerRequest request);
	List<Consumer> getAllConsumers();
	List<Consumer> getAllConsumersByOwner(Long ownerId);
	Consumer getConsumerById(Long consumerId);
	Consumer updateConsumer(ConsumerRequest request);
	Map<String, String> setActive(Consumer consumer, boolean active);
	Map<String, String> archiveConsumer(Long consumerId);
	Map<String, String> deleteConsumer(Long consumerId);

}
