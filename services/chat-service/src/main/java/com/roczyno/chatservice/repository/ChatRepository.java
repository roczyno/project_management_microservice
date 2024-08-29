package com.roczyno.chatservice.repository;

import com.roczyno.chatservice.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat,Integer> {

	Chat findByProjectId(Integer projectId);
	@Query("select c.teamMemberIds from Chat c where c.id=:chatId and c.projectId=:projectId")
	List<Integer> findTeamMemberIdsByProjectIdAndChatId(@Param("projectId") Integer projectId,
														@Param("chatId") Integer chatId);
}
