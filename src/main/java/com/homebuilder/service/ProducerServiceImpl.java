package com.homebuilder.service;

import com.homebuilder.entity.Producer;
import com.homebuilder.exception.DeviceNotFoundException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.repository.ProducerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author André Heinen
 */
@Service
public class ProducerServiceImpl implements ProducerService {

	private final ProducerRepository producerRepository;

	@Autowired
	public ProducerServiceImpl(ProducerRepository producerRepository) {
		this.producerRepository = producerRepository;
	}

	// CRUD-Operationen für SH-Nutzer
	@Override
	public Producer createProducer(Producer producer, Long ownerId) {
		producer.setOwnerId(ownerId);
		return producerRepository.save(producer);
	}

	@Override
	public List<Producer> getProducersForUser(Long ownerId) {
		return producerRepository.findByOwnerId(ownerId);
	}

	@Override
	public Producer getProducerForUser(Long producerId, Long ownerId) {
		Producer producer = producerRepository.findById(producerId)
				.orElseThrow(() -> new DeviceNotFoundException("Producer with ID " + producerId + " not found"));
		if (!producer.getOwnerId().equals(ownerId)) {
			throw new UnauthorizedAccessException("Unauthorized access to producer with ID " + producerId);
		}
		return producer;
	}

	@Override
	public Producer updateProducerForUser(Long producerId, Long ownerId, Producer producerDetails) {
		Producer producer = producerRepository.findById(producerId)
				.orElseThrow(() -> new DeviceNotFoundException("Producer with ID " + producerId + " not found"));
		if (!producer.getOwnerId().equals(ownerId)) {
			throw new UnauthorizedAccessException("Unauthorized access to producer with ID " + producerId);
		}
		producer.setName(producerDetails.getName());
		return producerRepository.save(producer);
	}

	@Override
	public void deleteProducerForUser(Long producerId, Long ownerId) {
		Producer producer = producerRepository.findById(producerId)
				.orElseThrow(() -> new DeviceNotFoundException("Producer with ID " + producerId + " not found"));
		if (!producer.getOwnerId().equals(ownerId)) {
			throw new UnauthorizedAccessException("Unauthorized access to producer with ID " + producerId);
		}
		producerRepository.delete(producer);
	}

	// CRUD-Operationen für administrative Aufgaben
	@Override
	public List<Producer> getAllProducers() {
		return producerRepository.findAll();
	}

	@Override
	public Producer getProducerById(Long producerId) {
		return producerRepository.findById(producerId)
				.orElseThrow(() -> new DeviceNotFoundException("Producer with ID " + producerId + " not found"));
	}
}
