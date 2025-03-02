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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
@Service
public class SmartConsumerTimeslotServiceImpl implements SmartConsumerTimeslotService {

	private final SmartConsumerTimeslotRepository smartConsumerTimeslotRepository;
	private final SmartConsumerProgramRepository smartConsumerProgramRepository;
	private final SmartConsumerRepository smartConsumerRepository;

	private final SecurityService securityService;

	@Autowired
	public SmartConsumerTimeslotServiceImpl(SmartConsumerTimeslotRepository smartConsumerTimeslotRepository, SmartConsumerProgramRepository smartConsumerProgramRepository, SmartConsumerRepository smartConsumerRepository, SecurityService securityService) {
		this.smartConsumerTimeslotRepository = smartConsumerTimeslotRepository;
		this.smartConsumerProgramRepository = smartConsumerProgramRepository;
		this.smartConsumerRepository = smartConsumerRepository;
		this.securityService = securityService;
	}

	@Override
	public SmartConsumerTimeslot createSmartConsumerTimeslot(@Valid SmartConsumerTimeslotRequest request) {
		SmartConsumerTimeslot timeslot = request.toEntity();
		if (securityService.isCurrentUserAdminOrSystem()) {
			if (request.getOwnerId() == null) {
				throw new DeviceNotFoundException("Owner ID must be provided when creating SmartConsumerTimeslot as System User");
			} else {
				timeslot.setUserId(request.getOwnerId());
			}
		} else {
			timeslot.setUserId(securityService.getCurrentUserId());
		}
		SmartConsumer smartConsumer = smartConsumerRepository.findById(request.getSmartConsumerId()).orElse(null);
		if (smartConsumer != null) {
			if (securityService.canAccessDevice(smartConsumer)) {
				SmartConsumerProgram program = smartConsumerProgramRepository.findById(request.getSmartConsumerProgramId()).orElse(null);
				if (program != null) {
					if (securityService.canAccessProgram(program)) {
						timeslot.setSmartConsumerProgram(program);
						timeslot.setStartTime(request.getStartTimeAsInstant());
						int durationInSeconds = program.getDurationInSeconds();
						timeslot.setEndTime(timeslot.getStartTime().plusSeconds(durationInSeconds));
						timeslot.setSmartConsumer(smartConsumer);
						List<SmartConsumerTimeslot> overlappingTimeslots = smartConsumerTimeslotRepository.findBySmartConsumerAndStartTimeLessThanAndEndTimeGreaterThan(smartConsumer, timeslot.getEndTime(), timeslot.getStartTime());
						if (!overlappingTimeslots.isEmpty()) {
							throw new TimeslotOverlapException("The timeslot overlaps with existing timeslots for this SmartConsumer.", overlappingTimeslots);
						}
						smartConsumer.getTimeslotList().add(timeslot);
						smartConsumerTimeslotRepository.save(timeslot);
						smartConsumerRepository.save(smartConsumer);
						return timeslot;
					}
					throw new UnauthorizedAccessException("Unauthorized access to SmartConsumerProgram with ID " + request.getSmartConsumerProgramId());
				}
				throw new DeviceNotFoundException("SmartConsumerProgram with ID " + request.getSmartConsumerProgramId() + " not found");
			}
			throw new UnauthorizedAccessException("Unauthorized access to SmartConsumer with ID " + request.getSmartConsumerId());
		}
		throw new DeviceNotFoundException("SmartConsumer with ID " + request.getSmartConsumerId() + " not found");
	}

	@Override
	public List<SmartConsumerTimeslot> getAllSmartConsumerTimeslots() {
		if (securityService.isCurrentUserAdminOrSystem()) {
			return smartConsumerTimeslotRepository.findAll(PageRequest.of(0, 1000)).getContent();
		}
		Long userId = securityService.getCurrentUserId();
		return smartConsumerTimeslotRepository.findByUserId(userId);
	}

