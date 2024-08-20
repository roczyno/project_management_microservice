package com.roczyno.userservice.service.impl;

import com.roczyno.userservice.exception.UserException;
import com.roczyno.userservice.model.User;
import com.roczyno.userservice.repository.UserRepository;
import com.roczyno.userservice.response.UserResponse;
import com.roczyno.userservice.service.UserService;
import com.roczyno.userservice.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final UserMapper mapper;
	@Override
	public UserResponse findUserProfileByJwt(Authentication connectedUser) {
		User user= (User) connectedUser.getPrincipal();
		return mapper.mapToUserResponse(userRepository.findByEmail(user.getEmail()));
	}

	@Override
	public UserResponse findUserById(Integer userId) {
		User user= userRepository.findById(userId).orElseThrow(()->new UserException("User not found"));
		return mapper.mapToUserResponse(user);
	}

	@Override
	public List<UserResponse> findAllUsersByIds(List<Integer> userIds) {
		List<User> users= userRepository.findByIdIsIn(userIds);
		return users.stream()
				.map(mapper::mapToUserResponse)
				.toList();
	}

	@Override
	public String increaseUserProjectSize(Integer userId) {
		User user=mapper.mapToUser(findUserById(userId));
		user.setProjectSize(user.getProjectSize()+1);
		userRepository.save(user);
		return "successful";
	}

	@Override
	public String decreaseUserProjectSize(Integer userId) {
		User user=mapper.mapToUser(findUserById(userId));
		user.setProjectSize(0);
		userRepository.save(user);
		return "successful";
	}
}
