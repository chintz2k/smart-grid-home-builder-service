package com.homebuilder.service;

import com.homebuilder.dto.SmartConsumerProgramRequest;
import com.homebuilder.dto.SmartConsumerRequest;
import com.homebuilder.entity.Room;
import com.homebuilder.entity.SmartConsumer;
import com.homebuilder.exception.CreateDeviceFailedException;
import com.homebuilder.exception.DeviceNotFoundException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.repository.RoomRepository;
import com.homebuilder.repository.SmartConsumerRepository;
import com.homebuilder.security.SecurityService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
public class SmartConsumerServiceImpl implements SmartConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(SmartConsumerServiceImpl.class);

    private final SmartConsumerRepository smartConsumerRepository;

    private final SecurityService securityService;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final SmartConsumerProgramService smartConsumerProgramService;
    private final RoomRepository roomRepository;

    @Autowired
    public SmartConsumerServiceImpl(SmartConsumerRepository smartConsumerRepository, SecurityService securityService, KafkaTemplate<String, String> kafkaTemplate, SmartConsumerProgramService smartConsumerProgramService, RoomRepository roomRepository) {
        this.smartConsumerRepository = smartConsumerRepository;
        this.securityService = securityService;
        this.kafkaTemplate = kafkaTemplate;
        this.smartConsumerProgramService = smartConsumerProgramService;
        this.roomRepository = roomRepository;
    }

    @Override
    @Transactional
    public SmartConsumer createSmartConsumer(@Valid SmartConsumerRequest request) {
        SmartConsumer smartConsumer = request.toEntity();
        Long roomId = request.getRoomId();
        if (securityService.isCurrentUserAdminOrSystem()) {
            if (request.getOwnerId() == null) {
                throw new CreateDeviceFailedException("Owner ID must be provided when creating SmartConsumer as System User");
            } else {
                smartConsumer.setUserId(request.getOwnerId());
            }
        } else {
            smartConsumer.setUserId(securityService.getCurrentUserId());
        }
		SmartConsumer dbObject = smartConsumerRepository.save(smartConsumer);
		if (roomId != null) {
            Room room = roomRepository.findById(roomId).orElse(null);
            if (room != null) {
                if (securityService.getCurrentUserId().equals(room.getUserId())) {
                    room.addDevice(smartConsumer);
                    smartConsumer.setRoom(room);
                    roomRepository.save(room);
                } else {
                    throw new UnauthorizedAccessException("Unauthorized access to Room with ID " + roomId);
                }
            }
        }
        SmartConsumerProgramRequest program = new SmartConsumerProgramRequest();
        program.setSmartConsumerId(dbObject.getId());
        program.setName(request.getSmartConsumerProgramName());
        program.setPowerConsumption(request.getSmartConsumerProgramPowerConsumption());
        program.setDurationInSeconds(request.getSmartConsumerProgramDurationInSeconds());
        smartConsumerProgramService.createSmartConsumerProgram(program);
		return smartConsumer;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SmartConsumer> getAllSmartConsumers() {
        if (securityService.isCurrentUserAdminOrSystem()) {
            return smartConsumerRepository.findAll(PageRequest.of(0, 1000)).getContent();
        }
        Long userId = securityService.getCurrentUserId();
        return smartConsumerRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SmartConsumer> getAllUnarchivedSmartConsumers() {
        if (securityService.isCurrentUserAdminOrSystem()) {
            return smartConsumerRepository.findAllByArchivedFalse(PageRequest.of(0, 1000)).getContent();
        }
        Long userId = securityService.getCurrentUserId();
        return smartConsumerRepository.findAllByUserIdAndArchivedFalse(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SmartConsumer> getAllSmartConsumersByOwner(Long ownerId) {
        if (securityService.isCurrentUserAdminOrSystem()) {
            return smartConsumerRepository.findByUserId(ownerId);
        } else {
            throw new UnauthorizedAccessException("Unauthorized access to SmartConsumers for Owner with ID " + ownerId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public SmartConsumer getSmartConsumerById(Long smartConsumerId) {
        SmartConsumer smartConsumer = smartConsumerRepository.findById(smartConsumerId).orElse(null);
        if (smartConsumer != null) {
            if (securityService.canAccessDevice(smartConsumer)) {
                return smartConsumer;
            } else {
                throw new UnauthorizedAccessException("Unauthorized access to SmartConsumer with ID " + smartConsumerId);
            }
        }
        return null;
    }

    @Override
    @Transactional
    public SmartConsumer updateSmartConsumer(@Valid SmartConsumerRequest request) {
        if (request.getId() == null) {
            throw new CreateDeviceFailedException("SmartConsumer ID must be provided when updating SmartConsumer");
        }
        SmartConsumer smartConsumer = smartConsumerRepository.findById(request.getId()).orElse(null);
        if (smartConsumer != null) {
            if (securityService.canAccessDevice(smartConsumer)) {
                boolean changed = false;
                if (!Objects.equals(request.getName(), smartConsumer.getName())) {
                    smartConsumer.setName(request.getName());
                    changed = true;
                }
                if (request.getPowerConsumption() != smartConsumer.getPowerConsumption()) {
					if (smartConsumer.isActive()) {
						setActive(smartConsumer, false, true);
					}
					smartConsumer.setPowerConsumption(request.getPowerConsumption());
                    changed = true;
                }
                if (request.getRoomId() != null && !Objects.equals(request.getRoomId(), smartConsumer.getRoom().getId())) {
                    if (smartConsumer.isActive()) {
                        setActive(smartConsumer, false, true);
                    }
                    Room oldRoom = roomRepository.findById(smartConsumer.getRoom().getId()).orElse(null);
                    Room newRoom = roomRepository.findById(request.getRoomId()).orElse(null);
                    if (oldRoom != null) {
                        oldRoom.removeDevice(smartConsumer);
                        roomRepository.save(oldRoom);
                    }
                    if (newRoom != null) {
                        if (securityService.getCurrentUserId().equals(newRoom.getUserId())) {
                            newRoom.addDevice(smartConsumer);
                            smartConsumer.setRoom(newRoom);
                            roomRepository.save(newRoom);
                        }
                    }
                    changed = true;
                }
                if (changed) {
                    smartConsumerRepository.save(smartConsumer);
                    return smartConsumer;
                }
                throw new CreateDeviceFailedException("No changes detected in SmartConsumer");
            }
            throw new UnauthorizedAccessException("Unauthorized access to SmartConsumer with ID " + request.getId());
        }
        return null;
    }

    @Override
    @Transactional
    public Map<String, String> setActive(SmartConsumer smartConsumer, boolean active, boolean sendEvent) {
        if (smartConsumer != null) {
            if (securityService.canAccessDevice(smartConsumer)) {
                if (smartConsumer.isArchived()) {
                    return Map.of(
                            "warning", "SmartConsumer is archived and cannot be activated or deactivated",
                            "id", smartConsumer.getId().toString()
                    );
                }
                if (smartConsumer.isActive() && active) {
                    return Map.of(
                            "warning", "SmartConsumer is already active",
                            "id", smartConsumer.getId().toString()
                    );
                }
                if (!smartConsumer.isActive() && !active) {
                    return Map.of(
                            "warning", "SmartConsumer is already inactive",
                            "id", smartConsumer.getId().toString()
                    );
                }
                smartConsumer.setActive(active);
                smartConsumerRepository.save(smartConsumer);
				if (sendEvent) {
					String event = String.format(
							Locale.ENGLISH,
							"{\"deviceId\": %d, \"ownerId\": %d, \"commercial\": %b, \"active\": %b, \"powerConsumption\": %f, \"timestamp\": \"%s\"}",
							smartConsumer.getId(), smartConsumer.getUserId(), securityService.isCurrentUserACommercialUser(), active, smartConsumer.getPowerConsumption(), Instant.now());
					kafkaTemplate.send("consumer-events", event).whenComplete((result, exception) -> {
						if (exception != null) {
							logger.error("Fehler beim Senden des Events: {}", exception.getMessage());
						}
					});
				}
				return Map.of(
                        "message", "Successfully updated SmartConsumer with ID " + smartConsumer.getId(),
                        "id", smartConsumer.getId().toString(),
                        "active", smartConsumer.isActive() ? "true" : "false"
                );
            }
        }
        throw new DeviceNotFoundException("SmartConsumer not found");
    }

    @Override
    @Transactional
    public Map<String, String> archiveSmartConsumer(Long smartConsumerId) {
        SmartConsumer smartConsumer = smartConsumerRepository.findById(smartConsumerId).orElse(null);
        if (smartConsumer != null) {
            if (securityService.canAccessDevice(smartConsumer)) {
                if (!smartConsumer.isArchived()) {
                    smartConsumer.setArchived(true);
                    if (smartConsumer.isActive()) {
                        setActive(smartConsumer, false, true);
                    }
                    Map<String, String> response = Map.of(
                            "message", "Successfully archived SmartConsumer with ID " + smartConsumerId,
                            "id", smartConsumerId.toString()
                    );
                    smartConsumerRepository.save(smartConsumer);
                    return response;
                } else {
                    throw new CreateDeviceFailedException("SmartConsumer with ID " + smartConsumerId + " is already archived");
                }
            }
            throw new UnauthorizedAccessException("Unauthorized access to SmartConsumer with ID " + smartConsumerId);
        }
        return null;
    }

    @Override
    @Transactional
    public Map<String, String> deleteSmartConsumer(Long smartConsumerId) {
        SmartConsumer smartConsumer = smartConsumerRepository.findById(smartConsumerId).orElse(null);
        if (smartConsumer != null) {
            if (securityService.canAccessDevice(smartConsumer)) {
                smartConsumer.getRoom().removeDevice(smartConsumer);
                smartConsumer.setRoom(null);
                Map<String, String> response = Map.of(
                        "message", "Successfully deleted SmartConsumer with ID " + smartConsumerId,
                        "id", smartConsumerId.toString()
                );
                smartConsumerRepository.deleteById(smartConsumerId);
                return response;
            }
            throw new UnauthorizedAccessException("Unauthorized access to SmartConsumer with ID " + smartConsumerId);
        }
        return null;
    }

    @Override
    @Transactional
    public Map<String, String> deleteAllSmartConsumersByOwnerId(Long ownerId) {
        if (securityService.isCurrentUserAdminOrSystem()) {
            smartConsumerRepository.deleteAllByUserId(ownerId);
        } else {
            throw new UnauthorizedAccessException("Unauthorized access to SmartConsumers for Owner with ID " + ownerId);
        }
        return Map.of(
                "message", "Successfully deleted all SmartConsumers for Owner with ID " + ownerId
        );
    }
}
