package com.homebuilder.service;

import com.homebuilder.dto.ConsumerRequest;
import com.homebuilder.dto.ConsumerResponse;
import com.homebuilder.entity.Consumer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
public interface ConsumerService {

	Consumer createConsumer(ConsumerRequest request);
	List<Consumer> createConsumerList(List<ConsumerRequest> request);
	List<Consumer> getAllConsumers();
	List<Consumer> getAllUnarchivedConsumers();
	List<Consumer> getAllConsumersByOwner(Long ownerId);
	Page<ConsumerResponse> getAllConsumersByOwnerAndRoomIsNull(Pageable pageable);
	Page<ConsumerResponse> getAllConsumersByOwnerAndRoomId(Long roomId, Pageable pageable);
	Consumer getConsumerById(Long consumerId);
	Consumer updateConsumer(ConsumerRequest request);
	Map<String, String> setActive(Consumer consumer, boolean active);
	Map<String, String> archiveConsumer(Long consumerId);
	Map<String, String> deleteConsumer(Long consumerId);
	Map<String, String> deleteAllConsumersByOwnerId(Long ownerId);

	Page<ConsumerResponse> getAllUnarchivedByUser(Pageable pageable);

}
