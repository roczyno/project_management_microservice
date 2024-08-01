package com.roczyno.userservice.util;

import com.roczyno.userservice.model.User;
import com.roczyno.userservice.response.UserResponse;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
	public UserResponse mapToUserResponse(User user) {
		return new UserResponse(
				user.getId(),
				user.getUsername(),
				user.getEmail(),
				user.getProjectSize(),
				user.getCreatedAt()
		);
	}
}
