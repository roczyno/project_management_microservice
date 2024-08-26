package com.roczyno.springbootecommerceapi.repository;



import com.roczyno.springbootecommerceapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String userEmail);
}
