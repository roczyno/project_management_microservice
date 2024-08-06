package com.roczyno.commentservice.service;

import com.roczyno.commentservice.request.CommentRequest;
import com.roczyno.commentservice.response.CommentResponse;

import java.util.List;

public interface CommentService {
	CommentResponse createComment(CommentRequest comment, String jwt, Integer issueId);
	String deleteComment(Integer commentId) ;
	List<CommentResponse> findCommentsByIssueId(Integer issueId);
	CommentResponse findCommentById(Integer commentId);
	CommentResponse updateComment(String jwt,Integer commentId, CommentRequest request);
}
