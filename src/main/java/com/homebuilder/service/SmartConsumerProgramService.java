package com.homebuilder.service;

import com.homebuilder.dto.SmartConsumerProgramRequest;
import com.homebuilder.entity.SmartConsumerProgram;

import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
public interface SmartConsumerProgramService {

	SmartConsumerProgram createSmartConsumerProgram(SmartConsumerProgramRequest request);
	List<SmartConsumerProgram> getAllSmartConsumerPrograms();
	List<SmartConsumerProgram> getAllSmartConsumerProgramsByOwner(Long ownerId);
	SmartConsumerProgram getSmartConsumerProgramById(Long smartConsumerProgramId);
	SmartConsumerProgram updateSmartConsumerProgram(SmartConsumerProgramRequest request);
	Map<String, String> archiveSmartConsumerProgram(Long smartConsumerProgramId);
	Map<String, String> deleteSmartConsumerProgram(Long smartConsumerProgramId);

}
