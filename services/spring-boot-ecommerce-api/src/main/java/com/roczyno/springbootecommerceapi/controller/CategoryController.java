package com.roczyno.springbootecommerceapi.controller;

import com.roczyno.springbootecommerceapi.request.CategoryRequest;
import com.roczyno.springbootecommerceapi.service.CategoryService;
import com.roczyno.springbootecommerceapi.util.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("category")
@RequiredArgsConstructor
public class CategoryController {
	private final CategoryService categoryService;
	@GetMapping
	public ResponseEntity<Object> getAllCategories(){
		return ResponseHandler.successResponse(categoryService.getAllCategories(), HttpStatus.OK);
	}
	@PutMapping("{id}")
	public ResponseEntity<Object> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest req){
		return ResponseHandler.successResponse(categoryService.updateCategory(req,id),HttpStatus.OK);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Object>deleteCategory(@PathVariable Long id){
		return ResponseHandler.successResponse(categoryService.deleteCategory(id),HttpStatus.OK);
	}

}
