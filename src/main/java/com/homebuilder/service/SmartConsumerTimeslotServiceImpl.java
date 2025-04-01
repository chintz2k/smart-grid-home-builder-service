package com.homebuilder.service;

import com.homebuilder.dto.SmartConsumerTimeslotRequest;
import com.homebuilder.entity.SmartConsumer;
import com.homebuilder.entity.SmartConsumerProgram;
import com.homebuilder.entity.SmartConsumerTimeslot;
import com.homebuilder.exception.*;
import com.homebuilder.repository.SmartConsumerProgramRepository;
import com.homebuilder.repository.SmartConsumerRepository;
import com.homebuilder.repository.SmartConsumerTimeslotRepository;
import com.homebuilder.security.SecurityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
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
						if (!smartConsumer.getProgramList().contains(program)) {
							throw new SmartProgramNotAllowedForSmartConsumerException("This Program is not executable on this SmartConsumer");
						}
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
	@Transactional(readOnly = true)
	public List<SmartConsumerTimeslot> getAllSmartConsumerTimeslots() {
		if (securityService.isCurrentUserAdminOrSystem()) {
			return smartConsumerTimeslotRepository.findAll(PageRequest.of(0, 1000)).getContent();
		}
		Long userId = securityService.getCurrentUserId();
		return smartConsumerTimeslotRepository.findByUserId(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<SmartConsumerTimeslot> getAllSmartConsumerTimeslotsByOwner(Long ownerId) {
		if (securityService.isCurrentUserAdminOrSystem()) {
			return smartConsumerTimeslotRepository.findByUserId(ownerId);
		} else {
			throw new UnauthorizedAccessException("Unauthorized access to SmartConsumerTimeslot for Owner with ID " + securityService.getCurrentUserId());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<SmartConsumerTimeslot> getAllByUserId(Long userId, Pageable pageable) {
		if (userId != null) {
			if (securityService.isCurrentUserAdminOrSystem()) {
				return smartConsumerTimeslotRepository.findAllByUserId(userId, pageable);
			}
		}
		userId = securityService.getCurrentUserId();
		return smartConsumerTimeslotRepository.findAllByUserId(userId, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<SmartConsumerTimeslot> getAllByUserIdAndByConsumerId(Long userId, Long consumerId, Pageable pageable) {
		if (userId != null) {
			if (securityService.isCurrentUserAdminOrSystem()) {
				return smartConsumerTimeslotRepository.findAllByUserIdAndSmartConsumerId(userId, consumerId, pageable);
			}
		}
		userId = securityService.getCurrentUserId();
		return smartConsumerTimeslotRepository.findAllByUserIdAndSmartConsumerId(userId, consumerId, pageable);
	}

	@Override
	@Transactional(readOnly = true)
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
	@Transactional
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
								if (!smartConsumer.getProgramList().contains(program)) {
									throw new SmartProgramNotAllowedForSmartConsumerException("This Program is not executable on this SmartConsumer");
								}
								if (timeslot.getStartTime() != request.getStartTimeAsInstant()) {
									timeslot.setStartTime(request.getStartTimeAsInstant());
									int durationInSeconds = program.getDurationInSeconds();
									timeslot.setEndTime(timeslot.getStartTime().plusSeconds(durationInSeconds));
									List<SmartConsumerTimeslot> overlappingTimeslots = smartConsumerTimeslotRepository.findBySmartConsumerAndStartTimeLessThanAndEndTimeGreaterThan(smartConsumer, timeslot.getEndTime(), timeslot.getStartTime());
									if (!overlappingTimeslots.isEmpty()) {
										throw new TimeslotOverlapException("The timeslot overlaps with existing timeslots for this SmartConsumer.", overlappingTimeslots);
									}
									smartConsumerTimeslotRepository.save(timeslot);
									return timeslot;
								}
								throw new CreateDeviceFailedException("No changes were made to the SmartConsumerTimeslot");
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
	@Transactional
	public Map<String, String> deleteSmartConsumerTimeslot(Long smartConsumerTimeslotId) {
		SmartConsumerTimeslot timeslot = smartConsumerTimeslotRepository.findById(smartConsumerTimeslotId).orElse(null);
		if (timeslot != null) {
			if (securityService.canAccessTimeslot(timeslot)) {
				Map<String, String> response = Map.of(
						"message", "Successfully deleted SmartConsumerTimeslot with ID " + smartConsumerTimeslotId,
						"id", smartConsumerTimeslotId.toString()
				);
				smartConsumerTimeslotRepository.deleteById(smartConsumerTimeslotId);
				return response;
			}
			throw new UnauthorizedAccessException("Unauthorized access to SmartConsumerTimeslot with ID " + smartConsumerTimeslotId);
		}
		throw new DeviceNotFoundException("SmartConsumerTimeslot with ID " + smartConsumerTimeslotId + " not found");
	}
}
