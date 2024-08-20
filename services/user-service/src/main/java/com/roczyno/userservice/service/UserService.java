package com.roczyno.userservice.service;

import com.roczyno.userservice.response.UserResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {
	UserResponse findUserProfileByJwt(Authentication connectedUser);
	UserResponse findUserById(Integer userId);
	List<UserResponse> findAllUsersByIds(List<Integer> userIds);
	String increaseUserProjectSize(Integer userId);
	String decreaseUserProjectSize(Integer userId);
}
