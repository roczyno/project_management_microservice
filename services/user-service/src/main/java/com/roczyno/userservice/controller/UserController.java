package com.roczyno.userservice.controller;

import com.roczyno.userservice.response.UserResponse;
import com.roczyno.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
	private final UserService userService;

	@GetMapping("/profile")
	public ResponseEntity<UserResponse> profile(Authentication connectedUser) {
		return ResponseEntity.ok(userService.findUserProfileByJwt(connectedUser));
	}
	@GetMapping("/{userId}")
	public ResponseEntity<UserResponse> getUserById(@PathVariable Integer userId) {
		return ResponseEntity.ok(userService.findUserById(userId));
	}

}

