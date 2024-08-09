package com.roczyno.chatservice.service.impl;

import com.roczyno.chatservice.model.Chat;
import com.roczyno.chatservice.repository.ChatRepository;
import com.roczyno.chatservice.response.ChatResponse;
import com.roczyno.chatservice.service.ChatService;
import com.roczyno.chatservice.util.ChatMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {
	private final ChatRepository chatRepository;
	private final ChatMapper mapper;

	@Override
	public ChatResponse createChat(Chat req, Integer projectId) {
		return mapper.toChatResponse(chatRepository.save(req));
	}
	@Override
	public ChatResponse getChatByProjectId(Integer projectId) {
		return mapper.toChatResponse(chatRepository.findByProjectId(projectId));
	}


}
