package com.homebuilder.service;

import com.homebuilder.entity.Device;

import java.util.List;

/**
 * @author André Heinen
 */
public interface DeviceService {

	List<Device> getAllDevicesFromUser(Long userId);

	List<Device> getAllDevices();

}
