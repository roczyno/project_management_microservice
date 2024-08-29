package com.roczyno.chatservice.service;


import com.roczyno.chatservice.external.user.UserResponse;
import com.roczyno.chatservice.model.Chat;
import com.roczyno.chatservice.response.ChatResponse;

import java.util.List;

public interface ChatService {
	ChatResponse getChatByProjectId(Integer projectId);
	ChatResponse createChat(Chat req,Integer projectId);
	ChatResponse addUserToChat(Integer projectId, Integer userId);
	String removeUserFromChat(Integer projectId, Integer userId);
	List<UserResponse> getChatProjectMembers(Integer chatId, Integer projectId, String jwt);
}
