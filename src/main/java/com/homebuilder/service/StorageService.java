package com.homebuilder.service;

import com.homebuilder.entity.Storage;

import java.util.List;

/**
 * @author André Heinen
 */
public interface StorageService {

	// CRUD-Operationen für SH-Nutzer (basierend auf Benutzer-ID)
	Storage createStorageForUser(Storage storage, Long userId);
	List<Storage> getAllStoragesFromUser(Long userId);
	Storage getStorageByIdFromUser(Long storageId, Long userId);
	Storage updateStorageForUser(Long storageId, Long userId, Storage storageDetails);
	void deleteStorageForUser(Long storageId, Long userId);

	// CRUD-Operationen für administrative Aufgaben (keine Einschränkung auf Benutzer-ID)
	List<Storage> getAllStorages();
	Storage getStorageById(Long storageId);
}
