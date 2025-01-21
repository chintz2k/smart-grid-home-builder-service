package com.homebuilder.service;

import com.homebuilder.dto.ProducerRequest;
import com.homebuilder.entity.Producer;

import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
public interface ProducerService {

	Producer createProducerForUser(ProducerRequest request);
	List<Producer> getAllProducersFromUser();
	Producer getProducerByIdFromUser(Long producerId);
	Producer updateProducerForUser(Long producerId, ProducerRequest request);
	Map<String, String> archiveProducerForUser(Long producerId);
	Map<String, String> deleteProducerForUser(Long producerId);

	List<Producer> getAllProducers();
	Producer getProducerById(Long producerId);
	Producer updateProducer(Long producerId, Producer request);

}
