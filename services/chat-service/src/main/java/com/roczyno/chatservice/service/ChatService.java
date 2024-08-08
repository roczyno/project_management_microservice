package com.roczyno.chatservice.service;


import com.roczyno.chatservice.model.Chat;

public interface ChatService {
	Chat getChatByProjectId(Integer projectId);
	Chat createChat(Chat req);
}
