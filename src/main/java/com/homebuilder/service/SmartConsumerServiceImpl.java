package com.homebuilder.service;

import com.homebuilder.dto.SmartConsumerRequest;
import com.homebuilder.entity.SmartConsumer;
import com.homebuilder.exception.CreateDeviceFailedException;
import com.homebuilder.exception.DeviceNotFoundException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.repository.SmartConsumerRepository;
import com.homebuilder.security.SecurityService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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

    @Autowired
    public SmartConsumerServiceImpl(SmartConsumerRepository smartConsumerRepository, SecurityService securityService, KafkaTemplate<String, String> kafkaTemplate) {
        this.smartConsumerRepository = smartConsumerRepository;
        this.securityService = securityService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public SmartConsumer createSmartConsumer(@Valid SmartConsumerRequest request) {
        SmartConsumer smartConsumer = request.toEntity();
        if (securityService.isCurrentUserAdminOrSystem()) {
            if (request.getOwnerId() == null) {
                throw new CreateDeviceFailedException("Owner ID must be provided when creating SmartConsumer as System User");
            } else {
                smartConsumer.setUserId(request.getOwnerId());
            }
        } else {
            smartConsumer.setUserId(securityService.getCurrentUserId());
        }
        smartConsumerRepository.save(smartConsumer);
        return smartConsumer;
    }

    @Override
    public List<SmartConsumer> getAllSmartConsumers() {
        if (securityService.isCurrentUserAdminOrSystem()) {
            return smartConsumerRepository.findAll(PageRequest.of(0, 1000)).getContent();
        }
        Long userId = securityService.getCurrentUserId();
        return smartConsumerRepository.findByUserId(userId);
    }

    @Override
    public List<SmartConsumer> getAllSmartConsumersByOwner(Long ownerId) {
        if (securityService.isCurrentUserAdminOrSystem()) {
            return smartConsumerRepository.findByUserId(ownerId);
        } else {
            throw new UnauthorizedAccessException("Unauthorized access to SmartConsumers for Owner with ID " + ownerId);
        }
    }

    @Override
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
						setActive(smartConsumer, false);
					}
					smartConsumer.setPowerConsumption(request.getPowerConsumption());
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
    public Map<String, String> setActive(SmartConsumer smartConsumer, boolean active) {
        if (smartConsumer != null) {
            if (securityService.canAccessDevice(smartConsumer)) {
                if (smartConsumer.isArchived()) {
                    throw new CreateDeviceFailedException("SmartConsumer with ID " + smartConsumer.getId() + " is archived and cannot be activated or deactivated");
                }
                if (smartConsumer.isActive() && active) {
                    throw new CreateDeviceFailedException("SmartConsumer with ID " + smartConsumer.getId() + " is already active");
                }
                if (!smartConsumer.isActive() && !active) {
                    throw new CreateDeviceFailedException("SmartConsumer with ID " + smartConsumer.getId() + " is already inactive");
                }
                smartConsumer.setActive(active);
                smartConsumerRepository.save(smartConsumer);
                String event = String.format(
                        Locale.ENGLISH,
                        "{\"deviceId\": %d, \"ownerId\": %d, \"commercial\": %b, \"active\": %b, \"powerConsumption\": %f, \"timestamp\": \"%s\"}",
                        smartConsumer.getId(), smartConsumer.getUserId(), securityService.isCurrentUserACommercialUser(), active, smartConsumer.getPowerConsumption(), Instant.now());
                kafkaTemplate.send("consumer-events", event).whenComplete((result, exception) -> {
                    if (exception != null) {
                        logger.error("Fehler beim Senden des Events: {}", exception.getMessage());
                    }
                });
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
    public Map<String, String> archiveSmartConsumer(Long smartConsumerId) {
        SmartConsumer smartConsumer = smartConsumerRepository.findById(smartConsumerId).orElse(null);
        if (smartConsumer != null) {
            if (securityService.canAccessDevice(smartConsumer)) {
                if (!smartConsumer.isArchived()) {
                    smartConsumer.setArchived(true);
                    if (smartConsumer.isActive()) {
                        setActive(smartConsumer, false);
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
    public Map<String, String> deleteSmartConsumer(Long smartConsumerId) {
        SmartConsumer smartConsumer = smartConsumerRepository.findById(smartConsumerId).orElse(null);
        if (smartConsumer != null) {
            if (securityService.canAccessDevice(smartConsumer)) {
                Map<String, String> response = Map.of(
                        "message", "Successfully deleted SmartConsumer with ID " + smartConsumerId,
                        "id", smartConsumerId.toString()
                );
                smartConsumerRepository.delete(smartConsumer);
                return response;
            }
            throw new UnauthorizedAccessException("Unauthorized access to SmartConsumer with ID " + smartConsumerId);
        }
        return null;
    }
}
