package com.roczyno.projectservice.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ProjectResponse(
		Integer id,
		String name,
		String description,
		String category,
		List<String> tags,
		Integer chatId,
		Integer userId
) {
}
