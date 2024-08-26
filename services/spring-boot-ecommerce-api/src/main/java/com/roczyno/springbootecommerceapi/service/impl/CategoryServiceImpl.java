package com.roczyno.springbootecommerceapi.service.impl;

import com.roczyno.springbootecommerceapi.entity.Category;
import com.roczyno.springbootecommerceapi.repository.CategoryRepository;
import com.roczyno.springbootecommerceapi.request.CategoryRequest;
import com.roczyno.springbootecommerceapi.response.CategoryResponse;
import com.roczyno.springbootecommerceapi.service.CategoryService;
import com.roczyno.springbootecommerceapi.util.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
	private final CategoryRepository categoryRepository;
	private final CategoryMapper categoryMapper;

	@Override
	public CategoryResponse getCategory(String name) {
		Category category=categoryRepository.findByName(name);
		return categoryMapper.mapToCategoryResponse(category);
	}
	public Category findCategory(String name){
		return categoryRepository.findByName(name);
	}

	@Override
	public List<CategoryResponse> getAllCategories() {
		List<Category> categories= categoryRepository.findAll();
		return categories.stream()
				.map(categoryMapper::mapToCategoryResponse)
				.toList();
	}

	@Override
	public CategoryResponse addCategory(CategoryRequest request) {
		Category category=Category.builder()
				.name(request.name())
				.build();
		return categoryMapper.mapToCategoryResponse(categoryRepository.save(category));
	}

	@Override
	public CategoryResponse updateCategory(CategoryRequest request, Long id) {
		Category category=categoryRepository.findById(id).orElseThrow();
		if(request.name()!=null){
			category.setName(request.name());
		}
		return categoryMapper.mapToCategoryResponse(categoryRepository.save(category));
	}

	@Override
	public String deleteCategory(Long id) {
		Category category=categoryRepository.findById(id).orElseThrow();
		categoryRepository.delete(category);
		return "category deleted";
	}


}
