package com.roczyno.commentservice.controller;

import com.roczyno.commentservice.request.CommentRequest;
import com.roczyno.commentservice.service.CommentService;
import com.roczyno.commentservice.util.ResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {
	private final CommentService commentService;
	@PostMapping("/issue/{issueId}")
	public ResponseEntity<Object> addComment(@PathVariable Integer issueId, @Valid @RequestBody CommentRequest req,
											 @RequestHeader("Authorization") String jwt){
		return ResponseHandler.successResponse(commentService.createComment(req,jwt,issueId), HttpStatus.OK);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteComment(@PathVariable Integer id)  {
		return ResponseHandler.successResponse(commentService.deleteComment(id),HttpStatus.OK);
	}
	@GetMapping("/issue/{issueId}")
	public ResponseEntity<Object> findCommentsByIssueId(@PathVariable Integer issueId) {
		return ResponseHandler.successResponse(commentService.findCommentsByIssueId(issueId),HttpStatus.OK);
	}
	@PutMapping("/{id}")
	public ResponseEntity<Object> updateComment(@PathVariable Integer id,@Valid @RequestBody CommentRequest req,
												@RequestHeader("Authorization") String jwt)  {
		return ResponseHandler.successResponse(commentService.updateComment(jwt,id,req),HttpStatus.OK);
	}

}
