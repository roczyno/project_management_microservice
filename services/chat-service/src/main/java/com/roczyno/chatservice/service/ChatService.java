package com.roczyno.chatservice.service;


import com.roczyno.chatservice.model.Chat;
import com.roczyno.chatservice.response.ChatResponse;

public interface ChatService {
	ChatResponse getChatByProjectId(Integer projectId);
	Chat createChat(Chat req);
}
