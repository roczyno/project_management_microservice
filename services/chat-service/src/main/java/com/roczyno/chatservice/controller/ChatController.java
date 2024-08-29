package com.roczyno.chatservice.controller;

import com.roczyno.chatservice.external.user.UserResponse;
import com.roczyno.chatservice.model.Chat;
import com.roczyno.chatservice.response.ChatResponse;
import com.roczyno.chatservice.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {
	private final ChatService chatService;

	@GetMapping("/project/{projectId}")
	public ResponseEntity<ChatResponse> getChat(@PathVariable Integer projectId){
		return ResponseEntity.ok(chatService.getChatByProjectId(projectId));
	}
	@PostMapping("/project/{projectId}")
	public ResponseEntity<ChatResponse> createChat(@Valid @RequestBody Chat req, @PathVariable Integer projectId){
		return ResponseEntity.ok(chatService.createChat(req,projectId));
	}
	@PostMapping("/project/{projectId}/user/{userId}")
	public ResponseEntity<ChatResponse> addUserToChat(@PathVariable Integer projectId,@PathVariable Integer userId){
		return ResponseEntity.ok(chatService.addUserToChat(projectId,userId));
	}
	@PutMapping("/remove/project/{projectId}/user/{userId}")
	public ResponseEntity<String> removeUserFromChat(@PathVariable Integer projectId, @PathVariable Integer userId){
		return ResponseEntity.ok(chatService.removeUserFromChat(projectId,userId));
	}
	@GetMapping("/{chatId}/project/{projectId}/members")
	public ResponseEntity<List<UserResponse>> getChatMembers(@PathVariable Integer chatId,
															 @PathVariable Integer projectId,
															 @RequestHeader("Authorization") String jwt){
		return ResponseEntity.ok(chatService.getChatProjectMembers(chatId,projectId,jwt));
	}
}
