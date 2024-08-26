package com.roczyno.userservice.request;

import com.roczyno.userservice.util.AppConstants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ChangePasswordRequest(
		@Pattern(regexp = AppConstants.PASSWORD_PATTERN,message = AppConstants.PASSWORD_PATTERN_MESSAGE)
		@NotNull(message = AppConstants.CANNOT_BE_NULL)
		String oldPassword,
		@Pattern(regexp = AppConstants.PASSWORD_PATTERN,message = AppConstants.PASSWORD_PATTERN_MESSAGE)
		@NotNull(message = AppConstants.CANNOT_BE_NULL)
		String newPassword,
		@Pattern(regexp = AppConstants.PASSWORD_PATTERN,message = AppConstants.PASSWORD_PATTERN_MESSAGE)
		@NotNull(message = AppConstants.CANNOT_BE_NULL)
		String confirmPassword
) {
}
