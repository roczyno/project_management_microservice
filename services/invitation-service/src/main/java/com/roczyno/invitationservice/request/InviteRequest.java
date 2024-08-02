package com.roczyno.invitationservice.request;

public record InviteRequest(
		String email,
		Integer projectId
) {
}
