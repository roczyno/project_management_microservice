package com.roczyno.chatservice.controller;

import com.roczyno.chatservice.model.Chat;
import com.roczyno.chatservice.response.ChatResponse;
import com.roczyno.chatservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {
	private final ChatService chatService;

	@GetMapping("/project/{projectId}")
	public ResponseEntity<ChatResponse> getChat(@PathVariable Integer projectId){
		return ResponseEntity.ok(chatService.getChatByProjectId(projectId));
	}
	@PostMapping
	public ResponseEntity<Chat> createChat(@RequestBody Chat req){
		return ResponseEntity.ok(chatService.createChat(req));
	}
}
