package com.roczyno.chatservice.response;

import java.time.LocalDateTime;

public record MessageResponse(
		Integer id,
		String content,
		Integer userId,
		LocalDateTime createdAt
) {
}
