package com.homebuilder.service;

import com.homebuilder.dto.ProducerRequest;
import com.homebuilder.entity.Producer;
import com.homebuilder.exception.DeviceNotFoundException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.repository.ProducerRepository;
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
public class ProducerServiceImpl implements ProducerService {

	private final ProducerRepository producerRepository;

	private final SecurityService securityService;

	@Autowired
	public ProducerServiceImpl(ProducerRepository producerRepository, SecurityService securityService) {
		this.producerRepository = producerRepository;
		this.securityService = securityService;
	}

	@Override
	public Producer createProducerForUser(@Valid ProducerRequest request) {
		Long userId = securityService.getCurrentUserId();
		Producer producer = request.toEntity();
		producer.setUserId(userId);
		producerRepository.save(producer);
		return producer;
	}

	@Override
	public List<Producer> getAllProducersFromUser() {
		Long userId = securityService.getCurrentUserId();
		List<Producer> producerList = producerRepository.findByUserId(userId);
		if (producerList.isEmpty()) {
			throw new DeviceNotFoundException("No Producers found for User with ID " + userId);
		}
		return producerList;
	}

	@Override
	public Producer getProducerByIdFromUser(Long producerId) {
		Long userId = securityService.getCurrentUserId();
		Producer producer = producerRepository.findById(producerId).orElseThrow(() -> new DeviceNotFoundException("Producer with ID " + producerId + " not found"));
		if (!producer.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to producer with ID " + producerId);
		}
		return producer;
	}

	@Override
	public Producer updateProducerForUser(Long existingProducerId, @Valid ProducerRequest request) {
		Long userId = securityService.getCurrentUserId();
		Producer existingProducer = getProducerByIdFromUser(existingProducerId);
		if (!existingProducer.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to producer with ID " + existingProducerId);
		}
		existingProducer.setName(request.getName());
		existingProducer.setActive(request.isActive());
		existingProducer.setArchived(request.isArchived());
		existingProducer.setPowerProduction(request.getPowerProduction());
		producerRepository.save(existingProducer);
		return existingProducer;
	}

	@Override
	public Map<String, String> deleteProducerForUser(Long producerId) {
		Long userId = securityService.getCurrentUserId();
		Producer producer = getProducerByIdFromUser(producerId);
		if (!producer.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to producer with ID " + producerId);
		}
		Map<String, String> response = Map.of(
				"message", "Successfully deleted Producer with ID " + producerId,
				"id", producerId.toString()
		);
		producerRepository.delete(producer);
		return response;
	}

	@Override
	public List<Producer> getAllProducers() {
		return producerRepository.findAll();
	}

	@Override
	public Producer getProducerById(Long producerId) {
		return producerRepository.findById(producerId).orElseThrow(() -> new DeviceNotFoundException("Producer with ID " + producerId + " not found"));
	}
}
