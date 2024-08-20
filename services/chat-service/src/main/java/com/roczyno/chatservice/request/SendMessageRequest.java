package com.roczyno.chatservice.request;

public record SendMessageRequest(
		Integer userId,
		String content,
		Integer chatId
) {
}
