package com.homebuilder.service;

import com.homebuilder.entity.Producer;

import java.util.List;

/**
 * @author André Heinen
 */
public interface ProducerService {

	// CRUD-Operationen für SH-Nutzer (basierend auf Benutzer-ID)
	Producer createProducerForUser(Producer producer, Long userId);
	List<Producer> getAllProducersFromUser(Long userId);
	Producer getProducerByIdFromUser(Long producerId, Long userId);
	Producer updateProducerForUser(Long producerId, Long userId, Producer producerDetails);
	void deleteProducerForUser(Long producerId, Long userId);

	// CRUD-Operationen für administrative Aufgaben (keine Einschränkung auf Benutzer-ID)
	List<Producer> getAllProducers();
	Producer getProducerById(Long producerId);
}
