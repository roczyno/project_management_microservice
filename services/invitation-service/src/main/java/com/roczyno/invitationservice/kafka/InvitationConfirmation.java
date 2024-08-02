package com.roczyno.invitationservice.kafka;

public record InvitationConfirmation(
		String to,
		String senderName,
		String subject,
		String projectName,
		String invitationUrl
) {
}
