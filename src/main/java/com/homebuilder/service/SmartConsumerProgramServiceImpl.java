package com.homebuilder.service;

import com.homebuilder.dto.SmartConsumerProgramRequest;
import com.homebuilder.entity.SmartConsumer;
import com.homebuilder.entity.SmartConsumerProgram;
import com.homebuilder.exception.CreateDeviceFailedException;
import com.homebuilder.exception.DeviceNotFoundException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.repository.SmartConsumerProgramRepository;
import com.homebuilder.repository.SmartConsumerRepository;
import com.homebuilder.security.SecurityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author André Heinen
 */
@Service
public class SmartConsumerProgramServiceImpl implements SmartConsumerProgramService {

	private final SmartConsumerProgramRepository smartConsumerProgramRepository;
	private final SmartConsumerRepository smartConsumerRepository;

	private final SecurityService securityService;

	@Autowired
	public SmartConsumerProgramServiceImpl(SmartConsumerProgramRepository smartConsumerProgramRepository, SmartConsumerRepository smartConsumerRepository, SecurityService securityService) {
		this.smartConsumerProgramRepository = smartConsumerProgramRepository;
		this.smartConsumerRepository = smartConsumerRepository;
		this.securityService = securityService;
	}

	@Override
	@Transactional
	public SmartConsumerProgram createSmartConsumerProgram(@Valid SmartConsumerProgramRequest request) {
		SmartConsumerProgram smartConsumerProgram = request.toEntity();
		if (securityService.isCurrentUserAdminOrSystem()) {
			if (request.getOwnerId() == null) {
				throw new CreateDeviceFailedException("Owner ID must be provided when creating SmartConsumerProgram as System User");
			} else {
				smartConsumerProgram.setUserId(request.getOwnerId());
			}
		} else {
			smartConsumerProgram.setUserId(securityService.getCurrentUserId());
		}
		SmartConsumer smartConsumer = smartConsumerRepository.findById(request.getSmartConsumerId()).orElse(null);
		if (smartConsumer != null) {
			if (securityService.canAccessDevice(smartConsumer)) {
				smartConsumerProgram.setSmartConsumer(smartConsumer);
				smartConsumer.getProgramList().add(smartConsumerProgram);
				smartConsumerProgramRepository.save(smartConsumerProgram);
				smartConsumerRepository.save(smartConsumer);
				return smartConsumerProgram;
			}
			throw new UnauthorizedAccessException("Unauthorized access to SmartConsumer with ID " + request.getSmartConsumerId());
		}
		throw new DeviceNotFoundException("SmartConsumer with ID " + request.getSmartConsumerId() + " not found");
	}

