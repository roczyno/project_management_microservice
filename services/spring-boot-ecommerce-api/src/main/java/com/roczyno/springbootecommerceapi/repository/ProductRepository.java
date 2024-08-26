package com.roczyno.springbootecommerceapi.repository;

import com.roczyno.springbootecommerceapi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

	@Query("select p from Product p where p.category.name=:category" )
	List<Product> findByCategory(@Param("category") String category);

	@Query("SELECT p FROM Product p " +
			"WHERE (:category IS NULL OR p.category.name = :category) " +
			"AND ((:minPrice IS NULL AND :maxPrice IS NULL) OR (p.discountPrice BETWEEN :minPrice AND :maxPrice)) " +
			"AND (:minDiscount IS NULL OR p.discountPricePercent >= :minDiscount) " +
			"ORDER BY " +
			"CASE WHEN :sort = 'price_low' THEN p.discountPrice END ASC, " +
			"CASE WHEN :sort = 'price_high' THEN p.discountPrice END DESC"
	)
	List<Product> filterProduct(@Param("category") String category,
								@Param("minPrice") Integer minPrice,
								@Param("maxPrice") Integer maxPrice,
								@Param("minDiscount") Integer minDiscount,
								@Param("sort") String sort);

	@Query("select p from Product p where p.title =:keyword or p.description=:keyword or p.brand=:keyword")
	List<Product> search(@Param("keyword") String keyword);
}
