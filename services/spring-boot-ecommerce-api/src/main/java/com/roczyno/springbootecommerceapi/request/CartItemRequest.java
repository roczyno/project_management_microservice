package com.roczyno.springbootecommerceapi.request;

import com.roczyno.springbootecommerceapi.entity.Product;

public record CartItemRequest(
		Product product,
		String size,
		int quantity,
		Integer price,
		Integer discountedPrice
) {
}
