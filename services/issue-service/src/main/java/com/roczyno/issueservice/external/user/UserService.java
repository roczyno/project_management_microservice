package com.roczyno.issueservice.external.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "USER-SERVICE",url = "http://localhost:8090")
public interface UserService {
	@GetMapping("/api/v1/user/profile")
	UserResponse getUserProfile(@RequestHeader("Authorization") String jwt);
	@GetMapping("/api/v1/user/{userId}")
	UserResponse getUserById(@PathVariable Integer userId);
}
