package com.homebuilder.service;

import com.homebuilder.entity.Device;

import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
public interface DeviceService {

	List<Device> getAllDevicesFromUser();
	Device getDeviceByIdFromUser(Long deviceId);
	boolean isDeviceActiveFromUser(Long deviceId);
	Device updateDeviceForUser(Long deviceId, Device device);
	Map<String, String> toggleDeviceOnOffForUser(Long deviceId, boolean active);

	List<Device> getAllDevices();
	Device getDeviceById(Long deviceId);
	boolean isDeviceActive(Long deviceId);
	Device updateDevice(Long deviceId, Device device);
	Map<String, String> toggleDeviceOnOff(Long deviceId, boolean active);

}
