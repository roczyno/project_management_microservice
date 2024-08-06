package com.roczyno.notificationservice.kafka;

import java.time.LocalDate;

public record IssueConfirmation(
		Long id,
		String title,
		String description,
		String status,
		String priority,
		LocalDate dueDate,
		String projectName,
		String userEmail,
		String subject,
		String senderName
) {
}
