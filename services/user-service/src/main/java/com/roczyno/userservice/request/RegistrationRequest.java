package com.roczyno.userservice.request;

public record RegistrationRequest(
		String username,
		String email,
		String password
) {
}
