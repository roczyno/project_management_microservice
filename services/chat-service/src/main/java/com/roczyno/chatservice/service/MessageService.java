package com.roczyno.chatservice.service;

import com.roczyno.chatservice.request.MessageRequest;
import com.roczyno.chatservice.response.MessageResponse;

import java.util.List;

public interface MessageService {
	MessageResponse sendMessage(MessageRequest req,Integer chatId,String jwt) ;
	List<MessageResponse> getChatsMessages(Integer chatId);
	MessageResponse findMessageById(Integer id);
	String deleteMessageById(Integer id,String jwt);
	MessageResponse updateMessage(Integer id,String jwt,MessageRequest req);
}
