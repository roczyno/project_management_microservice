package com.roczyno.chatservice.request;

import java.time.LocalDateTime;
import java.util.Set;

public record ChatRequest(
		 Integer projectId,
		 Set<Integer>teamMemberIds,
         LocalDateTime createdAt
) {
}
