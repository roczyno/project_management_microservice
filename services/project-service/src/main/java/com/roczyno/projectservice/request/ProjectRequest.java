package com.roczyno.projectservice.request;

import java.util.List;

public record ProjectRequest(
		 String name,
		 String description,
		 String category,
		 List<String>tags
) {
}
