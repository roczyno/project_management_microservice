package com.roczyno.notificationservice.kafka;

public record InviteConfirmation(
		String to,
		String senderName,
		String subject,
		String projectName,
		String InvitationUrl
) {
}
