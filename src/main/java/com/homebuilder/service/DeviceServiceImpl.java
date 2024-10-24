package com.homebuilder.service;

import com.homebuilder.entity.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
	public List<Device> getAllDevicesFromUser(Long userId) {
		List<Device> devices = new ArrayList<>();

		devices.addAll(consumerService.getAllConsumersFromUser(userId));
		devices.addAll(producerService.getAllProducersFromUser(userId));
		devices.addAll(storageService.getAllStoragesFromUser(userId));

		return devices;
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
