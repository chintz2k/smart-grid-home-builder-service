package com.homebuilder.service;

import com.homebuilder.entity.Consumer;
import com.homebuilder.entity.Device;
import com.homebuilder.entity.Producer;
import com.homebuilder.entity.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
@Service
public class DeviceServiceImpl implements DeviceService {

	private final ConsumerService consumerService;
	private final ProducerService producerService;
	private final StorageService storageService;

	@Autowired
	public DeviceServiceImpl(ConsumerService consumerService, ProducerService producerService, StorageService storageService) {
		this.consumerService = consumerService;
		this.producerService = producerService;
		this.storageService = storageService;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Device> getAllDevices() {
		List<Device> deviceList = new ArrayList<>();
		deviceList.addAll(consumerService.getAllConsumers());
		deviceList.addAll(producerService.getAllProducers());
		deviceList.addAll(storageService.getAllStorages());
		return deviceList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Device> getAllUnarchivedDevices() {
		List<Device> deviceList = new ArrayList<>();
		deviceList.addAll(consumerService.getAllUnarchivedConsumers());
		deviceList.addAll(producerService.getAllUnarchivedProducers());
		deviceList.addAll(storageService.getAllUnarchivedStorages());
		return deviceList;
	}

	@Override
	@Transactional(readOnly = true)
	public Device getDeviceById(Long deviceId) {
		Consumer consumer = consumerService.getConsumerById(deviceId);
		if (consumer != null) {
			return consumer;
		}
		Producer producer = producerService.getProducerById(deviceId);
		if (producer != null) {
			return producer;
		}
		return storageService.getStorageById(deviceId);
	}

	@Override
	@Transactional
	public Map<String, String> setActive(Long deviceId, boolean active) {
		Device device = getDeviceById(deviceId);
		return switch (device) {
			case Consumer ignored -> consumerService.setActive((Consumer) device, active);
			case Producer ignored -> producerService.setActive((Producer) device, active);
			case Storage ignored -> storageService.setActive((Storage) device, active);
			case null, default -> Map.of("message", "Device with ID " + deviceId + " not found");
		};
	}

	@Override
	@Transactional
	public List<Map<String, String>> deleteAllDevicesByOwner(Long ownerId) {
		List<Map<String, String>> list = new ArrayList<>();
		list.add(consumerService.deleteAllConsumersByOwnerId(ownerId));
		list.add(producerService.deleteAllProducersByOwnerId(ownerId));
		list.add(storageService.deleteAllStoragesByOwnerId(ownerId));
		return list;
	}
}
