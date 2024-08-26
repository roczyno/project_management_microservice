package com.roczyno.springbootecommerceapi.util;

import com.roczyno.springbootecommerceapi.entity.Category;
import com.roczyno.springbootecommerceapi.response.CategoryResponse;
import org.springframework.stereotype.Service;

@Service
public class CategoryMapper {
	public CategoryResponse mapToCategoryResponse(Category category) {
		return new CategoryResponse(
				category.getId(),
				category.getName()
		);
	}

	public Category toMapToCategory(CategoryResponse categoryResponse) {
		return Category.builder()
				.id(categoryResponse.id())
				.name(categoryResponse.name())
				.build();
	}


}
