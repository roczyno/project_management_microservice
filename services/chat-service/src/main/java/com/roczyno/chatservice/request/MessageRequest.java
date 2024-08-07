package com.roczyno.chatservice.request;

public record MessageRequest(
		Integer userId,
		String content
) {
}
