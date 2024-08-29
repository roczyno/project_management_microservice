package com.roczyno.projectservice.response;

import java.util.List;

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
