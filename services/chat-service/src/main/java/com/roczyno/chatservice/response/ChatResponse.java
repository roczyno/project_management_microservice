package com.roczyno.chatservice.response;


import java.time.LocalDateTime;
import java.util.Set;

public record ChatResponse(
		Integer id,
		Integer projectId,
		Set<Integer> teamMemberIds,
		LocalDateTime createdAt

) {
}
