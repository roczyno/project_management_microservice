package com.roczyno.springbootecommerceapi.service.impl;

import com.roczyno.springbootecommerceapi.entity.Cart;
import com.roczyno.springbootecommerceapi.entity.CartItem;
import com.roczyno.springbootecommerceapi.entity.Product;
import com.roczyno.springbootecommerceapi.entity.User;
import com.roczyno.springbootecommerceapi.exception.CartException;
import com.roczyno.springbootecommerceapi.repository.CartRepository;
import com.roczyno.springbootecommerceapi.request.AddItemRequest;
import com.roczyno.springbootecommerceapi.request.CartItemRequest;
import com.roczyno.springbootecommerceapi.response.CartResponse;
import com.roczyno.springbootecommerceapi.service.CartItemService;
import com.roczyno.springbootecommerceapi.service.CartService;
import com.roczyno.springbootecommerceapi.service.ProductService;
import com.roczyno.springbootecommerceapi.util.CartItemMapper;
import com.roczyno.springbootecommerceapi.util.CartMapper;
import com.roczyno.springbootecommerceapi.util.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
	private final CartRepository cartRepository;
	private final CartMapper cartMapper;
	private final ProductService productService;
	private final ProductMapper productMapper;
	private final CartItemService cartItemService;
	private final CartItemMapper cartItemMapper;

	@Override
	public CartResponse createCart(User user) {
		Cart cart = Cart.builder()
				.user(user)
				.createdAt(LocalDateTime.now())
				.build();

		return cartMapper.mapToCartResponse(cartRepository.save(cart));
	}

	@Override
	public String addCartItem(Authentication connectedUser, AddItemRequest req, Long productId) {
		User user = (User) connectedUser.getPrincipal();
		Cart cart = cartRepository.findByUser(user);
		Product product = productMapper.mapToProduct(productService.findProductById(productId));
		CartItem isPresent = cartItemMapper
				.mapToCartItem(cartItemService.isCartItemExist(cart, product, user, req.size()));
		if (isPresent == null) {
			CartItem newCartItem=cartItemMapper.mapToCartItem(cartItemService.createCartItem(new CartItemRequest(
					product, req.size(), req.quantity(), req.price(),product.getDiscountPrice()
			), connectedUser));

			cart.getCartItems().add(newCartItem);
			cartRepository.save(cart);
		}
		cart.getCartItems().add(isPresent);
		cartRepository.save(cart);
		return "Cart item added successfully";
	}

	@Override
	@Transactional
	public CartResponse findUserCart(Authentication connectedUser) {
		User user = (User) connectedUser.getPrincipal();
		Cart cart = cartRepository.findByUser(user);
		if (cart == null) {
			throw new CartException("Cart does not exists");
		}
		int totalPrice = 0;
		int totalDiscountPrice = 0;
		int totalItems = 0;

		for (CartItem cartItem : cart.getCartItems()) {
			totalPrice = totalPrice + cartItem.getPrice();
			totalDiscountPrice = totalDiscountPrice + cartItem.getDiscountedPrice();
			totalItems = totalItems + cartItem.getQuantity();
		}
		cart.setTotalPrice(totalPrice);
		cart.setTotalDiscount(totalPrice - totalDiscountPrice);
		cart.setTotalItems(totalItems);
		cart.setTotalDiscountedPrice(totalDiscountPrice);
		return cartMapper.mapToCartResponse(cartRepository.save(cart));
	}
}
