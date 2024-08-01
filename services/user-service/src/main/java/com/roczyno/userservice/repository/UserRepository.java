package com.roczyno.userservice.repository;

import com.roczyno.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
	User findByEmail(String email);
	User findByUsername(String username);
}
