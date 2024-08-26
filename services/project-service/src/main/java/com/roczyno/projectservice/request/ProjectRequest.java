package com.roczyno.projectservice.request;

import com.roczyno.projectservice.util.AppConstants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ProjectRequest(
		@NotNull(message = AppConstants.CANNOT_BE_NULL)
		@Size(min = 3, message = AppConstants.NAME_SIZE_MESSAGE)
		String name,
		@NotNull(message = AppConstants.CANNOT_BE_NULL)
		@Size(min = 10)
		String description,
		@NotNull(message = AppConstants.CANNOT_BE_NULL)
		String category,
		List<String> tags
) {
}
