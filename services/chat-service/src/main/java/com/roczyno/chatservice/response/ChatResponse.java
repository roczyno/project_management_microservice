package com.roczyno.chatservice.response;



import com.roczyno.chatservice.external.user.UserResponse;

import java.time.LocalDateTime;
import java.util.Set;

public record ChatResponse(
		Integer id,
		LocalDateTime createdAt,
		Set<UserResponse> users
) {
}
