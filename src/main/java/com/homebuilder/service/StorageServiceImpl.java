package com.homebuilder.service;

import com.homebuilder.dto.StorageRequest;
import com.homebuilder.dto.StorageResponse;
import com.homebuilder.entity.Room;
import com.homebuilder.entity.Storage;
import com.homebuilder.exception.CreateDeviceFailedException;
import com.homebuilder.exception.DeviceNotFoundException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.repository.RoomRepository;
import com.homebuilder.repository.StorageRepository;
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
public class StorageServiceImpl implements StorageService {

	private static final Logger logger = LoggerFactory.getLogger(StorageServiceImpl.class);

	private final StorageRepository storageRepository;

	private final SecurityService securityService;

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final RoomRepository roomRepository;

	@Autowired
	public StorageServiceImpl(StorageRepository storageRepository, SecurityService securityService, KafkaTemplate<String, String> kafkaTemplate, RoomRepository roomRepository) {
		this.storageRepository = storageRepository;
		this.securityService = securityService;
		this.kafkaTemplate = kafkaTemplate;
		this.roomRepository = roomRepository;
	}

	@Override
	@Transactional
	public Storage createStorage(@Valid StorageRequest request) {
		Storage storage = request.toEntity();
		Long roomId = request.getRoomId();
		if (securityService.isCurrentUserAdminOrSystem()) {
			if (request.getOwnerId() == null) {
				throw new CreateDeviceFailedException("Owner ID must be provided when creating Storage as System User");
			} else {
				storage.setUserId(request.getOwnerId());
			}
		} else {
			storage.setUserId(securityService.getCurrentUserId());
		}
		storageRepository.save(storage);
		if (roomId != null) {
			Room room = roomRepository.findById(roomId).orElse(null);
			if (room != null) {
				if (securityService.getCurrentUserId().equals(room.getUserId())) {
					room.addDevice(storage);
					storage.setRoom(room);
					roomRepository.save(room);
				} else {
					throw new UnauthorizedAccessException("Unauthorized access to Room with ID " + roomId);
				}
			}
		}
		return storage;
	}

