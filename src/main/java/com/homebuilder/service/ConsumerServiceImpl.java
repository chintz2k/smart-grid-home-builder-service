package com.homebuilder.service;

import com.homebuilder.dto.ConsumerRequest;
import com.homebuilder.entity.Consumer;
import com.homebuilder.exception.DeviceNotFoundException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.repository.ConsumerRepository;
import com.homebuilder.security.SecurityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
@Service
public class ConsumerServiceImpl implements ConsumerService {

	private final ConsumerRepository consumerRepository;

	private final SecurityService securityService;

	@Autowired
	public ConsumerServiceImpl(ConsumerRepository consumerRepository, SecurityService securityService) {
		this.consumerRepository = consumerRepository;
		this.securityService = securityService;
	}

	@Override
	public Consumer createConsumerForUser(@Valid ConsumerRequest request) {
		Long userId = securityService.getCurrentUserId();
		Consumer consumer = request.toEntity();
		consumer.setUserId(userId);
		consumerRepository.save(consumer);
		return consumer;
	}

	@Override
	public List<Consumer> getAllConsumersFromUser() {
		Long userId = securityService.getCurrentUserId();
		List<Consumer> consumerList = consumerRepository.findByUserId(userId);
		if (consumerList.isEmpty()) {
			throw new DeviceNotFoundException("No Consumers found for User with ID " + userId);
		}
		return consumerList;
	}

	@Override
	public Consumer getConsumerByIdFromUser(Long consumerId) {
		Long userId = securityService.getCurrentUserId();
		Consumer consumer = consumerRepository.findById(consumerId).orElseThrow(() -> new DeviceNotFoundException("Consumer with ID " + consumerId + " not found"));
		if (!consumer.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to consumer with ID " + consumerId);
		}
		return consumer;
	}

	@Override
	public Consumer updateConsumerForUser(Long existingConsumerId, @Valid ConsumerRequest request) {
		Long userId = securityService.getCurrentUserId();
		Consumer existingConsumer = getConsumerByIdFromUser(existingConsumerId);
		if (!existingConsumer.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to consumer with ID " + existingConsumerId);
		}
		existingConsumer.setName(request.getName());
		existingConsumer.setActive(request.isActive());
		existingConsumer.setArchived(request.isArchived());
		existingConsumer.setPowerConsumption(request.getPowerConsumption());
		consumerRepository.save(existingConsumer);
		return existingConsumer;
	}

	@Override
	public Map<String, String> deleteConsumerForUser(Long consumerId) {
		Long userId = securityService.getCurrentUserId();
		Consumer consumer = getConsumerByIdFromUser(consumerId);
		if (!consumer.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to consumer with ID " + consumerId);
		}
		Map<String, String> response = Map.of(
				"message", "Successfully deleted Consumer with ID " + consumerId,
				"id", consumerId.toString()
		);
		consumerRepository.delete(consumer);
		return response;
	}

	@Override
	public List<Consumer> getAllConsumers() {
		return consumerRepository.findAll();
	}

	@Override
	public Consumer getConsumerById(Long consumerId) {
		return consumerRepository.findById(consumerId).orElseThrow(() -> new DeviceNotFoundException("Consumer with ID " + consumerId + " not found"));
	}

	@Override
	public Consumer updateConsumer(Long consumerId, @Valid Consumer request) {
		Consumer consumer = getConsumerById(consumerId);
		consumer.setName(request.getName());
		consumer.setActive(request.isActive());
		consumer.setArchived(request.isArchived());
		consumer.setPowerConsumption(request.getPowerConsumption());
		consumerRepository.save(consumer);
		return consumer;
	}
}
