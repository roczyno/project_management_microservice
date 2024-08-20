package com.roczyno.projectservice.external.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "USER-SERVICE",url = "http://localhost:8090")
public interface UserService {
	@GetMapping("/api/v1/user/profile")
	UserResponse getUserProfile(@RequestHeader("Authorization") String jwt);
	@PostMapping("/api/v1/user/find-by-ids")
	List<UserResponse> findAllUsersByIds(@RequestBody List<Integer> userIds,@RequestHeader("Authorization") String jwt);
	@PutMapping("/api/v1/user/increase/project-size/{userId}")
	String increaseUserProjectSize(@PathVariable Integer userId);
}
