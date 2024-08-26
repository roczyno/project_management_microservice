package com.roczyno.springbootecommerceapi.service.impl;

import com.roczyno.springbootecommerceapi.entity.Cart;
import com.roczyno.springbootecommerceapi.entity.CartItem;
import com.roczyno.springbootecommerceapi.entity.Product;
import com.roczyno.springbootecommerceapi.entity.User;
import com.roczyno.springbootecommerceapi.exception.CartItemException;
import com.roczyno.springbootecommerceapi.repository.CartItemRepository;
import com.roczyno.springbootecommerceapi.request.CartItemRequest;
import com.roczyno.springbootecommerceapi.response.CartItemResponse;
import com.roczyno.springbootecommerceapi.service.CartItemService;
import com.roczyno.springbootecommerceapi.util.CartItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
	private final CartItemRepository cartItemRepository;
	private final CartItemMapper cartItemMapper;

	@Override
	public CartItemResponse createCartItem(CartItemRequest req,Authentication connectedUser) {
		User user=(User) connectedUser.getPrincipal();
		CartItem cartItem = CartItem.builder()
				.quantity(1)
				.discountedPrice(req.product().getDiscountPrice() * req.quantity())
				.price(req.product().getPrice() * req.quantity())
				.user(user)
				.build();
		return cartItemMapper.mapToCartItemResponse(cartItemRepository.save(cartItem));
	}

	@Override
	public CartItemResponse updateCartItem(Authentication connectedUser, Long id, CartItemRequest req) {
		User user=(User) connectedUser.getPrincipal();
		CartItem cartItem=cartItemMapper.mapToCartItem(findCartItemById(id));
		if(!cartItem.getUser().equals(user)){
			throw new CartItemException("Only the cart Item owner can modify");
		}
		cartItem.setQuantity(req.quantity());
		cartItem.setPrice(req.quantity()*req.product().getPrice());
		cartItem.setDiscountedPrice(req.product().getDiscountPrice()* cartItem.getQuantity());
		CartItem updatedCartItem= cartItemRepository.save(cartItem);
		return cartItemMapper.mapToCartItemResponse(updatedCartItem);
	}

	@Override
	public CartItemResponse isCartItemExist(Cart cart, Product product, User user, String size) {
		return cartItemRepository.isCartItemExist(product,cart,size,user);
	}

	@Override
	public void removeCartItem(Authentication connectedUser, Long id) {
		User user=(User) connectedUser.getPrincipal();
		CartItem cartItem=cartItemMapper.mapToCartItem(findCartItemById(id));
		if(!cartItem.getUser().equals(user)){
			throw new CartItemException("Only the cart Item owner can modify");
		}
		cartItemRepository.delete(cartItem);
	}

	@Override
	public CartItemResponse findCartItemById(Long cartItemId) {
		CartItem cartItem=cartItemRepository.findById(cartItemId)
				.orElseThrow(()-> new CartItemException("Cart Item not found"));
		return cartItemMapper.mapToCartItemResponse(cartItem);
	}
}
