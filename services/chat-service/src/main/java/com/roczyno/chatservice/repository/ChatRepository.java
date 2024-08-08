package com.roczyno.chatservice.repository;

import com.roczyno.chatservice.model.Chat;
import com.roczyno.chatservice.response.ChatResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat,Integer> {

	Chat findByProjectId(Integer projectId);
}
