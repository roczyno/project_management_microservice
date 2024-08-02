package com.roczyno.issueservice.request;

import java.time.LocalDate;
import java.util.List;

public record IssueRequest(
		 String title,
		 String description,
		 String status,
		 String priority,
		 LocalDate dueDate,
		 List<String>tags
) {
}
