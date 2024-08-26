package com.roczyno.springbootecommerceapi.request;

public record UpdateUserRequest(
		String firstName,
		String lastName,
		String profilePic,
		String phone
) {
}
