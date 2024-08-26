package com.roczyno.springbootecommerceapi.request;

import com.roczyno.springbootecommerceapi.entity.Category;


import java.util.Set;

public record ProductRequest(
		String title,
		String description,
		int price,
		int discountPrice,
		int discountPricePercent,
		int quantity,
		String brand,
		String color,
		Set<String> sizes,
		String imageUrl,
		Category category
) {
}
