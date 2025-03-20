package com.homebuilder.security;

import com.homebuilder.entity.Device;
import com.homebuilder.entity.Room;
import com.homebuilder.entity.SmartConsumerProgram;
import com.homebuilder.entity.SmartConsumerTimeslot;
import com.homebuilder.exception.UnauthorizedAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author André Heinen
 */
@Service
public class SecurityService {

	private final JwtUtil jwtUtil;

	@Autowired
	public SecurityService(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	public Long extractUserIdFromPrincipal(Object principal) {
		if (principal == null) {
			throw new UnauthorizedAccessException("No principal found, authentication is required");
		}

		if (principal instanceof Authentication) {
			if (principal instanceof UsernamePasswordAuthenticationToken) {
				String token = ((UsernamePasswordAuthenticationToken) principal).getCredentials().toString();
				return jwtUtil.extractUserId(token);
			}
		}

		throw new UnauthorizedAccessException("Invalid principal, cannot extract user ID");
	}

	public Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication.getPrincipal() == null) {
			throw new UnauthorizedAccessException("User is not authenticated");
		}

		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			return jwtUtil.extractUserId(authentication.getCredentials().toString());
		}

		throw new UnauthorizedAccessException("Invalid user ID in authentication context");
	}

	public boolean isCurrentUserACommercialUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication.getPrincipal() == null) {
			throw new UnauthorizedAccessException("User is not authenticated");
		}

		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			return jwtUtil.isCommercial(authentication.getCredentials().toString());
		}

		return false;
	}

	public String getCurrentUserRole() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication.getPrincipal() == null) {
			throw new UnauthorizedAccessException("User is not authenticated");
		}

		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			String token = authentication.getCredentials().toString();
			return jwtUtil.extractRole(token);
		}

		throw new UnauthorizedAccessException("Invalid authentication context, cannot extract role");
	}

	public boolean isCurrentUserAdminOrSystem() {
		return getCurrentUserRole().equals("ROLE_ADMIN") || getCurrentUserRole().equals("ROLE_SYSTEM");
	}

	/**
	 * Determines whether the current user can access the specified device.
	 *
	 * @param device the device to check access for; the device must have a user ID associated with it
	 * @param <T> the type of the device, which must extend the Device class
	 * @return true if the current user's ID matches the device's user ID or if the current user has an admin or system role;
	 *         false otherwise
	 */
	public <T extends Device> boolean canAccessDevice(T device) {
		Long userId = getCurrentUserId();
		if (!device.getUserId().equals(userId)) {
			return isCurrentUserAdminOrSystem();
		}
		return true;
	}

	public boolean canAccessRoom(Room room) {
		Long userId = getCurrentUserId();
		if (!room.getUserId().equals(userId)) {
			return isCurrentUserAdminOrSystem();
		}
		return true;
	}

	public boolean canAccessProgram(SmartConsumerProgram program) {
		Long userId = getCurrentUserId();
		if (!program.getUserId().equals(userId)) {
			return isCurrentUserAdminOrSystem();
		}
		return true;
	}

	public boolean canAccessTimeslot(SmartConsumerTimeslot timeslot) {
		Long userId = getCurrentUserId();
		if (!timeslot.getUserId().equals(userId)) {
			return isCurrentUserAdminOrSystem();
		}
		return true;
	}
}
