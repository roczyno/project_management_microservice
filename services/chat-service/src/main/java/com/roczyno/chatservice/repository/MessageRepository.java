package com.roczyno.chatservice.repository;

import com.roczyno.chatservice.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Integer> {
	List<Message> findByChatId(Integer chatId);
}
