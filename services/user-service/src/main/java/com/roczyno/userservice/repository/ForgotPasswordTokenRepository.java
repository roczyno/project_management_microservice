package com.roczyno.userservice.repository;

import com.roczyno.userservice.model.ForgotPasswordToken;
import com.roczyno.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordTokenRepository  extends JpaRepository<ForgotPasswordToken,Integer> {
	ForgotPasswordToken findByTokenAndUser(int token, User user);
}
