package com.roczyno.springbootecommerceapi.controller;

import com.roczyno.springbootecommerceapi.entity.Category;
import com.roczyno.springbootecommerceapi.request.ProductRequest;
import com.roczyno.springbootecommerceapi.service.ProductService;
import com.roczyno.springbootecommerceapi.util.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("product")
public class ProductController {
	private final ProductService productService;

	@PostMapping
	public ResponseEntity<Object> addProduct(@RequestBody ProductRequest request){
		return ResponseHandler.successResponse(productService.createProduct(request), HttpStatus.OK);
	}
	@GetMapping
	public ResponseEntity<Object> getAllProducts( @RequestParam(required = false) String category,
												  @RequestParam(required = false) List<String> colors,
												  @RequestParam(required = false) List<String> sizes,
												  @RequestParam(required = false) Integer minPrice,
												  @RequestParam(required = false) Integer maxPrice,
												  @RequestParam(required = false) Integer minDiscount,
												  @RequestParam(required = false) String sort,
												  @RequestParam(required = false) String stock){
		return ResponseHandler.successResponse(productService.getAllProducts(
				category, colors, sizes, minPrice, maxPrice,
				minDiscount, sort, stock),HttpStatus.OK);

	}
	@GetMapping("/{productId}")
	public ResponseEntity<Object> getProduct(@PathVariable Long productId){
		return ResponseHandler.successResponse(productService.findProductById(productId),HttpStatus.OK);
	}

	@GetMapping("/cat")
	public ResponseEntity<Object> findProductByCategory(@RequestParam String cat){
		return ResponseHandler.successResponse(productService.findProductByCategory(cat),HttpStatus.OK);
	}

	@PutMapping("/{productId}")
	public ResponseEntity<Object> updateProduct(@PathVariable Long productId,@RequestBody ProductRequest req){
		return ResponseHandler.successResponse(productService.updateProduct(productId,req),HttpStatus.OK);
	}

	@DeleteMapping("/{productId}")
	public ResponseEntity<Object> deleteProduct(@PathVariable Long productId){
		return ResponseHandler.successResponse(productService.deleteProduct(productId),HttpStatus.OK);
	}
	@GetMapping("/search")
	public ResponseEntity<Object> searchForProducts(@RequestParam String keyword){
		return ResponseHandler.successResponse(productService.searchForProducts(keyword),HttpStatus.OK);
	}


}
