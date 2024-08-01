package com.roczyno.projectservice.response;

import java.util.List;

public record ProjectResponse(
		String name,
		String description,
		String category,
		List<String> tags
) {
}
