package com.roczyno.springbootecommerceapi.util;


import com.roczyno.springbootecommerceapi.entity.User;
import com.roczyno.springbootecommerceapi.response.UserProfileResponse;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
	public UserProfileResponse toUserResponse(User user) {
		return new UserProfileResponse(
				user.getId(),
				user.getFirstName(),
				user.getLastName(),
				user.getEmail(),
				user.getProfilePic(),
				user.getPhone()
		);
	}
}
