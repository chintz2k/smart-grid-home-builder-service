package com.homebuilder.service;

import com.homebuilder.dto.ConsumerRequest;
import com.homebuilder.entity.Consumer;

import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
public interface ConsumerService {

	Consumer createConsumerForUser(ConsumerRequest request);
	List<Consumer> getAllConsumersFromUser();
	Consumer getConsumerByIdFromUser(Long consumerId);
	Consumer updateConsumerForUser(Long consumerId, ConsumerRequest request);
	Map<String, String> deleteConsumerForUser(Long consumerId);

	List<Consumer> getAllConsumers();
	Consumer getConsumerById(Long consumerId);
	Consumer updateConsumer(Long consumerId, Consumer request);

}
