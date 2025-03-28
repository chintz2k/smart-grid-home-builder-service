package com.homebuilder.service;

import com.homebuilder.dto.ProducerRequest;
import com.homebuilder.dto.ProducerResponse;
import com.homebuilder.entity.Producer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author André Heinen
 */
public interface ProducerService {

	Producer createProducer(ProducerRequest request);
	List<Producer> createProducerList(List<ProducerRequest> request);
	List<Producer> getAllProducers();
	List<Producer> getAllUnarchivedProducers();
	List<Producer> getAllProducersByOwner(Long ownerId);
	Page<ProducerResponse> getAllProducersByOwnerAndRoomIsNull(Pageable pageable);
	Page<ProducerResponse> getAllProducersByOwnerAndRoomId(Long roomId, Pageable pageable);
	Producer getProducerById(Long producerId);
	Producer updateProducer(ProducerRequest request);
	Map<String, String> setActive(Producer producer, boolean active, boolean sendEvent);
	Map<String, String> setActiveByListAndNoSendEvent(Set<Long> idSet, boolean active);
	Map<String, String> archiveProducer(Long producerId);
	Map<String, String> deleteProducer(Long producerId);
	Map<String, String> deleteAllProducersByOwnerId(Long ownerId);

	Page<ProducerResponse> getAllUnarchivedByUser(Pageable pageable);

}
