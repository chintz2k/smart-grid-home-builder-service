package com.homebuilder.service;

import com.homebuilder.entity.Storage;
import com.homebuilder.exception.DeviceNotFoundException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.repository.StorageRepository;
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
	public Storage createStorage(Storage storage, Long ownerId) {
		storage.setOwnerId(ownerId);
		return storageRepository.save(storage);
	}

	@Override
	public List<Storage> getStoragesForUser(Long ownerId) {
		return storageRepository.findByOwnerId(ownerId);
	}

	@Override
	public Storage getStorageForUser(Long storageId, Long ownerId) {
		Storage storage = storageRepository.findById(storageId)
				.orElseThrow(() -> new DeviceNotFoundException("Storage with ID " + storageId + " not found"));
		if (!storage.getOwnerId().equals(ownerId)) {
			throw new UnauthorizedAccessException("Unauthorized access to storage with ID " + storageId);
		}
		return storage;
	}

	@Override
	public Storage updateStorageForUser(Long storageId, Long ownerId, Storage storageDetails) {
		Storage storage = storageRepository.findById(storageId)
				.orElseThrow(() -> new DeviceNotFoundException("Storage with ID " + storageId + " not found"));
		if (!storage.getOwnerId().equals(ownerId)) {
			throw new UnauthorizedAccessException("Unauthorized access to storage with ID " + storageId);
		}
		storage.setName(storageDetails.getName());
		return storageRepository.save(storage);
	}

	@Override
	public void deleteStorageForUser(Long storageId, Long ownerId) {
		Storage storage = storageRepository.findById(storageId)
				.orElseThrow(() -> new DeviceNotFoundException("Storage with ID " + storageId + " not found"));
		if (!storage.getOwnerId().equals(ownerId)) {
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
		return storageRepository.findById(storageId)
				.orElseThrow(() -> new DeviceNotFoundException("Storage with ID " + storageId + " not found"));
	}
}
