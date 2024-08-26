package com.roczyno.springbootecommerceapi.response;

import com.roczyno.springbootecommerceapi.entity.Product;

public record CartItemResponse(
		 Long id,
		 Product product,
		 String size,
		 int quantity,
		 Integer price,
		 Integer discountedPrice
) {
}
