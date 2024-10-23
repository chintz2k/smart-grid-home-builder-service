package com.homebuilder.service;

import com.homebuilder.entity.Consumer;

import java.util.List;

/**
 * @author André Heinen
 */
public interface ConsumerService {

	// CRUD-Operationen für SH-Nutzer (basierend auf Benutzer-ID)
	Consumer createConsumerForUser(Consumer consumer, Long ownerId);
	List<Consumer> getAllConsumersForUser(Long ownerId);
	Consumer getConsumerByIdForUser(Long consumerId, Long ownerId);
	Consumer updateConsumerForUser(Long consumerId, Long ownerId, Consumer consumerDetails);
	void deleteConsumerForUser(Long consumerId, Long ownerId);

	// CRUD-Operationen für administrative Aufgaben (keine Einschränkung auf Benutzer-ID)
	List<Consumer> getAllConsumers();
	Consumer getConsumerById(Long consumerId);
}
