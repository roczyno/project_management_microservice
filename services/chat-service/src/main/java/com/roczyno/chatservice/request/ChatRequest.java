package com.roczyno.chatservice.request;

import com.roczyno.chatservice.util.AppConstants;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;

public record ChatRequest(
		@NotNull(message = AppConstants.CANNOT_BE_NULL)
		 Integer projectId,
		 Set<Integer>teamMemberIds,
         LocalDateTime createdAt
) {
}
