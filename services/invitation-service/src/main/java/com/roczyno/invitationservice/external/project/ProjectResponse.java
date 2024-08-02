package com.roczyno.invitationservice.external.project;

import java.util.List;

public record ProjectResponse(
		String name,
		String description,
		String category,
		List<String> tags
) {
}
