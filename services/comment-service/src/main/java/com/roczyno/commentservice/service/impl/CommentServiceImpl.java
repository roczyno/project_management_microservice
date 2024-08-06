package com.roczyno.commentservice.service.impl;

import com.roczyno.commentservice.external.user.UserResponse;
import com.roczyno.commentservice.external.user.UserService;
import com.roczyno.commentservice.model.Comment;
import com.roczyno.commentservice.repository.CommentRepository;
import com.roczyno.commentservice.request.CommentRequest;
import com.roczyno.commentservice.response.CommentResponse;
import com.roczyno.commentservice.service.CommentService;
import com.roczyno.commentservice.util.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final CommentRepository commentRepository;
	private final UserService userService;
	private final CommentMapper mapper;
	@Override
	public CommentResponse createComment(CommentRequest req, String jwt, Integer issueId) {
		UserResponse user=userService.getUserProfile(jwt);
		Comment comment=Comment.builder()
				.comment(req.comment())
				.createdDate(LocalDateTime.now())
				.userId(user.id())
				.username(user.username())
				.issueId(issueId)
				.build();
		Comment savedComment= commentRepository.save(comment);
	return  mapper.mapToCommentResponse(savedComment);
	}

	@Override
	public String deleteComment(Integer commentId) {
		CommentResponse commentResponse=findCommentById(commentId);
		commentRepository.delete(mapper.mapToComment(commentResponse));
		return "Comment deleted successfully";
	}

	@Override
	public List<CommentResponse> findCommentsByIssueId(Integer issueId) {
		List<Comment> comments=commentRepository.findByIssueId(issueId);
		return comments.stream()
				.map(mapper::mapToCommentResponse)
				.toList();
	}

	@Override
	public CommentResponse findCommentById(Integer commentId) {
		Comment comment= commentRepository.findById(commentId).orElseThrow(()-> new RuntimeException("comment not found"));
		return mapper.mapToCommentResponse(comment);
	}

	@Override
	public CommentResponse updateComment(String jwt, Integer commentId, CommentRequest request) {
		UserResponse user=userService.getUserProfile(jwt);
		Comment comment=commentRepository.findById(commentId).orElseThrow();
		if(!comment.getUserId().equals(user.id())){
			throw new RuntimeException();
		}
		if(request.comment()!=null){
			comment.setComment(request.comment());
		}
		Comment savedComment=commentRepository.save(comment);
		return mapper.mapToCommentResponse(savedComment);
	}
}
