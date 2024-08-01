package com.roczyno.userservice.service;

import com.roczyno.userservice.response.UserResponse;
import org.springframework.security.core.Authentication;

public interface UserService {
	UserResponse findUserProfileByJwt(Authentication connectedUser) ;
}
