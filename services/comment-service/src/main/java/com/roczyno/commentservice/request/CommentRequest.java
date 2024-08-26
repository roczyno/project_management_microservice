package com.roczyno.commentservice.request;

import com.roczyno.commentservice.util.AppConstants;
import jakarta.validation.constraints.NotNull;

public record CommentRequest(
		@NotNull(message = AppConstants.CANNOT_BE_NULL)
		 String comment
) {
}