	@Override
	public List<SmartConsumerTimeslot> getAllSmartConsumerTimeslotsByOwner(Long ownerId) {
		if (securityService.isCurrentUserAdminOrSystem()) {
			return smartConsumerTimeslotRepository.findByUserId(ownerId);
		} else {
			throw new UnauthorizedAccessException("Unauthorized access to SmartConsumerTimeslot for Owner with ID " + securityService.getCurrentUserId());
		}
	}

	@Override
	public SmartConsumerTimeslot getSmartConsumerTimeslotById(Long smartConsumerTimeslotId) {
		SmartConsumerTimeslot timeslot = smartConsumerTimeslotRepository.findById(smartConsumerTimeslotId).orElse(null);
		if (timeslot != null) {
			if (securityService.canAccessTimeslot(timeslot)) {
				return timeslot;
			}
			throw new UnauthorizedAccessException("Unauthorized access to SmartConsumerTimeslot with ID " + smartConsumerTimeslotId);
		}
		return null;
	}

	@Override
	public SmartConsumerTimeslot updateSmartConsumerTimeslot(@Valid SmartConsumerTimeslotRequest request) {
		if (request.getId() == null) {
			throw new DeviceNotFoundException("SmartConsumerTimeslot ID must be provided when updating SmartConsumerTimeslot");
		}
		SmartConsumerTimeslot timeslot = smartConsumerTimeslotRepository.findById(request.getId()).orElse(null);
		if (timeslot != null) {
			if (securityService.canAccessTimeslot(timeslot)) {
				SmartConsumer smartConsumer = smartConsumerRepository.findById(request.getSmartConsumerId()).orElse(null);
				if (smartConsumer != null) {
					if (securityService.canAccessDevice(smartConsumer)) {
						SmartConsumerProgram program = smartConsumerProgramRepository.findById(request.getSmartConsumerProgramId()).orElse(null);
						if (program != null) {
							if (securityService.canAccessProgram(program)) {
								timeslot.setSmartConsumer(smartConsumer);
								timeslot.setSmartConsumerProgram(program);
								timeslot.setStartTime(request.getStartTimeAsInstant());
								int durationInSeconds = program.getDurationInSeconds();
								timeslot.setEndTime(timeslot.getStartTime().plusSeconds(durationInSeconds));
								smartConsumerTimeslotRepository.save(timeslot);
								return timeslot;
							}
							throw new UnauthorizedAccessException("Unauthorized access to SmartConsumerProgram with ID " + request.getSmartConsumerProgramId());
						}
						throw new DeviceNotFoundException("SmartConsumerProgram with ID " + request.getSmartConsumerProgramId() + " not found");
					}
					throw new UnauthorizedAccessException("Unauthorized access to SmartConsumer with ID " + request.getSmartConsumerId());
				}
				throw new DeviceNotFoundException("SmartConsumer with ID " + request.getSmartConsumerId() + " not found");
			}
			throw new UnauthorizedAccessException("Unauthorized access to SmartConsumerTimeslot with ID " + request.getId());
		}
		throw new DeviceNotFoundException("SmartConsumerTimeslot with ID " + request.getId() + " not found");
	}

	@Override
	public Map<String, String> deleteSmartConsumerTimeslot(Long smartConsumerTimeslotId) {
		SmartConsumerTimeslot timeslot = smartConsumerTimeslotRepository.findById(smartConsumerTimeslotId).orElse(null);
		if (timeslot != null) {
			if (securityService.canAccessTimeslot(timeslot)) {
				Map<String, String> response = Map.of(
						"message", "Successfully deleted SmartConsumerTimeslot with ID " + smartConsumerTimeslotId,
						"id", smartConsumerTimeslotId.toString()
				);
				smartConsumerTimeslotRepository.delete(timeslot);
				return response;
			}
			throw new UnauthorizedAccessException("Unauthorized access to SmartConsumerTimeslot with ID " + smartConsumerTimeslotId);
		}
		throw new DeviceNotFoundException("SmartConsumerTimeslot with ID " + smartConsumerTimeslotId + " not found");
	}
}
