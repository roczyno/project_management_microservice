package com.roczyno.chatservice.service.impl;

import com.roczyno.chatservice.exception.ChatException;
import com.roczyno.chatservice.external.user.UserResponse;
import com.roczyno.chatservice.external.user.UserService;
import com.roczyno.chatservice.model.Chat;
import com.roczyno.chatservice.repository.ChatRepository;
import com.roczyno.chatservice.response.ChatResponse;
import com.roczyno.chatservice.service.ChatService;
import com.roczyno.chatservice.util.ChatMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {
	private final ChatRepository chatRepository;
	private final ChatMapper mapper;
	private final UserService userService;

	@Override
	public ChatResponse createChat(Chat req, Integer projectId) {
		return mapper.toChatResponse(chatRepository.save(req));
	}

	@Override
	@Transactional
	public ChatResponse addUserToChat(Integer projectId, Integer userId) {
		Chat chat=mapper.toChat(getChatByProjectId(projectId));

		if(chat.getTeamMemberIds().contains(userId)){
			throw new ChatException("User is already part of chat");
		}
		   chat.getTeamMemberIds().add(userId);
		chatRepository.save(chat);
		return mapper.toChatResponse(chat);
	}

	@Override
	@Transactional
	public String removeUserFromChat(Integer projectId, Integer userId) {
		Chat chat=mapper.toChat(getChatByProjectId(projectId));
		if(!chat.getTeamMemberIds().contains(userId)){
			throw new ChatException("User is not part of chat");
		}
		chat.getTeamMemberIds().remove(userId);
		chatRepository.save(chat);
		return "User removed from chat successfully";
	}

	@Override
	public List<UserResponse> getChatProjectMembers(Integer chatId, Integer projectId, String jwt) {
		List<Integer> teamIds = chatRepository.findTeamMemberIdsByProjectIdAndChatId(projectId,chatId);
		return userService.findAllUsersByIds(teamIds,jwt);
	}

	@Override
	public ChatResponse getChatByProjectId(Integer projectId) {
		return mapper.toChatResponse(chatRepository.findByProjectId(projectId));
	}


}