	@Override
	@Transactional(readOnly = true)
	public List<SmartConsumerProgram> getAllSmartConsumerPrograms() {
		if (securityService.isCurrentUserAdminOrSystem()) {
			return smartConsumerProgramRepository.findAll(PageRequest.of(0, 1000)).getContent();
		}
		Long userId = securityService.getCurrentUserId();
		return smartConsumerProgramRepository.findByUserId(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<SmartConsumerProgram> getAllUnarchivedSmartConsumerPrograms() {
		if (securityService.isCurrentUserAdminOrSystem()) {
			return smartConsumerProgramRepository.findAllByArchivedFalse(PageRequest.of(0, 1000)).getContent();
		}
		Long userId = securityService.getCurrentUserId();
		return smartConsumerProgramRepository.findAllByUserIdAndArchivedFalse(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<SmartConsumerProgram> getAllSmartConsumerProgramsByOwner(Long ownerId) {
		if (securityService.isCurrentUserAdminOrSystem()) {
			return smartConsumerProgramRepository.findByUserId(ownerId);
		} else {
			throw new UnauthorizedAccessException("Unauthorized access to SmartConsumerPrograms for Owner with ID " + ownerId);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public SmartConsumerProgram getSmartConsumerProgramById(Long smartConsumerProgramId) {
		SmartConsumerProgram smartConsumerProgram = smartConsumerProgramRepository.findById(smartConsumerProgramId).orElse(null);
		if (smartConsumerProgram != null) {
			if (securityService.canAccessProgram(smartConsumerProgram)) {
				return smartConsumerProgram;
			} else {
				throw new UnauthorizedAccessException("Unauthorized access to SmartConsumerProgram with ID " + smartConsumerProgramId);
			}
		}
		return null;
	}

	@Override
	@Transactional
	public SmartConsumerProgram updateSmartConsumerProgram(@Valid SmartConsumerProgramRequest request) {
		if (request.getId() == null) {
			throw new CreateDeviceFailedException("SmartConsumerProgram ID must be provided when updating SmartConsumerProgram");
		}
		SmartConsumerProgram program = smartConsumerProgramRepository.findById(request.getId()).orElse(null);
		if (program != null) {
			if (securityService.canAccessProgram(program)) {
				SmartConsumer smartConsumer = smartConsumerRepository.findById(request.getSmartConsumerId()).orElse(null);
				if (smartConsumer != null) {
					if (securityService.canAccessDevice(smartConsumer)) {
						program.setName(request.getName());
						program.setDurationInSeconds(request.getDurationInSeconds());
						program.setPowerConsumption(request.getPowerConsumption());
						if (!Objects.equals(program.getName(), request.getName()) || program.getDurationInSeconds() != request.getDurationInSeconds() || program.getPowerConsumption() != request.getPowerConsumption()) {
							smartConsumerProgramRepository.save(program);
							return program;
						}
						throw new CreateDeviceFailedException("No changes detected in SmartConsumerProgram");
					}
					throw new UnauthorizedAccessException("Unauthorized access to SmartConsumer with ID " + request.getSmartConsumerId());
				}
				throw new DeviceNotFoundException("SmartConsumer with ID " + request.getSmartConsumerId() + " not found");
			}
			throw new UnauthorizedAccessException("Unauthorized access to SmartConsumerProgram with ID " + request.getId());
		}
		throw new DeviceNotFoundException("SmartConsumerProgram with ID " + request.getId() + " not found");
	}

	@Override
	@Transactional
	public Map<String, String> archiveSmartConsumerProgram(Long smartConsumerProgramId) {
		SmartConsumerProgram program = smartConsumerProgramRepository.findById(smartConsumerProgramId).orElse(null);
		if (program != null) {
			if (securityService.canAccessProgram(program)) {
				if (!program.isArchived()) {
					program.setArchived(true);
					Map<String, String> response = Map.of(
							"message", "Successfully archived SmartConsumerProgram with ID " + smartConsumerProgramId,
							"id", smartConsumerProgramId.toString()
					);
					smartConsumerProgramRepository.save(program);
					return response;
				}
				throw new CreateDeviceFailedException("SmartConsumerProgram with ID " + smartConsumerProgramId + " is already archived");
			}
			throw new UnauthorizedAccessException("Unauthorized access to SmartConsumerProgram with ID " + smartConsumerProgramId);
		}
		throw new DeviceNotFoundException("SmartConsumerProgram with ID " + smartConsumerProgramId + " not found");
	}

	@Override
	@Transactional
	public Map<String, String> deleteSmartConsumerProgram(Long smartConsumerProgramId) {
		SmartConsumerProgram program = smartConsumerProgramRepository.findById(smartConsumerProgramId).orElse(null);
		if (program != null) {
			if (securityService.canAccessProgram(program)) {
				Map<String, String> response = Map.of(
						"message", "Successfully deleted SmartConsumerProgram with ID " + smartConsumerProgramId,
						"id", smartConsumerProgramId.toString()
				);
				smartConsumerProgramRepository.delete(program);
				return response;
			}
			throw new UnauthorizedAccessException("Unauthorized access to SmartConsumerProgram with ID " + smartConsumerProgramId);
		}
		throw new DeviceNotFoundException("SmartConsumerProgram with ID " + smartConsumerProgramId + " not found");
	}
}
