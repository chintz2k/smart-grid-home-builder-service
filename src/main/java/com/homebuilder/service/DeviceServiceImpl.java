package com.homebuilder.service;

import com.homebuilder.entity.Device;
import com.homebuilder.exception.DeviceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	// SH-Nutzer
	@Override
	public List<Device> getAllDevicesByUser(Long userId) {
		List<Device> devices = new ArrayList<>();

		devices.addAll(consumerService.getAllConsumersFromUser(userId));
		devices.addAll(producerService.getAllProducersFromUser(userId));
		devices.addAll(storageService.getAllStoragesFromUser(userId));

		return devices;
	}

	public Device getDeviceByIdAndUser(Long deviceId, Long userId) {
		Optional<Device> device = Optional.ofNullable(consumerService.getConsumerById(deviceId));
		if (device.isEmpty()) {
			device = Optional.ofNullable(producerService.getProducerById(deviceId));
		}
		if (device.isEmpty()) {
			device = Optional.ofNullable(storageService.getStorageById(deviceId));
		}
		return device.filter(d -> d.getUserId().equals(userId))
				.orElseThrow(() -> new DeviceNotFoundException("Device with ID " + deviceId + " not found or not accessible by user with ID " + userId));
	}

	// Admin
	@Override
	public List<Device> getAllDevices() {
		List<Device> devices = new ArrayList<>();

		devices.addAll(consumerService.getAllConsumers());
		devices.addAll(producerService.getAllProducers());
		devices.addAll(storageService.getAllStorages());

		return devices;
	}
}
