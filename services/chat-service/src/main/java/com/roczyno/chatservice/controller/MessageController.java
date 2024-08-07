package com.roczyno.chatservice.controller;

import com.roczyno.chatservice.request.MessageRequest;
import com.roczyno.chatservice.response.MessageResponse;
import com.roczyno.chatservice.service.MessageService;
import lombok.RequiredArgsConstructor;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/message")
public class MessageController {
	private final MessageService messageService;

	@PostMapping("/chat/{chatId}")
	public ResponseEntity<MessageResponse> sendMessage(@PathVariable Integer chatId, @RequestBody MessageRequest req,
													   @RequestHeader("Authorization") String jwt){
		return ResponseEntity.ok(messageService.sendMessage(req,chatId,jwt));
	}

	@GetMapping("/chat/{chatId}")
	public ResponseEntity<List<MessageResponse>> getChatMessages(@PathVariable Integer chatId){
		return ResponseEntity.ok(messageService.getChatsMessages(chatId));
	}

	@DeleteMapping("/chat/{chatId}")
	public ResponseEntity<String> deleteMessage(@PathVariable Integer chatId,@RequestHeader("Authorization") String jwt){
		return ResponseEntity.ok(messageService.deleteMessageById(chatId,jwt));
	}

	@PutMapping("/chat/{chatId}")
	public ResponseEntity<MessageResponse> updateMessage(@PathVariable Integer chatId,@RequestBody MessageRequest req,
															   @RequestHeader("Authorization") String jwt){
		return ResponseEntity.ok(messageService.updateMessage(chatId,jwt,req));
	}

}
