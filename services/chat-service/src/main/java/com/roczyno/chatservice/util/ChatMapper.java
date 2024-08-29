package com.roczyno.chatservice.util;

import com.roczyno.chatservice.model.Chat;
import com.roczyno.chatservice.response.ChatResponse;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ChatMapper {
	public ChatResponse toChatResponse(Chat req) {
		return new ChatResponse(
				req.getId(),
				req.getProjectId(),
				req.getTeamMemberIds(),
				req.getCreatedAt()
		);
	}

	public Chat toChat(ChatResponse chatResponse) {
		return Chat.builder()
				.id(chatResponse.id())
				.projectId(chatResponse.projectId())
				.teamMemberIds(chatResponse.teamMemberIds())
				.createdAt(chatResponse.createdAt())
				.build();
	}
}
