package com.homebuilder.service;

import com.homebuilder.dto.SmartConsumerProgramRequest;
import com.homebuilder.entity.SmartConsumer;
import com.homebuilder.entity.SmartConsumerProgram;
import com.homebuilder.exception.DeviceNotFoundException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.repository.SmartConsumerProgramRepository;
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
public class SmartConsumerProgramServiceImpl implements SmartConsumerProgramService {

	private final SmartConsumerProgramRepository smartConsumerProgramRepository;
	private final SmartConsumerRepository smartConsumerRepository;

	private final SmartConsumerService smartConsumerService;

	private final SecurityService securityService;

	@Autowired
	public SmartConsumerProgramServiceImpl(SmartConsumerProgramRepository smartConsumerProgramRepository, SmartConsumerRepository smartConsumerRepository, SmartConsumerService smartConsumerService, SecurityService securityService) {
		this.smartConsumerProgramRepository = smartConsumerProgramRepository;
		this.smartConsumerRepository = smartConsumerRepository;
		this.smartConsumerService = smartConsumerService;
		this.securityService = securityService;
	}

	@Override
	public SmartConsumerProgram createSmartConsumerProgramForUser(@Valid SmartConsumerProgramRequest request) {
		Long userId = securityService.getCurrentUserId();
		SmartConsumerProgram smartConsumerProgram = request.toEntity();
		smartConsumerProgram.setUserId(userId);
		SmartConsumer smartConsumer = smartConsumerService.getSmartConsumerByIdFromUser(request.getSmartConsumerId());
		smartConsumerProgram.setSmartConsumer(smartConsumer);
		smartConsumer.getProgramList().add(smartConsumerProgram);
		smartConsumerProgramRepository.save(smartConsumerProgram);
		smartConsumerRepository.save(smartConsumer);
		return smartConsumerProgram;
	}

	@Override
	public List<SmartConsumerProgram> getAllSmartConsumerProgramsFromUser() {
		Long userId = securityService.getCurrentUserId();
		List<SmartConsumerProgram> smartConsumerProgramList = smartConsumerProgramRepository.findByUserId(userId);
		if (smartConsumerProgramList.isEmpty()) {
			throw new DeviceNotFoundException("No SmartConsumerPrograms found for User with ID " + userId);
		}
		return smartConsumerProgramList;
	}

	@Override
	public SmartConsumerProgram getSmartConsumerProgramByIdFromUser(Long smartConsumerProgramId) {
		Long userId = securityService.getCurrentUserId();
		SmartConsumerProgram smartConsumerProgram = smartConsumerProgramRepository.findById(smartConsumerProgramId).orElseThrow(() -> new DeviceNotFoundException("SmartConsumerProgram with ID " + smartConsumerProgramId + " not found"));
		if (!smartConsumerProgram.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to smartConsumerProgram with ID " + smartConsumerProgramId);
		}
		return smartConsumerProgram;
	}

	@Override
	public SmartConsumerProgram updateSmartConsumerProgramForUser(Long existingProgramId, @Valid SmartConsumerProgramRequest request) {
		Long userId = securityService.getCurrentUserId();
		SmartConsumerProgram existingProgram = getSmartConsumerProgramByIdFromUser(existingProgramId);
		if (!existingProgram.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to SmartConsumerProgram with ID " + existingProgramId);
		}
		existingProgram.setName(request.getName());
		existingProgram.setDurationInSeconds(request.getDurationInSeconds());
		existingProgram.setPowerConsumption(request.getPowerConsumption());
		existingProgram.setArchived(request.isArchived());
		smartConsumerProgramRepository.save(existingProgram);
		return existingProgram;
	}

	@Override
	public Map<String, String> deleteSmartConsumerProgramForUser(Long smartConsumerProgramId) {
		Long userId = securityService.getCurrentUserId();
		SmartConsumerProgram smartConsumerProgram = getSmartConsumerProgramByIdFromUser(smartConsumerProgramId);
		if (!smartConsumerProgram.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to SmartConsumerProgram with ID " + smartConsumerProgramId);
		}
		Map<String, String> response = Map.of(
				"message", "Successfully deleted SmartConsumerProgram with ID " + smartConsumerProgramId,
				"id", smartConsumerProgramId.toString()
		);
		smartConsumerProgramRepository.delete(smartConsumerProgram);
		return response;
	}

	@Override
	public List<SmartConsumerProgram> getAllSmartConsumerPrograms() {
		return smartConsumerProgramRepository.findAll();
	}

	@Override
	public SmartConsumerProgram getSmartConsumerProgramById(Long smartConsumerProgramId) {
		return smartConsumerProgramRepository.findById(smartConsumerProgramId).orElseThrow(() -> new DeviceNotFoundException("SmartConsumerProgram with ID " + smartConsumerProgramId + " not found"));
	}
}
