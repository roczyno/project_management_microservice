package com.roczyno.springbootecommerceapi.util;

import com.roczyno.springbootecommerceapi.entity.CartItem;
import com.roczyno.springbootecommerceapi.response.CartItemResponse;
import org.springframework.stereotype.Service;

@Service
public class CartItemMapper {
	public CartItemResponse mapToCartItemResponse(CartItem cartItem) {
		return new CartItemResponse(
				cartItem.getId(),
				cartItem.getProduct(),
				cartItem.getSize(),
				cartItem.getQuantity(),
				cartItem.getPrice(),
				cartItem.getDiscountedPrice()
		);

	}

	public CartItem mapToCartItem(CartItemResponse cartItemResponse) {
		return CartItem.builder()
				.id(cartItemResponse.id())
				.product(cartItemResponse.product())
				.size(cartItemResponse.size())
				.quantity(cartItemResponse.quantity())
				.price(cartItemResponse.price())
				.discountedPrice(cartItemResponse.discountedPrice())
				.build();
	}
}
