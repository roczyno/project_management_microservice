package com.roczyno.chatservice.util;

import com.roczyno.chatservice.model.Message;
import com.roczyno.chatservice.response.MessageResponse;
import org.springframework.stereotype.Service;

@Service
public class MessageMapper {
	public MessageResponse toMessageResponse(Message req) {
		return new MessageResponse(
				req.getId(),
				req.getContent(),
				req.getUserId(),
				req.getCreatedAt()
		);
	}

	public Message toMessage(MessageResponse messageResponse) {
		return Message.builder()
				.id(messageResponse.id())
				.content(messageResponse.content())
				.userId(messageResponse.userId())
				.createdAt(messageResponse.createdAt())
				.build();

	}
}
