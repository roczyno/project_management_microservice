package com.roczyno.projectservice.external.chat;

import java.time.LocalDateTime;
import java.util.Set;


public record ChatResponse(
		Integer id,
		Integer projectId,
		Set<Integer> teamMemberIds,
		LocalDateTime createdAt
) {

}
