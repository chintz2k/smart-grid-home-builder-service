package com.homebuilder.service;

import com.homebuilder.entity.*;
import com.homebuilder.exception.DeviceNotFoundException;
import com.homebuilder.security.SecurityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
		if (device.isArchived()) {
			return;
		}
		if (device instanceof Consumer consumer) {
			if (device instanceof SmartConsumer smartConsumer) {
				String event = String.format("{\"deviceId\": %d, \"deviceType\": \"%s\", \"ownerId\": %d, \"commercial\": %b, \"active\": %b, \"powerConsumption\": %f, \"timestamp\": \"%s\"}",
						smartConsumer.getId(), smartConsumer.getClass().getSimpleName(), smartConsumer.getUserId(), securityService.isCommercialUser(), active, smartConsumer.getPowerConsumption(), Instant.now());
				kafkaTemplate.send("smart-consumer-events", event).whenComplete((result, exception) -> {
					if (exception != null) {
						System.err.println("Fehler beim Senden des Events: " + exception.getMessage());
					}
				});
				return;
			}
			String event = String.format("{\"deviceId\": %d, \"deviceType\": \"%s\", \"ownerId\": %d, \"commercial\": %b, \"active\": %b, \"powerConsumption\": %f, \"timestamp\": \"%s\"}",
					consumer.getId(), consumer.getClass().getSimpleName(), consumer.getUserId(), securityService.isCommercialUser(), active, consumer.getPowerConsumption(), Instant.now());
			kafkaTemplate.send("consumer-events", event).whenComplete((result, exception) -> {
				if (exception != null) {
					System.err.println("Fehler beim Senden des Events: " + exception.getMessage());
				}
			});
		} else if (device instanceof Producer producer) {
			String event = String.format("{\"deviceId\": %d, \"deviceType\": \"%s\", \"ownerId\": %d, \"commercial\": %b, \"active\": %b, \"powerProduction\": %f, \"timestamp\": \"%s\"}",
					producer.getId(), producer.getClass().getSimpleName(), producer.getUserId(), securityService.isCommercialUser(), active, producer.getPowerProduction(), Instant.now());
			kafkaTemplate.send("producer-events", event).whenComplete((result, exception) -> {
				if (exception != null) {
					System.err.println("Fehler beim Senden des Events: " + exception.getMessage());
				}
			});
		} else if (device instanceof Storage storage) {
			String event = String.format("{\"deviceId\": %d, \"deviceType\": \"%s\", \"ownerId\": %d, \"commercial\": %b, \"active\": %b, \"capacity\": %f, \"currentCharge\": %f, \"chargingPriority\": %d, \"consumingPriority\": %d, \"timestamp\": \"%s\"}",
					storage.getId(), storage.getClass().getSimpleName(), storage.getUserId(), securityService.isCommercialUser(), active, storage.getCapacity(), storage.getCurrentCharge(), storage.getChargingPriority(), storage.getConsumingPriority(), Instant.now());
			kafkaTemplate.send("storage-events", event).whenComplete((result, exception) -> {
				if (exception != null) {
					System.err.println("Fehler beim Senden des Events: " + exception.getMessage());
				}
			});
		}
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

	private Optional<Device> getSafeConsumerById(Long deviceId) {
		try {
			return Optional.ofNullable(consumerService.getConsumerById(deviceId));
		} catch (DeviceNotFoundException e) {
			return Optional.empty();
		}
	}

	private Optional<Device> getSafeProducerById(Long deviceId) {
		try {
			return Optional.ofNullable(producerService.getProducerById(deviceId));
		} catch (DeviceNotFoundException e) {
			return Optional.empty();
		}
	}

	private Optional<Device> getSafeStorageById(Long deviceId) {
		try {
			return Optional.ofNullable(storageService.getStorageById(deviceId));
		} catch (DeviceNotFoundException e) {
			return Optional.empty();
		}
	}

	@Override
	public Device getDeviceByIdFromUser(Long deviceId) {
		Long userId = securityService.getCurrentUserId();
		List<Optional<Device>> devices = List.of(
				getSafeConsumerById(deviceId),
				getSafeProducerById(deviceId),
				getSafeStorageById(deviceId)
		);
		Optional<Device> foundDevice = devices.stream()
				.filter(Optional::isPresent)
				.map(Optional::get)
				.filter(d -> d.getUserId().equals(userId))
				.findFirst();
		Device device = foundDevice.orElseThrow(() ->
				new DeviceNotFoundException("Device with ID " + deviceId + " not found or not accessible by user with ID " + userId));
		return device;
	}

	@Override
	public boolean isDeviceActiveFromUser(Long deviceId) {
		Long userId = securityService.getCurrentUserId();
		Device device = getDeviceByIdFromUser(deviceId);
		if (!device.getUserId().equals(userId)) {
			throw new DeviceNotFoundException("Device with ID " + deviceId + " not found or not accessible by user with ID " + userId);
		}
		boolean active = device.isActive();
		return active;
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
	public boolean isDeviceActive(Long deviceId) {
		Device device = getDeviceById(deviceId);
		boolean active = device.isActive();
		return active;
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
