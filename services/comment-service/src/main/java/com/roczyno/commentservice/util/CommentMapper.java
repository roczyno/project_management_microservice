package com.roczyno.commentservice.util;

import com.roczyno.commentservice.model.Comment;
import com.roczyno.commentservice.response.CommentResponse;
import org.springframework.stereotype.Service;

@Service
public class CommentMapper {
	public CommentResponse mapToCommentResponse(Comment req) {
		return new CommentResponse(
				req.getId(),
				req.getComment(),
				req.getCreatedDate(),
				req.getUsername(),
				req.getIssueId()
		);
	}

	public Comment mapToComment(CommentResponse res) {
		return Comment.builder()
				.id(res.id())
				.comment(res.comment())
				.createdDate(res.createdDate())
				.username(res.username())
				.issueId(res.issueId())
				.build();
	}
}
