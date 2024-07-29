package com.roczyno.userservice.request;

public record PasswordUpdateRequest(
		String password,
		String repeatPassword
) {
}
