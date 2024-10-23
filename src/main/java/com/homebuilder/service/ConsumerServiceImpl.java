package com.homebuilder.service;

import com.homebuilder.entity.Consumer;
import com.homebuilder.exception.DeviceNotFoundException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.repository.ConsumerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author André Heinen
 */
@Service
public class ConsumerServiceImpl implements ConsumerService {

	private final ConsumerRepository consumerRepository;

	@Autowired
	public ConsumerServiceImpl(ConsumerRepository consumerRepository) {
		this.consumerRepository = consumerRepository;
	}

	// CRUD-Operationen für SH-Nutzer
	@Override
	public Consumer createConsumerForUser(Consumer consumer, Long ownerId) {
		consumer.setOwnerId(ownerId);
		return consumerRepository.save(consumer);
	}

	@Override
	public List<Consumer> getAllConsumersForUser(Long ownerId) {
		return consumerRepository.findByOwnerId(ownerId)
				.orElseThrow(() -> new DeviceNotFoundException("No Consumers found for User with id " + ownerId));
	}

	@Override
	public Consumer getConsumerByIdForUser(Long consumerId, Long ownerId) {
		Consumer consumer = consumerRepository.findById(consumerId)
				.orElseThrow(() -> new DeviceNotFoundException("Consumer with ID " + consumerId + " not found"));
		if (!consumer.getOwnerId().equals(ownerId)) {
			throw new UnauthorizedAccessException("Unauthorized access to consumer with ID " + consumerId);
		}
		return consumer;
	}

	@Override
	public Consumer updateConsumerForUser(Long consumerId, Long ownerId, Consumer consumerDetails) {
		Consumer consumer = consumerRepository.findById(consumerId)
				.orElseThrow(() -> new DeviceNotFoundException("Consumer with ID " + consumerId + " not found"));
		if (!consumer.getOwnerId().equals(ownerId)) {
			throw new UnauthorizedAccessException("Unauthorized access to consumer with ID " + consumerId);
		}
		consumer.setName(consumerDetails.getName());
		return consumerRepository.save(consumer);
	}

	@Override
	public void deleteConsumerForUser(Long consumerId, Long ownerId) {
		Consumer consumer = consumerRepository.findById(consumerId)
				.orElseThrow(() -> new DeviceNotFoundException("Consumer with ID " + consumerId + " not found"));
		if (!consumer.getOwnerId().equals(ownerId)) {
			throw new UnauthorizedAccessException("Unauthorized access to consumer with ID " + consumerId);
		}
		consumerRepository.delete(consumer);
	}

	// CRUD-Operationen für administrative Aufgaben
	@Override
	public List<Consumer> getAllConsumers() {
		return consumerRepository.findAll();
	}

	@Override
	public Consumer getConsumerById(Long consumerId) {
		return consumerRepository.findById(consumerId)
				.orElseThrow(() -> new DeviceNotFoundException("Consumer with ID " + consumerId + " not found"));
	}
}
