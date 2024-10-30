package com.homebuilder.service;

import com.homebuilder.entity.Device;

import java.util.List;

/**
 * @author André Heinen
 */
public interface DeviceService {

	List<Device> getAllDevicesByUser(Long userId);

	List<Device> getAllDevices();

	Device getDeviceByIdAndUser(Long deviceId, Long userId);

}
