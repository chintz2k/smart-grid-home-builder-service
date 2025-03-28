package com.homebuilder.service;

import com.homebuilder.dto.SmartConsumerProgramRequest;
import com.homebuilder.dto.SmartConsumerProgramResponse;
import com.homebuilder.entity.SmartConsumerProgram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author André Heinen
 */
public interface SmartConsumerProgramService {

	SmartConsumerProgram createSmartConsumerProgram(SmartConsumerProgramRequest request);
	List<SmartConsumerProgram> getAllSmartConsumerPrograms();
	List<SmartConsumerProgram> getAllUnarchivedSmartConsumerPrograms();
	List<SmartConsumerProgram> getAllSmartConsumerProgramsByOwner(Long ownerId);
	Page<SmartConsumerProgramResponse> getAllByUserId(Long userId, Pageable pageable);
	Page<SmartConsumerProgramResponse> getAllByUserIdAndByConsumerId(Long userId, Long consumerId, Pageable pageable);
	SmartConsumerProgram getSmartConsumerProgramById(Long smartConsumerProgramId);
	SmartConsumerProgram updateSmartConsumerProgram(SmartConsumerProgramRequest request);
	Map<String, String> archiveSmartConsumerProgram(Long smartConsumerProgramId);
	Map<String, String> deleteSmartConsumerProgram(Long smartConsumerProgramId);

}
