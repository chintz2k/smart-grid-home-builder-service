package com.homebuilder.service;

import com.homebuilder.entity.Producer;

import java.util.List;

/**
 * @author André Heinen
 */
public interface ProducerService {

	// CRUD-Operationen für SH-Nutzer (basierend auf Benutzer-ID)
	Producer createProducer(Producer producer, Long ownerId);
	List<Producer> getProducersForUser(Long ownerId);
	Producer getProducerForUser(Long producerId, Long ownerId);
	Producer updateProducerForUser(Long producerId, Long ownerId, Producer producerDetails);
	void deleteProducerForUser(Long producerId, Long ownerId);

	// CRUD-Operationen für administrative Aufgaben (keine Einschränkung auf Benutzer-ID)
	List<Producer> getAllProducers();
	Producer getProducerById(Long producerId);
}
