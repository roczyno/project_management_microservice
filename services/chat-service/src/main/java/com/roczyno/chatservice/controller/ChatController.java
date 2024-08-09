package com.roczyno.chatservice.controller;

import com.roczyno.chatservice.model.Chat;
import com.roczyno.chatservice.service.ChatService;
import com.roczyno.chatservice.util.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
	public ResponseEntity<Object> getChat(@PathVariable Integer projectId){
		return ResponseHandler.successResponse(chatService.getChatByProjectId(projectId), HttpStatus.OK);
	}
	@PostMapping("/project/{projectId}")
	public ResponseEntity<Object> createChat(@RequestBody Chat req,@PathVariable Integer projectId){
		return ResponseHandler.successResponse(chatService.createChat(req,projectId),HttpStatus.OK);
	}
}
