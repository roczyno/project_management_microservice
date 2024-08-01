package com.roczyno.userservice.response;

import java.time.LocalDateTime;

public record UserResponse(
		Integer id,
		String username,
		String email,
		int projectSize,
		LocalDateTime createdAt
) {
}
