package com.roczyno.chatservice.service;


import com.roczyno.chatservice.model.Chat;
import com.roczyno.chatservice.response.ChatResponse;

public interface ChatService {
	ChatResponse getChatByProjectId(Integer projectId);
	ChatResponse createChat(Chat req,Integer projectId);

	ChatResponse addUserToChat(Integer projectId, Integer userId);
}
