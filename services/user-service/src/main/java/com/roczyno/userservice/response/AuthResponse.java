package com.roczyno.userservice.response;

import lombok.Builder;

@Builder
public record AuthResponse(
		String jwt,
		String message
) {
}
