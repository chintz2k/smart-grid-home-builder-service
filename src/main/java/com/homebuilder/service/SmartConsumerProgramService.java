package com.homebuilder.service;

import com.homebuilder.dto.SmartConsumerProgramRequest;
import com.homebuilder.entity.SmartConsumerProgram;

import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
public interface SmartConsumerProgramService {

	SmartConsumerProgram createSmartConsumerProgramForUser(SmartConsumerProgramRequest request);
	List<SmartConsumerProgram> getAllSmartConsumerProgramsFromUser();
	SmartConsumerProgram getSmartConsumerProgramByIdFromUser(Long smartConsumerProgramId);
	SmartConsumerProgram updateSmartConsumerProgramForUser(Long existingProgramId, SmartConsumerProgramRequest request);
	Map<String, String> deleteSmartConsumerProgramForUser(Long smartConsumerProgramId);

	List<SmartConsumerProgram> getAllSmartConsumerPrograms();
	SmartConsumerProgram getSmartConsumerProgramById(Long smartConsumerProgramId);
}
