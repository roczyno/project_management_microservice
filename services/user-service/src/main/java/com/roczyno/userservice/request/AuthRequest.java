package com.roczyno.userservice.request;

public record AuthRequest(
		String email,
		String password
) {
}
