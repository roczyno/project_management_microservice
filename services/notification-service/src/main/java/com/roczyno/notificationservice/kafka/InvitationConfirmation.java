package com.roczyno.notificationservice.kafka;

public record InvitationConfirmation(
		String to,
		String senderName,
		String subject,
		String projectName,
		String InvitationUrl
) {
}
