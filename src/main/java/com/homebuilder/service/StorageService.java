package com.homebuilder.service;

import com.homebuilder.dto.StorageRequest;
import com.homebuilder.entity.Storage;

import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
public interface StorageService {

	Storage createStorage(StorageRequest request);
	List<Storage> createStorageList(List<StorageRequest> request);
	List<Storage> getAllStorages();
	List<Storage> getAllUnarchivedStorages();
	List<Storage> getAllStoragesByOwner(Long ownerId);
	Storage getStorageById(Long storageId);
	Storage updateStorage(StorageRequest request);
	Map<String, String> setActive(Storage storage, boolean active);
	Map<String, String> archiveStorage(Long storageId);
	Map<String, String> deleteStorage(Long storageId);
	Map<String, String> deleteAllStoragesByOwnerId(Long ownerId);

}
