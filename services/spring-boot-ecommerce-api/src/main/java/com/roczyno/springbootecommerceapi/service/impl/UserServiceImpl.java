package com.roczyno.springbootecommerceapi.service.impl;


import com.roczyno.springbootecommerceapi.repository.UserRepository;
import com.roczyno.springbootecommerceapi.request.UpdateUserRequest;
import com.roczyno.springbootecommerceapi.response.UserProfileResponse;
import com.roczyno.springbootecommerceapi.service.UserService;
import com.roczyno.springbootecommerceapi.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final UserMapper mapper;
	@Override
	public List<UserProfileResponse> getAllUsers() {
		return userRepository.findAll()
				.stream()
				.map(mapper::toUserResponse)
				.collect(Collectors.toList());
	}

	@Override
	public UserProfileResponse getUserById(Integer id) {
		var user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User with id " + id + " not found"));
		return mapper.toUserResponse(user);
	}

	@Override
	public UserProfileResponse updateUserProfile(Integer userId, UpdateUserRequest req) {
		var user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User with id " + userId +
				" not found"));
		if(req.firstName()!=null){
			user.setFirstName(req.firstName());
		}
		if(req.lastName()!=null){
			user.setLastName(req.lastName());
		}
		if(req.phone()!=null){
			user.setPhone(req.phone());
		}
		if(req.profilePic()!=null){
			user.setProfilePic(req.profilePic());
		}
		var savedUser = userRepository.save(user);
		return mapper.toUserResponse(savedUser);
	}
}
