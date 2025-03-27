package com.homebuilder.service;

import com.homebuilder.dto.StorageRequest;
import com.homebuilder.dto.StorageResponse;
import com.homebuilder.entity.Storage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
	Page<StorageResponse> getAllStoragesByOwnerAndRoomIsNull(Pageable pageable);
	Page<StorageResponse> getAllStoragesByOwnerAndRoomId(Long roomId, Pageable pageable);
	Storage getStorageById(Long storageId);
	Storage updateStorage(StorageRequest request);
	Map<String, String> setActive(Storage storage, boolean active, boolean sendEvent);
	Map<String, String> archiveStorage(Long storageId);
	Map<String, String> deleteStorage(Long storageId);
	Map<String, String> deleteAllStoragesByOwnerId(Long ownerId);

	Page<StorageResponse> getAllUnarchivedByUser(Pageable pageable);

}
