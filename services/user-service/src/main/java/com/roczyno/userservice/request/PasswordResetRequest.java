package com.roczyno.userservice.request;

import com.roczyno.userservice.util.AppConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record PasswordResetRequest(
		@Email(message = AppConstants.EMAIL)
		@NotNull(message = AppConstants.CANNOT_BE_NULL)
		String email
) {
}
