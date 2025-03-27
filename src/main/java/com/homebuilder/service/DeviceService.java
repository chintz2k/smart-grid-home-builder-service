package com.homebuilder.service;

import com.homebuilder.dto.GeneralDeviceDataDTO;
import com.homebuilder.entity.Device;

import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
public interface DeviceService {

	List<Device> getAllDevices();
	List<Device> getAllUnarchivedDevices();
	Device getDeviceById(Long deviceId);
	GeneralDeviceDataDTO getDeviceDataById(Long deviceId);
	Map<String, String> setActive(Long deviceId, boolean active, boolean sendEvent);
	List<Map<String, String>> deleteAllDevicesByOwner(Long ownerId);

}
