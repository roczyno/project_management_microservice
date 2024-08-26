package com.roczyno.springbootecommerceapi.repository;

import com.roczyno.springbootecommerceapi.entity.Cart;
import com.roczyno.springbootecommerceapi.entity.CartItem;
import com.roczyno.springbootecommerceapi.entity.Product;
import com.roczyno.springbootecommerceapi.entity.User;
import com.roczyno.springbootecommerceapi.response.CartItemResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

	@Query("select c from CartItem c where c.product=:product and c.user=:user and c.size=:size and c.cart=:cart")
	CartItemResponse isCartItemExist(@Param("product") Product product, @Param("cart") Cart cart,
									 @Param("size") String size, @Param("user") User user);
}
