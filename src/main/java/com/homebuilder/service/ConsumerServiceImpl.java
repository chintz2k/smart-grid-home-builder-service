package com.homebuilder.service;

import com.homebuilder.dto.ConsumerRequest;
import com.homebuilder.dto.ConsumerResponse;
import com.homebuilder.entity.Consumer;
import com.homebuilder.entity.Room;
import com.homebuilder.exception.CreateDeviceFailedException;
import com.homebuilder.exception.DeviceNotFoundException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.repository.ConsumerRepository;
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
public class ConsumerServiceImpl implements ConsumerService {

	private static final Logger logger = LoggerFactory.getLogger(ConsumerServiceImpl.class);

	private final ConsumerRepository consumerRepository;

	private final SecurityService securityService;

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final RoomRepository roomRepository;

	@Autowired
	public ConsumerServiceImpl(ConsumerRepository consumerRepository, SecurityService securityService, KafkaTemplate<String, String> kafkaTemplate, RoomRepository roomRepository) {
		this.consumerRepository = consumerRepository;
		this.securityService = securityService;
		this.kafkaTemplate = kafkaTemplate;
		this.roomRepository = roomRepository;
	}

	@Override
	@Transactional
	public Consumer createConsumer(@Valid ConsumerRequest request) {
		Consumer consumer = request.toEntity();
		if (securityService.isCurrentUserAdminOrSystem()) {
			if (request.getOwnerId() == null) {
				throw new CreateDeviceFailedException("Owner ID must be provided when creating Consumer as System User");
			} else {
				consumer.setUserId(request.getOwnerId());
			}
		} else {
			consumer.setUserId(securityService.getCurrentUserId());
		}
		consumerRepository.save(consumer);
		Long roomId = request.getRoomId();
		if (roomId != null) {
			Room room = roomRepository.findById(roomId).orElse(null);
			if (room != null) {
				if (securityService.getCurrentUserId().equals(room.getUserId())) {
					room.addDevice(consumer);
					consumer.setRoom(room);
					roomRepository.save(room);
				} else {
					throw new UnauthorizedAccessException("Unauthorized access to Room with ID " + roomId);
				}
			}
		}
		return consumer;
	}

