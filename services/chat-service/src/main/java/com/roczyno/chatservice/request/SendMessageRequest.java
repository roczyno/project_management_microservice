package com.roczyno.chatservice.request;

import com.roczyno.chatservice.util.AppConstants;
import jakarta.validation.constraints.NotNull;

public record SendMessageRequest(
		@NotNull(message = AppConstants.CANNOT_BE_NULL)
		Integer userId,
		@NotNull(message = AppConstants.CANNOT_BE_NULL)
		String content,
		@NotNull(message = AppConstants.CANNOT_BE_NULL)
		Integer chatId
) {
}
