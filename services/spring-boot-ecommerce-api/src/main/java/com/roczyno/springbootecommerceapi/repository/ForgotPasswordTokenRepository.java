package com.roczyno.springbootecommerceapi.repository;



import com.roczyno.springbootecommerceapi.entity.ForgotPasswordToken;
import com.roczyno.springbootecommerceapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordTokenRepository extends JpaRepository<ForgotPasswordToken, Integer> {

    ForgotPasswordToken findByTokenAndUser(int token, User user);
}
