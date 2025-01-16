package com.homebuilder.service;

import com.homebuilder.entity.Consumer;
import com.homebuilder.entity.Device;
import com.homebuilder.entity.Producer;
import com.homebuilder.entity.Storage;
import com.homebuilder.exception.DeviceNotFoundException;
import com.homebuilder.security.SecurityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

	private final KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	public DeviceServiceImpl(ConsumerService consumerService, ProducerService producerService, StorageService storageService, SecurityService securityService, KafkaTemplate<String, String> kafkaTemplate) {
		this.consumerService = consumerService;
		this.producerService = producerService;
		this.storageService = storageService;
		this.securityService = securityService;
		this.kafkaTemplate = kafkaTemplate;
	}

	private void publishDeviceActivity(boolean active, Device device) {
		String event = String.format("{\"deviceId\": %d, \"deviceType\": \"%s\", \"active\": %b, \"timestamp\": \"%s\"}", device.getId(), device.getClass().getSimpleName(), active, LocalDateTime.now());
		kafkaTemplate.send("device-events", event).whenComplete((result, exception) -> {
			if (exception != null) {
				System.err.println("Fehler beim Senden des Events: " + exception.getMessage());
			}
		});
	}

	private Device saveDevice(Long deviceId, Device existingDevice) {
		if (existingDevice instanceof Consumer) {
			consumerService.updateConsumer(deviceId, (Consumer) existingDevice);
		} else if (existingDevice instanceof Producer) {
			producerService.updateProducer(deviceId, (Producer) existingDevice);
		} else if (existingDevice instanceof Storage) {
			storageService.updateStorage(deviceId, (Storage) existingDevice);
		}
		return existingDevice;
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

	@Override
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
	public Device updateDeviceForUser(Long deviceId, @Valid Device device) {
		Device existingDevice = getDeviceByIdFromUser(deviceId);
		return saveDevice(deviceId, existingDevice);
	}

	@Override
	public Map<String, String> toggleDeviceOnOffForUser(Long deviceId, boolean active) {
		Device device = getDeviceByIdFromUser(deviceId);
		device.setActive(active);
		Device updatedDevice = updateDeviceForUser(deviceId, device);
		publishDeviceActivity(active, updatedDevice);
		Map<String, String> response = Map.of(
				"message", "Successfully updated Device with ID " + updatedDevice.getId(),
				"id", updatedDevice.getId().toString(),
				"active", updatedDevice.isActive() ? "true" : "false"
		);
		return response;
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

	@Override
	public Device updateDevice(Long deviceId, @Valid Device device) {
		Device existingDevice = getDeviceById(deviceId);
		return saveDevice(deviceId, existingDevice);
	}

	@Override
	public Map<String, String> toggleDeviceOnOff(Long deviceId, boolean active) {
		Device device = getDeviceById(deviceId);
		device.setActive(active);
		Device updatedDevice = updateDevice(deviceId, device);
		publishDeviceActivity(active, updatedDevice);
		Map<String, String> response = Map.of(
				"message", "Successfully updated Device with ID " + updatedDevice.getId(),
				"id", updatedDevice.getId().toString(),
				"active", updatedDevice.isActive() ? "true" : "false"
		);
		return response;
	}
}
