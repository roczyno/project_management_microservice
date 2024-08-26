package com.roczyno.springbootecommerceapi.service.impl;

import com.roczyno.springbootecommerceapi.entity.Category;
import com.roczyno.springbootecommerceapi.entity.Product;
import com.roczyno.springbootecommerceapi.exception.ProductException;
import com.roczyno.springbootecommerceapi.repository.ProductRepository;
import com.roczyno.springbootecommerceapi.request.CategoryRequest;
import com.roczyno.springbootecommerceapi.request.ProductRequest;
import com.roczyno.springbootecommerceapi.response.ProductResponse;
import com.roczyno.springbootecommerceapi.service.CategoryService;
import com.roczyno.springbootecommerceapi.service.ProductService;
import com.roczyno.springbootecommerceapi.util.CategoryMapper;
import com.roczyno.springbootecommerceapi.util.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	private final CategoryService categoryService;
	private final CategoryMapper categoryMapper;
	private final ProductRepository productRepository;
	private final ProductMapper productMapper;



	@Override
	@Transactional
	public ProductResponse createProduct(ProductRequest req) {
		Product product=Product.builder()
				.title(req.title())
				.description(req.description())
				.price(req.price())
				.discountPrice(req.discountPrice())
				.discountPricePercent(req.discountPricePercent())
				.quantity(req.quantity())
				.brand(req.brand())
				.color(req.color())
				.sizes(req.sizes())
				.imageUrl(req.imageUrl())
				.createdAt(LocalDateTime.now())
				.build();


		Category category=categoryService.findCategory(req.category().getName());
		if(category==null){
			Category newCategory=Category.builder()
					.name(req.category().getName())
					.build();
			Category newSavedCategory=categoryMapper
					.toMapToCategory(categoryService.addCategory(new CategoryRequest(newCategory.getName())));
			product.setCategory(newSavedCategory);
			return productMapper.mapToProductResponse(productRepository.save(product));
		}
		product.setCategory(category);
		return productMapper.mapToProductResponse(productRepository.save(product));
	}

	@Override
	public String deleteProduct(Long productId) {
		Product product=productMapper.mapToProduct(findProductById(productId));
		productRepository.delete(product);
		return "Product deleted successfully";
	}

	@Override
	public ProductResponse updateProduct(Long productId, ProductRequest req) {
		Product product=productMapper.mapToProduct(findProductById(productId));
		Product updatedProduct=updateExistingProduct(product,req);
		return productMapper.mapToProductResponse(updatedProduct);
	}

	private Product updateExistingProduct(Product existingProduct, ProductRequest req) {
		Optional.ofNullable(req.title()).ifPresent(existingProduct::setTitle);
		Optional.ofNullable(req.description()).ifPresent(existingProduct::setDescription);
		Optional.of(req.price()).ifPresent(existingProduct::setPrice);
		Optional.of(req.discountPrice()).ifPresent(existingProduct::setDiscountPrice);
		Optional.of(req.discountPricePercent()).ifPresent(existingProduct::setDiscountPricePercent);
		Optional.of(req.quantity()).ifPresent(existingProduct::setQuantity);
		Optional.ofNullable(req.brand()).ifPresent(existingProduct::setBrand);
		Optional.ofNullable(req.color()).ifPresent(existingProduct::setColor);
		Optional.ofNullable(req.sizes()).ifPresent(existingProduct::setSizes);
		Optional.ofNullable(req.imageUrl()).ifPresent(existingProduct::setImageUrl);
		Optional.ofNullable(req.category()).ifPresent(categoryRequest -> {
			Category category =categoryMapper.toMapToCategory(categoryService.getCategory(categoryRequest.getName()));
			if (category == null) {
				category = Category.builder()
						.name(categoryRequest.getName())
						.build();
				categoryService.addCategory(new CategoryRequest(category.getName()));
			}
			existingProduct.setCategory(category);
		});
		return productRepository.save(existingProduct);
	}

	@Override
	public ProductResponse findProductById(Long productId) {
		Product product=productRepository.findById(productId).
				orElseThrow(()->new ProductException("Product not found"));
		return productMapper.mapToProductResponse(product);
	}

	@Override
	public List<ProductResponse> findProductByCategory(String category) {
		List<Product> products=productRepository.findByCategory(category);
		return products.stream()
				.map(productMapper::mapToProductResponse)
				.toList();
	}

	@Override
	public List<ProductResponse> getAllProducts(String category, List<String> colors, List<String> sizes, Integer minPrice,
												Integer maxPrice, Integer minDiscount, String stock, String sort) {
		List<Product> products = productRepository.filterProduct(category, minPrice, maxPrice, minDiscount, sort);

		if (colors != null && !colors.isEmpty()) {
			products = products.stream()
					.filter(p -> colors.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor())))
					.collect(Collectors.toList());
		}
		if (sizes != null && !sizes.isEmpty()) {
			products = products.stream()
					.filter(p -> sizes.stream().anyMatch(s -> s.equalsIgnoreCase(p.getColor())))
					.collect(Collectors.toList());
		}



		if (stock != null) {
			products = switch (stock) {
				case "in_stock" -> products.stream()
						.filter(p -> p.getQuantity() > 0)
						.collect(Collectors.toList());
				case "out_of_stock" -> products.stream()
						.filter(p -> p.getQuantity() < 1)
						.collect(Collectors.toList());
				default -> products;
			};
		}

		return products.stream()
				.map(productMapper::mapToProductResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<ProductResponse> searchForProducts(String keyword) {
		List<Product> products=productRepository.search(keyword);
		return products.stream()
				.map(productMapper::mapToProductResponse)
				.collect(Collectors.toList());
	}

}
