package com.homebuilder.service;

import com.homebuilder.entity.Storage;

import java.util.List;

/**
 * @author André Heinen
 */
public interface StorageService {

	// CRUD-Operationen für SH-Nutzer (basierend auf Benutzer-ID)
	Storage createStorage(Storage storage, Long ownerId);
	List<Storage> getStoragesForUser(Long ownerId);
	Storage getStorageForUser(Long storageId, Long ownerId);
	Storage updateStorageForUser(Long storageId, Long ownerId, Storage storageDetails);
	void deleteStorageForUser(Long storageId, Long ownerId);

	// CRUD-Operationen für administrative Aufgaben (keine Einschränkung auf Benutzer-ID)
	List<Storage> getAllStorages();
	Storage getStorageById(Long storageId);
}
