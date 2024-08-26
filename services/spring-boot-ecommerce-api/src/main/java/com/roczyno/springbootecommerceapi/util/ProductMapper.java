package com.roczyno.springbootecommerceapi.util;

import com.roczyno.springbootecommerceapi.entity.Product;
import com.roczyno.springbootecommerceapi.response.ProductResponse;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {
	public ProductResponse mapToProductResponse(Product saveProduct) {
		return new ProductResponse(
				saveProduct.getId(),
				saveProduct.getTitle(),
				saveProduct.getDescription(),
				saveProduct.getPrice(),
				saveProduct.getDiscountPrice(),
				saveProduct.getDiscountPricePercent(),
				saveProduct.getQuantity(),
				saveProduct.getBrand(),
				saveProduct.getColor(),
				saveProduct.getSizes(),
				saveProduct.getImageUrl(),
				saveProduct.getNumOfRatings(),
				saveProduct.getCategory(),
				saveProduct.getCreatedAt()
		);
	}

	public Product mapToProduct(ProductResponse productResponse) {
		return Product.builder()
				.id(productResponse.id())
				.title(productResponse.title())
				.description(productResponse.description())
				.price(productResponse.price())
				.discountPrice(productResponse.discountPrice())
				.discountPricePercent(productResponse.discountPricePercent())
				.quantity(productResponse.quantity())
				.brand(productResponse.brand())
				.color(productResponse.color())
				.sizes(productResponse.sizes())
				.imageUrl(productResponse.imageUrl())
				.numOfRatings(productResponse.numOfRatings())
				.category(productResponse.category())
				.createdAt(productResponse.createdAt())
				.build();
	}

}
