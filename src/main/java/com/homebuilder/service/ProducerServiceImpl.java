package com.homebuilder.service;

import com.homebuilder.dto.ProducerRequest;
import com.homebuilder.dto.ProducerResponse;
import com.homebuilder.entity.Producer;
import com.homebuilder.entity.Room;
import com.homebuilder.exception.CreateDeviceFailedException;
import com.homebuilder.exception.DeviceNotFoundException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.repository.ProducerRepository;
import com.homebuilder.repository.RoomRepository;
import com.homebuilder.security.SecurityService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * @author André Heinen
 */
@Service
public class ProducerServiceImpl implements ProducerService {

	private static final Logger logger = LoggerFactory.getLogger(ProducerServiceImpl.class);

	private final ProducerRepository producerRepository;

	private final SecurityService securityService;

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final RoomRepository roomRepository;

	@Autowired
	public ProducerServiceImpl(ProducerRepository producerRepository, SecurityService securityService, KafkaTemplate<String, String> kafkaTemplate, RoomRepository roomRepository) {
		this.producerRepository = producerRepository;
		this.securityService = securityService;
		this.kafkaTemplate = kafkaTemplate;
		this.roomRepository = roomRepository;
	}

	@Override
	@Transactional
	public Producer createProducer(@Valid ProducerRequest request) {
		Producer producer = request.toEntity();
		if (securityService.isCurrentUserAdminOrSystem()) {
			if (request.getOwnerId() == null) {
				throw new CreateDeviceFailedException("Owner ID must be provided when creating Producer as System User");
			} else {
				producer.setUserId(request.getOwnerId());
			}
		} else {
			producer.setUserId(securityService.getCurrentUserId());
		}
		producerRepository.save(producer);
		Long roomId = request.getRoomId();
		if (roomId != null) {
			Room room = roomRepository.findById(roomId).orElse(null);
			if (room != null) {
				if (securityService.getCurrentUserId().equals(room.getUserId())) {
					room.addDevice(producer);
					producer.setRoom(room);
					roomRepository.save(room);
				} else {
					throw new UnauthorizedAccessException("Unauthorized access to Room with ID " + roomId);
				}
			}
		}
		return producer;
	}