	@Override
	@Transactional
	public List<Consumer> createConsumerList(List<@Valid ConsumerRequest> request) {
		List<Consumer> consumerList = request.stream().map(ConsumerRequest::toEntity).toList();
		if (securityService.isCurrentUserAdminOrSystem()) {
			for (ConsumerRequest consumerRequest : request) {
				if (consumerRequest.getOwnerId() == null) {
					throw new CreateDeviceFailedException("Owner ID must be provided when creating Consumer as System User");
				} else {
					consumerList.forEach(consumer -> consumer.setUserId(consumerRequest.getOwnerId()));
				}
			}
		} else {
			consumerList.forEach(consumer -> consumer.setUserId(securityService.getCurrentUserId()));
		}
		consumerRepository.saveAll(consumerList);
		return consumerList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Consumer> getAllConsumers() {
		if (securityService.isCurrentUserAdminOrSystem()) {
			return consumerRepository.findAll(PageRequest.of(0, 1000)).getContent();
		}
		Long userId = securityService.getCurrentUserId();
		return consumerRepository.findByUserId(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Consumer> getAllUnarchivedConsumers() {
		if (securityService.isCurrentUserAdminOrSystem()) {
			return consumerRepository.findAllByArchivedFalse(PageRequest.of(0, 1000)).getContent();
		}
		Long userId = securityService.getCurrentUserId();
		return consumerRepository.findAllByUserIdAndArchivedFalse(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Consumer> getAllConsumersByOwner(Long ownerId) {
		if (securityService.isCurrentUserAdminOrSystem()) {
			return consumerRepository.findByUserId(ownerId);
		} else {
			throw new UnauthorizedAccessException("Unauthorized access to Consumers for Owner with ID " + ownerId);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ConsumerResponse> getAllConsumersByOwnerAndRoomIsNull(Pageable pageable) {
		Long ownerId = securityService.getCurrentUserId();
		Page<Consumer> list = consumerRepository.findByUserIdAndRoomIsNull(ownerId, pageable);
		return list.map(ConsumerResponse::new);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ConsumerResponse> getAllConsumersByOwnerAndRoomId(Long roomId, Pageable pageable) {
		Long ownerId = securityService.getCurrentUserId();
		Page<Consumer> list = consumerRepository.findByRoomId(roomId, pageable);
		if (!Objects.equals(list.getContent().getFirst().getUserId(), ownerId)) {
			throw new UnauthorizedAccessException("Unauthorized access to Consumers for Room with ID " + roomId);
		}
		return list.map(ConsumerResponse::new);
	}

	@Override
	@Transactional(readOnly = true)
	public Consumer getConsumerById(Long consumerId) {
		Consumer consumer = consumerRepository.findById(consumerId).orElse(null);
		if (consumer != null) {
			if (securityService.canAccessDevice(consumer)) {
				return consumer;
			} else {
				throw new UnauthorizedAccessException("Unauthorized access to Consumer with ID " + consumerId);
			}
		}
		return null;
	}

	@Override
	@Transactional
	public Consumer updateConsumer(@Valid ConsumerRequest request) {
		if (request.getId() == null) {
			throw new CreateDeviceFailedException("Consumer ID must be provided when updating Consumer");
		}
		Consumer consumer = consumerRepository.findById(request.getId()).orElse(null);
		if (consumer != null) {
			if (securityService.canAccessDevice(consumer)) {
				boolean changed = false;
				if (!Objects.equals(request.getName(), consumer.getName())) {
					consumer.setName(request.getName());
					changed = true;
				}
				if (request.getPowerConsumption() != consumer.getPowerConsumption()) {
					if (consumer.isActive()) {
						setActive(consumer, false);
					}
					consumer.setPowerConsumption(request.getPowerConsumption());
					changed = true;
				}
				if (changed) {
					consumerRepository.save(consumer);
					return consumer;
				}
				throw new CreateDeviceFailedException("No changes detected in Consumer");
			}
			throw new UnauthorizedAccessException("Unauthorized access to Consumer with ID " + request.getId());
		}
		return null;
	}

	@Override
	@Transactional
	public Map<String, String> setActive(Consumer consumer, boolean active) {
		if (consumer != null) {
			if (securityService.canAccessDevice(consumer)) {
				if (consumer.isArchived()) {
					throw new CreateDeviceFailedException("Consumer with ID " + consumer.getId() + " is archived and cannot be activated or deactivated");
				}
				if (consumer.isActive() && active) {
					throw new CreateDeviceFailedException("Consumer with ID " + consumer.getId() + " is already active");
				}
				if (!consumer.isActive() && !active) {
					throw new CreateDeviceFailedException("Consumer with ID " + consumer.getId() + " is already inactive");
				}
				consumer.setActive(active);
				consumerRepository.save(consumer);
				String event = String.format(
						Locale.ENGLISH,
						"{\"deviceId\": %d, \"ownerId\": %d, \"commercial\": %b, \"active\": %b, \"powerConsumption\": %f, \"timestamp\": \"%s\"}",
						consumer.getId(), consumer.getUserId(), securityService.isCurrentUserACommercialUser(), active, consumer.getPowerConsumption(), Instant.now());
				kafkaTemplate.send("consumer-events", event).whenComplete((result, exception) -> {
					if (exception != null) {
						logger.error("Fehler beim Senden des Events: {}", exception.getMessage());
					}
				});
				return Map.of(
						"message", "Successfully updated Consumer with ID " + consumer.getId(),
						"id", consumer.getId().toString(),
						"active", consumer.isActive() ? "true" : "false"
				);
			}
		}
		throw new DeviceNotFoundException("Consumer not found");
	}

	@Override
	@Transactional
	public Map<String, String> archiveConsumer(Long consumerId) {
		Consumer consumer = consumerRepository.findById(consumerId).orElse(null);
		if (consumer != null) {
			if (securityService.canAccessDevice(consumer)) {
				if (!consumer.isArchived()) {
					consumer.setArchived(true);
					if (consumer.isActive()) {
						setActive(consumer, false);
					}
					Map<String, String> response = Map.of(
							"message", "Successfully archived Consumer with ID " + consumerId,
							"id", consumerId.toString()
					);
					consumerRepository.save(consumer);
					return response;
				} else {
					throw new CreateDeviceFailedException("Consumer with ID " + consumerId + " is already archived");
				}
			}
			throw new UnauthorizedAccessException("Unauthorized access to Consumer with ID " + consumerId);
		}
		throw new DeviceNotFoundException("Consumer with ID " + consumerId + " not found");
	}

	@Override
	@Transactional
	public Map<String, String> deleteConsumer(Long consumerId) {
		Consumer consumer = consumerRepository.findById(consumerId).orElse(null);
		if (consumer != null) {
			if (securityService.canAccessDevice(consumer)) {
				Map<String, String> response = Map.of(
						"message", "Successfully deleted Consumer with ID " + consumerId,
						"id", consumerId.toString()
				);
				consumerRepository.delete(consumer);
				return response;
			}
			throw new UnauthorizedAccessException("Unauthorized access to Consumer with ID " + consumerId);
		}
		return null;
	}

	@Override
	@Transactional
	public Map<String, String> deleteAllConsumersByOwnerId(Long ownerId) {
		if (securityService.isCurrentUserAdminOrSystem()) {
			consumerRepository.deleteAllByUserId(ownerId);
		} else {
			throw new UnauthorizedAccessException("Unauthorized access to Consumers for Owner with ID " + ownerId);
		}
		return Map.of(
				"message", "Successfully deleted all Consumers for Owner with ID " + ownerId
		);
	}

	@Override
	public Page<ConsumerResponse> getAllUnarchivedByUser(Pageable pageable) {
		Long userId = securityService.getCurrentUserId();
		Page<Consumer> consumerPage = consumerRepository.findByUserIdAndArchivedFalse(userId, pageable);
		return consumerPage.map(ConsumerResponse::new);
	}
}
