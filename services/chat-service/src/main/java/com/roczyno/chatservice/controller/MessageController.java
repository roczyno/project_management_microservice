package com.roczyno.chatservice.controller;

import com.roczyno.chatservice.request.MessageRequest;
import com.roczyno.chatservice.service.MessageService;
import com.roczyno.chatservice.util.ResponseHandler;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/message")
public class MessageController {
	private final MessageService messageService;

	@PostMapping("/chat/{chatId}")
	public ResponseEntity<Object> sendMessage(@PathVariable Integer chatId, @Valid @RequestBody MessageRequest req,
													   @RequestHeader("Authorization") String jwt){
		return ResponseHandler.successResponse(messageService.sendMessage(req,chatId,jwt), HttpStatus.OK);
	}

	@GetMapping("/chat/{chatId}")
	public ResponseEntity<Object> getChatMessages(@PathVariable Integer chatId){
		return ResponseHandler.successResponse(messageService.getChatsMessages(chatId),HttpStatus.OK);
	}

	@DeleteMapping("/chat/{chatId}")
	public ResponseEntity<Object> deleteMessage(@PathVariable Integer chatId,@RequestHeader("Authorization") String jwt){
		return ResponseHandler.successResponse(messageService.deleteMessageById(chatId,jwt),HttpStatus.OK);
	}

	@PutMapping("/chat/{chatId}")
	public ResponseEntity<Object> updateMessage(@PathVariable Integer chatId,@Valid @RequestBody MessageRequest req,
															   @RequestHeader("Authorization") String jwt){
		return ResponseHandler.successResponse(messageService.updateMessage(chatId,jwt,req),HttpStatus.OK);
	}

}