	@Override
	@Transactional
	public List<Producer> createProducerList(List<@Valid ProducerRequest> request) {
		List<Producer> producerList = request.stream().map(ProducerRequest::toEntity).toList();
		if (securityService.isCurrentUserAdminOrSystem()) {
			for (ProducerRequest producerRequest : request) {
				if (producerRequest.getOwnerId() == null) {
					throw new CreateDeviceFailedException("Owner ID must be provided when creating Producer as System User");
				} else {
					producerList.forEach(producer -> producer.setUserId(producerRequest.getOwnerId()));
				}
			}
		} else {
			producerList.forEach(producer -> producer.setUserId(securityService.getCurrentUserId()));
		}
		producerRepository.saveAll(producerList);
		return producerList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producer> getAllProducers() {
		if (securityService.isCurrentUserAdminOrSystem()) {
			return producerRepository.findAll(PageRequest.of(0, 1000)).getContent();
		}
		Long userId = securityService.getCurrentUserId();
		return producerRepository.findByUserId(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producer> getAllUnarchivedProducers() {
		if (securityService.isCurrentUserAdminOrSystem()) {
			return producerRepository.findAllByArchivedFalse(PageRequest.of(0, 1000)).getContent();
		}
		Long userId = securityService.getCurrentUserId();
		return producerRepository.findAllByUserIdAndArchivedFalse(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producer> getAllProducersByOwner(Long ownerId) {
		if (securityService.isCurrentUserAdminOrSystem()) {
			return producerRepository.findByUserId(ownerId);
		} else {
			throw new UnauthorizedAccessException("Unauthorized access to Producers for Owner with ID " + ownerId);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProducerResponse> getAllProducersByOwnerAndRoomIsNull(Pageable pageable) {
		Long userId = securityService.getCurrentUserId();
		Page<Producer> producerPage = producerRepository.findByUserIdAndRoomIsNull(userId, pageable);
		return producerPage.map(ProducerResponse::new);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProducerResponse> getAllProducersByOwnerAndRoomId(Long roomId, Pageable pageable) {
		Long userId = securityService.getCurrentUserId();
		Page<Producer> list = producerRepository.findByRoomId(roomId, pageable);
		if (!list.isEmpty()) {
			if (!Objects.equals(list.getContent().getFirst().getUserId(), userId)) {
				throw new UnauthorizedAccessException("Unauthorized access to Producers for Room with ID " + roomId);
			}
		}
		return list.map(ProducerResponse::new);
	}

	@Override
	@Transactional(readOnly = true)
	public Producer getProducerById(Long producerId) {
		Producer producer = producerRepository.findById(producerId).orElse(null);
		if (producer != null) {
			if (securityService.canAccessDevice(producer)) {
				return producer;
			} else {
				throw new UnauthorizedAccessException("Unauthorized access to Producer with ID " + producerId);
			}
		}
		return null;
	}

	@Override
	@Transactional
	public Producer updateProducer(@Valid ProducerRequest request) {
		if (request.getId() == null) {
			throw new CreateDeviceFailedException("Producer ID must be provided when updating Producer");
		}
		Producer producer = producerRepository.findById(request.getId()).orElse(null);
		if (producer != null) {
			if (securityService.canAccessDevice(producer)) {
				boolean changed = false;
				if (!Objects.equals(request.getName(), producer.getName())) {
					producer.setName(request.getName());
					changed = true;
				}
				if (request.getPowerProduction() != producer.getPowerProduction()) {
					if (producer.isActive()) {
						setActive(producer, false);
					}
					producer.setPowerProduction(request.getPowerProduction());
					changed = true;
				}
				if (changed) {
					producerRepository.save(producer);
					return producer;
				}
				throw new CreateDeviceFailedException("No changes detected in Producer");
			}
			throw new UnauthorizedAccessException("Unauthorized access to Producer with ID " + request.getId());
		}
		return null;
	}

	@Override
	@Transactional
	public Map<String, String> setActive(Producer producer, boolean active) {
		if (producer != null) {
			if (securityService.canAccessDevice(producer)) {
				if (producer.isArchived()) {
					throw new CreateDeviceFailedException("Producer with ID " + producer.getId() + " is archived and cannot be activated or deactivated");
				}
				if (producer.isActive() && active) {
					throw new CreateDeviceFailedException("Producer with ID " + producer.getId() + " is already active");
				}
				if (!producer.isActive() && !active) {
					throw new CreateDeviceFailedException("Producer with ID " + producer.getId() + " is already inactive");
				}
				producer.setActive(active);
				producerRepository.save(producer);
				String event = String.format(
						Locale.ENGLISH,
						"{\"deviceId\": %d, \"ownerId\": %d, \"commercial\": %b, \"active\": %b, \"powerType\": \"%s\", \"renewable\": %b, \"powerProduction\": %f, \"timestamp\": \"%s\"}",
						producer.getId(), producer.getUserId(), securityService.isCurrentUserACommercialUser(), active, producer.getPowerType(), producer.isRenewable(), producer.getPowerProduction(), Instant.now());
				kafkaTemplate.send("producer-events", event).whenComplete((result, exception) -> {
					if (exception != null) {
						logger.error("Fehler beim Senden des Events: {}", exception.getMessage());
					}
				});
				return Map.of(
						"message", "Successfully updated Producer with ID " + producer.getId(),
						"id", producer.getId().toString(),
						"active", producer.isActive() ? "true" : "false"
				);
			}
		}
		throw new DeviceNotFoundException("Producer not found");
	}

	@Override
	@Transactional
	public Map<String, String> archiveProducer(Long producerId) {
		Producer producer = producerRepository.findById(producerId).orElse(null);
		if (producer != null) {
			if (securityService.canAccessDevice(producer)) {
				if (!producer.isArchived()) {
					producer.setArchived(true);
					if (producer.isActive()) {
						setActive(producer, false);
					}
					Map<String, String> response = Map.of(
							"message", "Successfully archived Producer with ID " + producerId,
							"id", producerId.toString()
					);
					producerRepository.save(producer);
					return response;
				} else {
					throw new CreateDeviceFailedException("Producer with ID " + producerId + " is already archived");
				}
			}
			throw new UnauthorizedAccessException("Unauthorized access to Producer with ID " + producerId);
		}
		throw new DeviceNotFoundException("Producer with ID " + producerId + " not found");
	}

	@Override
	@Transactional
	public Map<String, String> deleteProducer(Long producerId) {
		Producer producer = producerRepository.findById(producerId).orElse(null);
		if (producer != null) {
			if (securityService.canAccessDevice(producer)) {
				producer.getRoom().removeDevice(producer);
				producer.setRoom(null);
				Map<String, String> response = Map.of(
						"message", "Successfully deleted Producer with ID " + producerId,
						"id", producerId.toString()
				);
				producerRepository.deleteById(producerId);
				return response;
			}
			throw new UnauthorizedAccessException("Unauthorized access to Producer with ID " + producerId);
		}
		return null;
	}

	@Override
	@Transactional
	public Map<String, String> deleteAllProducersByOwnerId(Long ownerId) {
		if (securityService.isCurrentUserAdminOrSystem()) {
			producerRepository.deleteAllByUserId(ownerId);
		} else {
			throw new UnauthorizedAccessException("Unauthorized access to delete all Producers for Owner with ID " + ownerId);
		}
		return Map.of(
				"message", "Successfully deleted all Producers for Owner with ID " + ownerId
		);
	}

	@Override
	public Page<ProducerResponse> getAllUnarchivedByUser(Pageable pageable) {
		Long userId = securityService.getCurrentUserId();
		Page<Producer> producerPage = producerRepository.findByUserIdAndArchivedFalse(userId, pageable);
		return producerPage.map(ProducerResponse::new);
	}
}
