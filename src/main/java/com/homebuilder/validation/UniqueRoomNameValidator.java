package com.homebuilder.validation;

import com.homebuilder.repository.RoomRepository;
import com.homebuilder.security.SecurityService;
import com.homebuilder.dto.RoomRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueRoomNameValidator implements ConstraintValidator<UniqueRoomName, RoomRequest> {

	private final RoomRepository roomRepository;
	private final SecurityService securityService;

	@Autowired
	public UniqueRoomNameValidator(RoomRepository roomRepository, SecurityService securityService) {
		this.roomRepository = roomRepository;
		this.securityService = securityService;
	}

	@Override
	public boolean isValid(RoomRequest roomRequest, ConstraintValidatorContext context) {
		if (roomRequest.getName() == null || roomRequest.getName().trim().isEmpty()) {
			return true;
		}
		Long currentUserId = securityService.getCurrentUserId();
		return roomRepository.findByUserIdAndName(currentUserId, roomRequest.getName())
				.map(existingRoom -> roomRequest.getId() != null && existingRoom.getId().equals(roomRequest.getId()))
				.orElse(true);
	}
}
