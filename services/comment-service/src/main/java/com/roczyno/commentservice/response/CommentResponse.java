package com.roczyno.commentservice.response;

import java.time.LocalDateTime;

public record CommentResponse(
		 Long id,
		 String comment,
		 LocalDateTime createdDate,
		 String  username,
		 Integer issueId
) {
}
