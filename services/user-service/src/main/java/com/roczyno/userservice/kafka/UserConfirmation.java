package com.roczyno.userservice.kafka;

public record UserConfirmation(
		String to,
		String username,
		String confirmationUrl,
		String activationCode,
		String subject
) {
}
