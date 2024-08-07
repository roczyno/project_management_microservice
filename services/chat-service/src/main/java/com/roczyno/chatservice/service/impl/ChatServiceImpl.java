package com.roczyno.chatservice.service.impl;

import com.roczyno.chatservice.external.user.UserService;
import com.roczyno.chatservice.model.Chat;
import com.roczyno.chatservice.repository.ChatRepository;
import com.roczyno.chatservice.request.ChatRequest;
import com.roczyno.chatservice.response.ChatResponse;
import com.roczyno.chatservice.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {
	private final ChatRepository chatRepository;

	@Override
	public Chat createChat(Chat req) {
		return chatRepository.save(req);
	}
	@Override
	public ChatResponse getChatByProjectId(Integer projectId) {
		return chatRepository.findByProjectId(projectId);
	}


}