	@Override
	@Transactional
	public List<Storage> createStorageList(List<@Valid StorageRequest> request) {
		List<Storage> storageList = request.stream().map(StorageRequest::toEntity).toList();
		if (securityService.isCurrentUserAdminOrSystem()) {
			for (StorageRequest storageRequest : request) {
				if (storageRequest.getOwnerId() == null) {
					throw new CreateDeviceFailedException("Owner ID must be provided when creating Storages as System User");
				} else {
					storageList.forEach(storage -> storage.setUserId(storageRequest.getOwnerId()));
				}
			}
		} else {
			storageList.forEach(storage -> storage.setUserId(securityService.getCurrentUserId()));
		}
		storageRepository.saveAll(storageList);
		return storageList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Storage> getAllStorages() {
		if (securityService.isCurrentUserAdminOrSystem()) {
			return storageRepository.findAll(PageRequest.of(0, 1000)).getContent();
		}
		Long userId = securityService.getCurrentUserId();
		return storageRepository.findByUserId(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Storage> getAllUnarchivedStorages() {
		if (securityService.isCurrentUserAdminOrSystem()) {
			return storageRepository.findAllByArchivedFalse(PageRequest.of(0, 1000)).getContent();
		}
		Long userId = securityService.getCurrentUserId();
		return storageRepository.findAllByUserIdAndArchivedFalse(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Storage> getAllStoragesByOwner(Long ownerId) {
		if (securityService.isCurrentUserAdminOrSystem()) {
			return storageRepository.findByUserId(ownerId);
		} else {
			throw new UnauthorizedAccessException("Unauthorized access to Storages for Owner with ID " + ownerId);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<StorageResponse> getAllStoragesByOwnerAndRoomIsNull(Pageable pageable) {
		Long userId = securityService.getCurrentUserId();
		Page<Storage> storagePage = storageRepository.findByUserIdAndRoomIsNull(userId, pageable);
		return storagePage.map(StorageResponse::new);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<StorageResponse> getAllStoragesByOwnerAndRoomId(Long roomId, Pageable pageable) {
		Long userId = securityService.getCurrentUserId();
		Page<Storage> list = storageRepository.findByRoomId(roomId, pageable);
		if (!list.isEmpty()) {
			if (!Objects.equals(list.getContent().getFirst().getUserId(), userId)) {
				throw new UnauthorizedAccessException("Unauthorized access to Storages for Owner with ID " + userId);
			}
		}
		return list.map(StorageResponse::new);
	}

	@Override
	@Transactional(readOnly = true)
	public Storage getStorageById(Long storageId) {
		Storage storage = storageRepository.findById(storageId).orElse(null);
		if (storage != null) {
			if (securityService.canAccessDevice(storage)) {
				return storage;
			} else {
				throw new UnauthorizedAccessException("Unauthorized access to Storage with ID " + storageId);
			}
		}
		return null;
	}

	@Override
	@Transactional
	public Storage updateStorage(@Valid StorageRequest request) {
		if (request.getId() == null) {
			throw new CreateDeviceFailedException("Storage ID must be provided when updating Storage");
		}
		Storage storage = storageRepository.findById(request.getId()).orElse(null);
		if (storage != null) {
			if (securityService.canAccessDevice(storage)) {
				boolean changed = false;
				if (!Objects.equals(request.getName(), storage.getName())) {
					storage.setName(request.getName());
					changed = true;
				}
				if (request.getCapacity() != storage.getCapacity()) {
					if (storage.isActive()) {
						setActive(storage, false);
					}
					storage.setCapacity(request.getCapacity());
					changed = true;
				}
				if (changed) {
					storageRepository.save(storage);
					return storage;
				}
				throw new CreateDeviceFailedException("No changes detected in Storage");
			}
			throw new UnauthorizedAccessException("Unauthorized access to Storage with ID " + request.getId());
		}
		return null;
	}

	@Override
	@Transactional
	public Map<String, String> setActive(Storage storage, boolean active) {
		if (storage != null) {
			if (securityService.canAccessDevice(storage)) {
				if (storage.isArchived()) {
					throw new CreateDeviceFailedException("Storage with ID " + storage.getId() + " is archived and cannot be activated or deactivated");
				}
				if (storage.isActive() && active) {
					throw new CreateDeviceFailedException("Storage with ID " + storage.getId() + " is already active");
				}
				if (!storage.isActive() && !active) {
					throw new CreateDeviceFailedException("Storage with ID " + storage.getId() + " is already inactive");
				}
				storage.setActive(active);
				storageRepository.save(storage);
				String event = String.format(
						Locale.ENGLISH,
						"{\"deviceId\": %d, \"ownerId\": %d, \"commercial\": %b, \"active\": %b, \"capacity\": %f, \"currentCharge\": %f, \"chargingPriority\": %d, \"consumingPriority\": %d, \"timestamp\": \"%s\"}",
						storage.getId(), storage.getUserId(), securityService.isCurrentUserACommercialUser(), active, storage.getCapacity(), storage.getCurrentCharge(), storage.getChargingPriority(), storage.getConsumingPriority(), Instant.now());
				kafkaTemplate.send("storage-events", event).whenComplete((result, exception) -> {
					if (exception != null) {
						logger.error("Fehler beim Senden des Events: {}", exception.getMessage());
					}
				});
				return Map.of(
						"message", "Successfully updated Storage with ID " + storage.getId(),
						"id", storage.getId().toString(),
						"active", storage.isActive() ? "true" : "false"
				);
			}
		}
		throw new DeviceNotFoundException("Storage not found");
	}

	@Override
	@Transactional
	public Map<String, String> archiveStorage(Long storageId) {
		Storage storage = storageRepository.findById(storageId).orElse(null);
		if (storage != null) {
			if (securityService.canAccessDevice(storage)) {
				if (!storage.isArchived()) {
					storage.setArchived(true);
					if (storage.isActive()) {
						setActive(storage, false);
					}
					Map<String, String> response = Map.of(
							"message", "Successfully archived Storage with ID " + storageId,
							"id", storageId.toString()
					);
					storageRepository.save(storage);
					return response;
				} else {
					throw new CreateDeviceFailedException("Storage with ID " + storageId + " is already archived");
				}
			}
			throw new UnauthorizedAccessException("Unauthorized access to Storage with ID " + storageId);
		}
		return null;
	}

	@Override
	@Transactional
	public Map<String, String> deleteStorage(Long storageId) {
		Storage storage = storageRepository.findById(storageId).orElse(null);
		if (storage != null) {
			if (securityService.canAccessDevice(storage)) {
				storage.getRoom().removeDevice(storage);
				storage.setRoom(null);
				Map<String, String> response = Map.of(
						"message", "Successfully deleted Storage with ID " + storageId,
						"id", storageId.toString()
				);
				storageRepository.deleteById(storageId);
				return response;
			}
			throw new UnauthorizedAccessException("Unauthorized access to Storage with ID " + storageId);
		}
		return null;
	}

	@Override
	@Transactional
	public Map<String, String> deleteAllStoragesByOwnerId(Long ownerId) {
		if (securityService.isCurrentUserAdminOrSystem()) {
			storageRepository.deleteAllByUserId(ownerId);
		} else {
			throw new UnauthorizedAccessException("Unauthorized access to Storages for Owner with ID " + ownerId);
		}
		return Map.of(
				"message", "Successfully deleted all Storages for Owner with ID " + ownerId
		);
	}

	@Override
	public Page<StorageResponse> getAllUnarchivedByUser(Pageable pageable) {
		Long userId = securityService.getCurrentUserId();
		Page<Storage> storagePage = storageRepository.findByUserIdAndArchivedFalse(userId, pageable);
		return storagePage.map(StorageResponse::new);
	}
}
