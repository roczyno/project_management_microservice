package com.roczyno.springbootecommerceapi.service;

import com.roczyno.springbootecommerceapi.entity.Cart;
import com.roczyno.springbootecommerceapi.entity.Product;
import com.roczyno.springbootecommerceapi.entity.User;
import com.roczyno.springbootecommerceapi.request.CartItemRequest;
import com.roczyno.springbootecommerceapi.response.CartItemResponse;
import org.springframework.security.core.Authentication;

public interface CartItemService {
	CartItemResponse createCartItem (CartItemRequest cartItem,Authentication connectedUser);
	CartItemResponse updateCartItem(Authentication connectedUser,Long id, CartItemRequest cartItem);
	CartItemResponse isCartItemExist(Cart cart, Product product, User user, String size);
	void removeCartItem(Authentication connectedUser, Long cartItemId);
	CartItemResponse findCartItemById(Long cartItemId);
}
