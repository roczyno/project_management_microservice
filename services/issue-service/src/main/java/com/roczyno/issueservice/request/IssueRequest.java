package com.roczyno.issueservice.request;

import com.roczyno.issueservice.util.AppConstants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record IssueRequest(
		@NotNull(message = AppConstants.CANNOT_BE_NULL)
		@Size(min = 5, message = AppConstants.NAME_SIZE_MESSAGE)
		String title,
		@NotNull(message = AppConstants.CANNOT_BE_NULL)
		String description,
		@NotNull(message = AppConstants.CANNOT_BE_NULL)
		String status,
		@NotNull(message = AppConstants.CANNOT_BE_NULL)
		String priority,
		@NotNull(message = AppConstants.CANNOT_BE_NULL)
		LocalDate dueDate,
		List<String> tags
) {
}
