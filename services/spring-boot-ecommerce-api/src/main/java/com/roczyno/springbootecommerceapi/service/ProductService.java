package com.roczyno.springbootecommerceapi.service;



import com.roczyno.springbootecommerceapi.entity.Category;
import com.roczyno.springbootecommerceapi.request.ProductRequest;
import com.roczyno.springbootecommerceapi.response.ProductResponse;

import java.util.List;

public interface ProductService {
	ProductResponse createProduct(ProductRequest req);
	String deleteProduct(Long productId);
	ProductResponse updateProduct(Long productId, ProductRequest req);
	ProductResponse findProductById(Long productId);
	List<ProductResponse> findProductByCategory(String category);
	List<ProductResponse> getAllProducts(String category, List<String> color, List<String> sizes, Integer minPrice,
								 Integer maxPrice, Integer minDiscount, String stock,String sort);

	List<ProductResponse> searchForProducts(String keyword);
}
