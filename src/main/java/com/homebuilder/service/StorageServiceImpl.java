package com.homebuilder.service;

import com.homebuilder.dto.StorageRequest;
import com.homebuilder.entity.Storage;
import com.homebuilder.exception.DeviceNotFoundException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.repository.StorageRepository;
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
public class StorageServiceImpl implements StorageService {

	private final StorageRepository storageRepository;

	private final SecurityService securityService;

	@Autowired
	public StorageServiceImpl(StorageRepository storageRepository, SecurityService securityService) {
		this.storageRepository = storageRepository;
		this.securityService = securityService;
	}

	@Override
	public Storage createStorageForUser(@Valid StorageRequest request) {
		Long userId = securityService.getCurrentUserId();
		Storage storage = request.toEntity();
		storage.setUserId(userId);
		storageRepository.save(storage);
		return storage;
	}

	@Override
	public List<Storage> getAllStoragesFromUser() {
		Long userId = securityService.getCurrentUserId();
		List<Storage> storageList = storageRepository.findByUserId(userId);
		if (storageList.isEmpty()) {
			throw new DeviceNotFoundException("No Storages found for User with ID " + userId);
		}
		return storageList;
	}

	@Override
	public Storage getStorageByIdFromUser(Long storageId) {
		Long userId = securityService.getCurrentUserId();
		Storage storage = storageRepository.findById(storageId).orElseThrow(() -> new DeviceNotFoundException("Storage with ID " + storageId + " not found"));
		if (!storage.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to storage with ID " + storageId);
		}
		return storage;
	}

	@Override
	public Storage updateStorageForUser(Long existingStorageId, @Valid StorageRequest request) {
		Long userId = securityService.getCurrentUserId();
		Storage existingStorage = getStorageByIdFromUser(existingStorageId);
		if (!existingStorage.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to storage with ID " + existingStorageId);
		}
		existingStorage.setName(request.getName());
		existingStorage.setActive(request.isActive());
		existingStorage.setArchived(request.isArchived());
		existingStorage.setCapacity(request.getCapacity());
		existingStorage.setChargingPriority(request.getChargingPriority());
		existingStorage.setConsumingPriority(request.getConsumingPriority());
		storageRepository.save(existingStorage);
		return existingStorage;
	}

	@Override
	public Map<String, String> deleteStorageForUser(Long storageId) {
		Long userId = securityService.getCurrentUserId();
		Storage storage = getStorageByIdFromUser(storageId);
		if (!storage.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to storage with ID " + storageId);
		}
		Map<String, String> response = Map.of(
				"message", "Successfully deleted SmartConsumer with ID " + storageId,
				"id", storageId.toString()
		);
		storageRepository.delete(storage);
		return response;
	}

	@Override
	public List<Storage> getAllStorages() {
		return storageRepository.findAll();
	}

	@Override
	public Storage getStorageById(Long storageId) {
		return storageRepository.findById(storageId).orElseThrow(() -> new DeviceNotFoundException("Storage with ID " + storageId + " not found"));
	}

	@Override
	public Storage updateStorage(Long storageId, @Valid Storage request) {
		Storage storage = getStorageById(storageId);
		storage.setName(request.getName());
		storage.setActive(request.isActive());
		storage.setArchived(request.isArchived());
		storage.setCapacity(request.getCapacity());
		storage.setCurrentCharge(request.getCurrentCharge());
		storage.setChargingPriority(request.getChargingPriority());
		storage.setConsumingPriority(request.getConsumingPriority());
		storageRepository.save(storage);
		return storage;
	}
}
