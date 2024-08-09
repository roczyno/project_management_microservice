package com.roczyno.projectservice.external.chat;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "CHAT-SERVICE",url = "http://localhost:8086")
public interface ChatService {
	@PostMapping("/api/v1/chat/project/{projectId}")
	ChatResponse createChat(@RequestBody Chat req, @PathVariable Integer projectId);
}
