package com.homebuilder.service;

import com.homebuilder.entity.Device;
import com.homebuilder.exception.DeviceNotFoundException;
import com.homebuilder.security.SecurityService;
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

	private final SecurityService securityService;

	@Autowired
	public DeviceServiceImpl(ConsumerService consumerService, ProducerService producerService, StorageService storageService, SecurityService securityService) {
		this.consumerService = consumerService;
		this.producerService = producerService;
		this.storageService = storageService;
		this.securityService = securityService;
	}

	@Override
	public List<Device> getAllDevicesFromUser() {
		List<Device> devices = new ArrayList<>();
		try {
			devices.addAll(consumerService.getAllConsumersFromUser());
		} catch (Exception ignored) {}
		try {
			devices.addAll(producerService.getAllProducersFromUser());
		} catch (Exception ignored) {}
		try {
			devices.addAll(storageService.getAllStoragesFromUser());
		} catch (Exception ignored) {}
		return devices;
	}

	public Device getDeviceByIdFromUser(Long deviceId) {
		Long userId = securityService.getCurrentUserId();
		Optional<Device> device = Optional.ofNullable(consumerService.getConsumerById(deviceId));
		if (device.isEmpty()) {
			device = Optional.ofNullable(producerService.getProducerById(deviceId));
		}
		if (device.isEmpty()) {
			device = Optional.ofNullable(storageService.getStorageById(deviceId));
		}
		return device.filter(d -> d.getUserId().equals(userId)).orElseThrow(() -> new DeviceNotFoundException("Device with ID " + deviceId + " not found or not accessible by user with ID " + userId));
	}

	@Override
	public List<Device> getAllDevices() {
		List<Device> devices = new ArrayList<>();

		devices.addAll(consumerService.getAllConsumers());
		devices.addAll(producerService.getAllProducers());
		devices.addAll(storageService.getAllStorages());

		return devices;
	}

	@Override
	public Device getDeviceById(Long deviceId) {
		Optional<Device> device = Optional.ofNullable(consumerService.getConsumerById(deviceId));
		if (device.isEmpty()) {
			device = Optional.ofNullable(producerService.getProducerById(deviceId));
		}
		if (device.isEmpty()) {
			device = Optional.ofNullable(storageService.getStorageById(deviceId));
		}
		return device.orElseThrow(() -> new DeviceNotFoundException("Device with ID " + deviceId + " not found"));
	}
}
