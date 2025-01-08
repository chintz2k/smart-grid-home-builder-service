package com.homebuilder.service;

import com.homebuilder.dto.SmartConsumerRequest;
import com.homebuilder.entity.SmartConsumer;
import com.homebuilder.exception.DeviceNotFoundException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.repository.SmartConsumerRepository;
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
public class SmartConsumerServiceImpl implements SmartConsumerService {

    private final SmartConsumerRepository smartConsumerRepository;
    private final SecurityService securityService;

    @Autowired
    public SmartConsumerServiceImpl(SmartConsumerRepository smartConsumerRepository, SecurityService securityService) {
        this.smartConsumerRepository = smartConsumerRepository;
        this.securityService = securityService;
    }

    @Override
    public SmartConsumer createSmartConsumerForUser(@Valid SmartConsumerRequest request) {
        Long userId = securityService.getCurrentUserId();
        SmartConsumer smartConsumer = request.toEntity();
        smartConsumer.setUserId(userId);
        smartConsumerRepository.save(smartConsumer);
        return smartConsumer;
    }

    @Override
    public List<SmartConsumer> getAllSmartConsumersFromUser() {
        Long userId = securityService.getCurrentUserId();
        List<SmartConsumer> smartConsumerList = smartConsumerRepository.findByUserId(userId);
        if (smartConsumerList.isEmpty()) {
            throw new DeviceNotFoundException("No SmartConsumers found for User with ID " + userId);
        }
        return smartConsumerList;
    }

    @Override
    public SmartConsumer getSmartConsumerByIdFromUser(Long smartConsumerId) {
        Long userId = securityService.getCurrentUserId();
        SmartConsumer smartConsumer = smartConsumerRepository.findById(smartConsumerId).orElseThrow(() -> new DeviceNotFoundException("SmartConsumer with ID " + smartConsumerId + " not found"));
        if (!smartConsumer.getUserId().equals(userId)) {
            throw new UnauthorizedAccessException("Unauthorized access to SmartConsumer with ID " + smartConsumerId);
        }
        return smartConsumer;
    }

    @Override
    public SmartConsumer updateSmartConsumerForUser(Long existingConsumerId, @Valid SmartConsumerRequest request) {
        Long userId = securityService.getCurrentUserId();
        SmartConsumer existingConsumer = getSmartConsumerByIdFromUser(existingConsumerId);
        if (!existingConsumer.getUserId().equals(userId)) {
            throw new UnauthorizedAccessException("Unauthorized access to SmartConsumer with ID " + request.getId());
        }
        existingConsumer.setName(request.getName());
        existingConsumer.setPowerConsumption(request.getPowerConsumption());
        existingConsumer.setActive(request.isActive());
        existingConsumer.setArchived(request.isArchived());
        smartConsumerRepository.save(existingConsumer);
        return existingConsumer;
    }

    @Override
    public Map<String, String> deleteSmartConsumerForUser(Long smartConsumerId) {
        Long userId = securityService.getCurrentUserId();
        SmartConsumer smartConsumer = getSmartConsumerByIdFromUser(smartConsumerId);
        if (!smartConsumer.getUserId().equals(userId)) {
            throw new UnauthorizedAccessException("Unauthorized access to SmartConsumer with ID " + smartConsumerId);
        }
        Map<String, String> response = Map.of(
                "message", "Successfully deleted SmartConsumer with ID " + smartConsumerId,
                "id", smartConsumerId.toString()
        );
        smartConsumerRepository.delete(smartConsumer);
        return response;
    }

    @Override
    public List<SmartConsumer> getAllSmartConsumers() {
        return smartConsumerRepository.findAll();
    }

    @Override
    public SmartConsumer getSmartConsumerById(Long smartConsumerId) {
        return smartConsumerRepository.findById(smartConsumerId).orElseThrow(() -> new DeviceNotFoundException("SmartConsumer with ID " + smartConsumerId + " not found"));
    }
}
