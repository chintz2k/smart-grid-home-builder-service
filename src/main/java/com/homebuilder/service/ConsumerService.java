package com.homebuilder.service;

import com.homebuilder.entity.Consumer;

import java.util.List;

/**
 * @author André Heinen
 */
public interface ConsumerService {

	// CRUD-Operationen für SH-Nutzer (basierend auf Benutzer-ID)
	Consumer createConsumerForUser(Consumer consumer, Long userId);
	List<Consumer> getAllConsumersFromUser(Long userId);
	Consumer getConsumerByIdFromUser(Long consumerId, Long userId);
	Consumer updateConsumerForUser(Long consumerId, Long userId, Consumer consumerDetails);
	void deleteConsumerForUser(Long consumerId, Long userId);

	// CRUD-Operationen für administrative Aufgaben (keine Einschränkung auf Benutzer-ID)
	List<Consumer> getAllConsumers();
	Consumer getConsumerById(Long consumerId);
}
