package com.roczyno.springbootecommerceapi.service;

import com.roczyno.springbootecommerceapi.entity.Category;
import com.roczyno.springbootecommerceapi.request.CategoryRequest;
import com.roczyno.springbootecommerceapi.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
	CategoryResponse getCategory(String name);
	List<CategoryResponse> getAllCategories();
	CategoryResponse addCategory(CategoryRequest category);
	CategoryResponse updateCategory(CategoryRequest category, Long id);
	String deleteCategory(Long id);
	Category findCategory(String name);
}
