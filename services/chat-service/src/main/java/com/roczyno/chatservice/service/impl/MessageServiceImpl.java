package com.roczyno.chatservice.service.impl;

import com.roczyno.chatservice.exception.MessageException;
import com.roczyno.chatservice.external.user.UserResponse;
import com.roczyno.chatservice.external.user.UserService;
import com.roczyno.chatservice.model.Chat;
import com.roczyno.chatservice.model.Message;
import com.roczyno.chatservice.repository.ChatRepository;
import com.roczyno.chatservice.repository.MessageRepository;
import com.roczyno.chatservice.request.MessageRequest;
import com.roczyno.chatservice.response.MessageResponse;
import com.roczyno.chatservice.service.MessageService;
import com.roczyno.chatservice.util.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
	private final MessageRepository messageRepository;
	private final ChatRepository chatRepository;
	private final UserService userService;
	private final MessageMapper mapper;

	@Override
	public MessageResponse sendMessage(MessageRequest req,Integer chatId, String jwt) {
		Chat chat=chatRepository.findById(chatId).orElseThrow();
		UserResponse user=userService.getUserProfile(jwt);
		Message message=Message.builder()
				.content(req.content())
				.userId(user.id())
				.chat(chat)
				.createdAt(LocalDateTime.now())
				.build();
		Message savedMessage=messageRepository.save(message);
		return mapper.toMessageResponse(savedMessage);
	}

	@Override
	public List<MessageResponse> getChatsMessages(Integer chatId) {
		List<Message> messages=messageRepository.findByChatId(chatId);
		return messages.stream()
				.map(mapper::toMessageResponse)
				.toList();
	}

	@Override
	public MessageResponse findMessageById(Integer id) {
		Message message=messageRepository.findById(id).orElseThrow(()-> new MessageException("Message not found"));
		return mapper.toMessageResponse(message);
	}

	@Override
	public String deleteMessageById(Integer id, String jwt) {
		UserResponse user=userService.getUserProfile(jwt);
		MessageResponse messageResponse=findMessageById(id);
		if(!user.id().equals(messageResponse.userId())){
			throw  new MessageException("You are not allowed to delete this message");
		}
		Message message=mapper.toMessage(messageResponse);
		messageRepository.delete(message);
		return "Message Deleted";
	}

	@Override
	public MessageResponse updateMessage(Integer id, String jwt,MessageRequest req) {
		UserResponse user=userService.getUserProfile(jwt);
		MessageResponse messageResponse=findMessageById(id);
		Message message=mapper.toMessage(messageResponse);
		if(!user.id().equals(messageResponse.userId())){
			throw  new MessageException("You are not allowed to update this message");
		}
		if(req.content()!=null){
			message.setContent(req.content());
		}
		Message updatedMessage=messageRepository.save(message);
		return mapper.toMessageResponse(updatedMessage);
	}
}
