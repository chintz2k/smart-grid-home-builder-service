package com.homebuilder.service;

import com.homebuilder.dto.StorageRequest;
import com.homebuilder.entity.Storage;

import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
public interface StorageService {

	Storage createStorageForUser(StorageRequest request);
	List<Storage> getAllStoragesFromUser();
	Storage getStorageByIdFromUser(Long storageId);
	Storage updateStorageForUser(Long storageId, StorageRequest request);
	Map<String, String> archiveStorageForUser(Long storageId);
	Map<String, String> deleteStorageForUser(Long storageId);

	List<Storage> getAllStorages();
	Storage getStorageById(Long storageId);
	Storage updateStorage(Long storageId, Storage request);

}
