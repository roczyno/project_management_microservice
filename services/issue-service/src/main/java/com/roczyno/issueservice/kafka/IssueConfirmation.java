package com.roczyno.issueservice.kafka;

import java.time.LocalDate;

public record IssueConfirmation(
		 Long id,
		 String title,
		 String description,
		 String status,
		 String priority,
		 LocalDate dueDate,
		 String projectName
) {
}
