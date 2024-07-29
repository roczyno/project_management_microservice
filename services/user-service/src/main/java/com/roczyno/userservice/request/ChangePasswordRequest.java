package com.roczyno.userservice.request;

public record ChangePasswordRequest(
		String oldPassword,
		String newPassword,
		String confirmPassword
) {
}
