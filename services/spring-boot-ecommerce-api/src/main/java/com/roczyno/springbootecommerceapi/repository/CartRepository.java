package com.roczyno.springbootecommerceapi.repository;

import com.roczyno.springbootecommerceapi.entity.Cart;
import com.roczyno.springbootecommerceapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
	Cart findByUser(User user);
}
