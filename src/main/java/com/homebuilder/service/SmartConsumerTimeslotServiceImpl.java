package com.homebuilder.service;

import com.homebuilder.dto.SmartConsumerTimeslotRequest;
import com.homebuilder.entity.SmartConsumer;
import com.homebuilder.entity.SmartConsumerProgram;
import com.homebuilder.entity.SmartConsumerTimeslot;
import com.homebuilder.exception.DeviceNotFoundException;
import com.homebuilder.exception.TimeslotOverlapException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.repository.SmartConsumerProgramRepository;
import com.homebuilder.repository.SmartConsumerRepository;
import com.homebuilder.repository.SmartConsumerTimeslotRepository;
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
public class SmartConsumerTimeslotServiceImpl implements SmartConsumerTimeslotService {

	private final SmartConsumerTimeslotRepository smartConsumerTimeslotRepository;
	private final SmartConsumerRepository smartConsumerRepository;

	private final SmartConsumerService smartConsumerService;
	private final SmartConsumerProgramService smartConsumerProgramService;

	private final SecurityService securityService;
	private final SmartConsumerProgramRepository smartConsumerProgramRepository;

	@Autowired
	public SmartConsumerTimeslotServiceImpl(SmartConsumerTimeslotRepository smartConsumerTimeslotRepository, SmartConsumerRepository smartConsumerRepository, SmartConsumerService smartConsumerService, SmartConsumerProgramService smartConsumerProgramService, SecurityService securityService, SmartConsumerProgramRepository smartConsumerProgramRepository) {
		this.smartConsumerTimeslotRepository = smartConsumerTimeslotRepository;
		this.smartConsumerRepository = smartConsumerRepository;
		this.smartConsumerService = smartConsumerService;
		this.smartConsumerProgramService = smartConsumerProgramService;
		this.securityService = securityService;
		this.smartConsumerProgramRepository = smartConsumerProgramRepository;
	}

	@Override
	public SmartConsumerTimeslot createSmartConsumerTimeslotForUser(@Valid SmartConsumerTimeslotRequest request) {
		Long userId = securityService.getCurrentUserId();
		SmartConsumerTimeslot smartConsumerTimeslot = request.toEntity();
		smartConsumerTimeslot.setUserId(userId);
		SmartConsumerProgram program = smartConsumerProgramService.getSmartConsumerProgramById(request.getSmartConsumerProgramId());
		smartConsumerTimeslot.setSmartConsumerProgram(program);
		int durationInSeconds = program.getDurationInSeconds();
		smartConsumerTimeslot.setEndTime(smartConsumerTimeslot.getStartTime().plusSeconds(durationInSeconds));
		SmartConsumer smartConsumer = smartConsumerService.getSmartConsumerByIdFromUser(request.getSmartConsumerId());
		smartConsumerTimeslot.setSmartConsumer(smartConsumer);
		List<SmartConsumerTimeslot> overlappingTimeslots = smartConsumerTimeslotRepository.findBySmartConsumerAndStartTimeLessThanAndEndTimeGreaterThan(smartConsumer, smartConsumerTimeslot.getEndTime(), smartConsumerTimeslot.getStartTime());
		if (!overlappingTimeslots.isEmpty()) {
			throw new TimeslotOverlapException("The timeslot overlaps with existing timeslots for this SmartConsumer.", overlappingTimeslots);
		}
		smartConsumer.getTimeslotList().add(smartConsumerTimeslot);
		smartConsumerTimeslotRepository.save(smartConsumerTimeslot);
		smartConsumerRepository.save(smartConsumer);
		return smartConsumerTimeslot;
	}

	@Override
	public List<SmartConsumerTimeslot> getAllSmartConsumerTimeslotsFromUser() {
		Long userId = securityService.getCurrentUserId();
		List<SmartConsumerTimeslot> smartConsumerTimeslotList = smartConsumerTimeslotRepository.findByUserId(userId);
		if (smartConsumerTimeslotList.isEmpty()) {
			throw new DeviceNotFoundException("No SmartConsumerTimeslots found for User with ID " + userId);
		}
		return smartConsumerTimeslotList;
	}

	@Override
	public SmartConsumerTimeslot getSmartConsumerTimeslotByIdFromUser(Long smartConsumerTimeslotId) {
		Long userId = securityService.getCurrentUserId();
		SmartConsumerTimeslot smartConsumerTimeslot = smartConsumerTimeslotRepository.findById(smartConsumerTimeslotId).orElseThrow(() -> new DeviceNotFoundException("SmartConsumerTimeslot with ID " + smartConsumerTimeslotId + " not found"));
		if (!smartConsumerTimeslot.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to smartConsumerTimeslot with ID " + smartConsumerTimeslotId);
		}
		return smartConsumerTimeslot;
	}

	@Override
	public SmartConsumerTimeslot updateSmartConsumerTimeslotForUser(Long existingTimeslotId, @Valid SmartConsumerTimeslotRequest request) {
		Long userId = securityService.getCurrentUserId();
		SmartConsumerTimeslot existingTimeslot = getSmartConsumerTimeslotByIdFromUser(existingTimeslotId);
		if (!existingTimeslot.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to smartConsumerTimeslot with ID " + existingTimeslotId);
		}
		existingTimeslot.setStartTime(request.getStartTime());
		SmartConsumerProgram program = smartConsumerProgramService.getSmartConsumerProgramById(request.getSmartConsumerProgramId());
		int durationInSeconds = program.getDurationInSeconds();
		existingTimeslot.setEndTime(existingTimeslot.getStartTime().plusSeconds(durationInSeconds));
		existingTimeslot.setArchived(request.isArchived());
		smartConsumerTimeslotRepository.save(existingTimeslot);
		return existingTimeslot;
	}

	@Override
	public Map<String, String> deleteSmartConsumerTimeslotForUser(Long smartConsumerTimeslotId) {
		Long userId = securityService.getCurrentUserId();
		SmartConsumerTimeslot smartConsumerTimeslot = getSmartConsumerTimeslotByIdFromUser(smartConsumerTimeslotId);
		if (!smartConsumerTimeslot.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to smartConsumerTimeslot with ID " + smartConsumerTimeslotId);
		}
		Map<String, String> response = Map.of(
				"message", "Successfully deleted SmartConsumerProgram with ID " + smartConsumerTimeslotId,
				"id", smartConsumerTimeslotId.toString()
		);
		smartConsumerTimeslotRepository.delete(smartConsumerTimeslot);
		return response;
	}

	@Override
	public List<SmartConsumerTimeslot> getAllSmartConsumerTimeslots() {
		return smartConsumerTimeslotRepository.findAll();
	}

	@Override
	public SmartConsumerTimeslot getSmartConsumerTimeslotById(Long smartConsumerTimeslotId) {
		return smartConsumerTimeslotRepository.findById(smartConsumerTimeslotId).orElseThrow(() -> new DeviceNotFoundException("SmartConsumerTimeslot with ID " + smartConsumerTimeslotId + " not found"));
	}
}
