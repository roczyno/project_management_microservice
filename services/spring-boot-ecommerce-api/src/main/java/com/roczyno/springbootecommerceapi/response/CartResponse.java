package com.roczyno.springbootecommerceapi.response;

import com.roczyno.springbootecommerceapi.entity.CartItem;

import java.util.Set;

public record CartResponse(
		Long id,
		Set<CartItem> cartItems,
		double totalPrice,
		int totalItems,
		int totalDiscountedPrice,
		int totalDiscount
) {
}
