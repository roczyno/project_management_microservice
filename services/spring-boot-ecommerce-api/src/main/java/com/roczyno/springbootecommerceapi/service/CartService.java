package com.roczyno.springbootecommerceapi.service;

import com.roczyno.springbootecommerceapi.entity.User;
import com.roczyno.springbootecommerceapi.request.AddItemRequest;
import com.roczyno.springbootecommerceapi.response.CartResponse;
import org.springframework.security.core.Authentication;

public interface CartService {
	CartResponse createCart(User user);
	String addCartItem(Authentication connectedUser, AddItemRequest req,Long productId);
	CartResponse findUserCart(Authentication connectedUser);
}
