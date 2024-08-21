package com.roczyno.projectservice.external.chat;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "CHAT-SERVICE",url = "http://localhost:8086")
@Service
public interface ChatService {
	@PostMapping("/api/v1/chat/project/{projectId}")
	ChatResponse createChat(@RequestBody Chat req, @PathVariable Integer projectId);
	@PostMapping("/api/v1/chat/project/{projectId}/user/{userId}")
	ChatResponse addUserToChat(@PathVariable Integer projectId, @PathVariable Integer userId);
	@PutMapping("/api/v1/chat/remove/project/{projectId}/user/{userId}")
	 String removeUserFromChat(@PathVariable Integer projectId, @PathVariable Integer userId);
}
