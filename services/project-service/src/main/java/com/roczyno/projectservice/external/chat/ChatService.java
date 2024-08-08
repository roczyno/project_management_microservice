package com.roczyno.projectservice.external.chat;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "CHAT-SERVICE",url = "http://localhost:8090")
public interface ChatService {
	@PostMapping
	Chat createChat(@RequestBody Chat req);
}
