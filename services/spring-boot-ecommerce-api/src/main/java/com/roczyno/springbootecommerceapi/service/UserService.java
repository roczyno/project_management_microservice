package com.roczyno.springbootecommerceapi.service;



import com.roczyno.springbootecommerceapi.request.UpdateUserRequest;
import com.roczyno.springbootecommerceapi.response.UserProfileResponse;

import java.util.List;

public interface UserService {
	List<UserProfileResponse> getAllUsers();
	UserProfileResponse getUserById(Integer id);
	UserProfileResponse updateUserProfile(Integer userId, UpdateUserRequest req);
}
