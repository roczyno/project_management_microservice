package com.roczyno.springbootecommerceapi.util;

import com.roczyno.springbootecommerceapi.entity.Cart;
import com.roczyno.springbootecommerceapi.response.CartResponse;
import org.springframework.stereotype.Service;

@Service
public class CartMapper {
	public CartResponse mapToCartResponse(Cart cart) {
		return new CartResponse(
				cart.getId(),
				cart.getCartItems(),
				cart.getTotalPrice(),
				cart.getTotalItems(),
				cart.getTotalDiscountedPrice(),
				cart.getTotalDiscount()
		);
	}
}
