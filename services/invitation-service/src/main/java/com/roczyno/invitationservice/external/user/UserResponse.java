package com.roczyno.invitationservice.external.user;

import java.time.LocalDateTime;

public record UserResponse(
		Integer id,
		String username,
		String email,
		int projectSize,
		LocalDateTime createdAt
) {
}
