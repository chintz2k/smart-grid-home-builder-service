package com.homebuilder.service;

import com.homebuilder.entity.Storage;
import com.homebuilder.exception.DeviceNotFoundException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.repository.StorageRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author André Heinen
 */
@Service
public class StorageServiceImpl implements StorageService {

	private final StorageRepository storageRepository;

	@Autowired
	public StorageServiceImpl(StorageRepository storageRepository) {
		this.storageRepository = storageRepository;
	}

	// CRUD-Operationen für SH-Nutzer
	@Override
	public Storage createStorageForUser(@Valid Storage storage, Long userId) {
		storage.setUserId(userId);
		return storageRepository.save(storage);
	}

	@Override
	public List<Storage> getAllStoragesFromUser(Long userId) {
		return storageRepository.findByUserId(userId).orElseThrow(() -> new DeviceNotFoundException("No Storages found for User with ID " + userId));
	}

	@Override
	public Storage getStorageByIdFromUser(Long storageId, Long userId) {
		Storage storage = storageRepository.findById(storageId).orElseThrow(() -> new DeviceNotFoundException("Storage with ID " + storageId + " not found"));
		if (!storage.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to storage with ID " + storageId);
		}
		return storage;
	}

	@Override
	public Storage updateStorageForUser(Long storageId, Long userId, @Valid Storage storageDetails) {
		Storage storage = storageRepository.findById(storageId).orElseThrow(() -> new DeviceNotFoundException("Storage with ID " + storageId + " not found"));
		if (!storage.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to storage with ID " + storageId);
		}
		storage.setName(storageDetails.getName());
		storage.setCapacity(storageDetails.getCapacity());
		storage.setCurrentCharge(storageDetails.getCurrentCharge());
		return storageRepository.save(storage);
	}

	@Override
	public void deleteStorageForUser(Long storageId, Long userId) {
		Storage storage = storageRepository.findById(storageId).orElseThrow(() -> new DeviceNotFoundException("Storage with ID " + storageId + " not found"));
		if (!storage.getUserId().equals(userId)) {
			throw new UnauthorizedAccessException("Unauthorized access to storage with ID " + storageId);
		}
		storageRepository.delete(storage);
	}

	// CRUD-Operationen für administrative Aufgaben
	@Override
	public List<Storage> getAllStorages() {
		return storageRepository.findAll();
	}

	@Override
	public Storage getStorageById(Long storageId) {
		return storageRepository.findById(storageId).orElseThrow(() -> new DeviceNotFoundException("Storage with ID " + storageId + " not found"));
	}
}
