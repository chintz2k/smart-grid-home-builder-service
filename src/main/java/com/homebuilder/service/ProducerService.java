package com.homebuilder.service;

import com.homebuilder.dto.ProducerRequest;
import com.homebuilder.entity.Producer;

import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
public interface ProducerService {

	Producer createProducer(ProducerRequest request);
	List<Producer> getAllProducers();
	List<Producer> getAllProducersByOwner(Long ownerId);
	Producer getProducerById(Long producerId);
	Producer updateProducer(ProducerRequest request);
	Map<String, String> setActive(Producer producer, boolean active);
	Map<String, String> archiveProducer(Long producerId);
	Map<String, String> deleteProducer(Long producerId);

}
