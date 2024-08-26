package com.roczyno.invitationservice.request;

import com.roczyno.invitationservice.util.AppConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record InviteRequest(
		@Email(message = AppConstants.EMAIL)
		@NotNull(message = AppConstants.CANNOT_BE_NULL)
		String email,
		@NotNull(message = AppConstants.CANNOT_BE_NULL)
		Integer projectId
) {
}
