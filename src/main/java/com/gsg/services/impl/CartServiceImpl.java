package com.gsg.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.Cart;
import com.gsg.mongo.repository.CartRepository;
import com.gsg.services.CartService;

@Service
public class CartServiceImpl implements CartService {
	private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

	@Autowired
	private CartRepository cartRepository;

	@Override
	public Cart getByCartId(String cartId) throws ResourceNotFoundException {
		logger.info("CartServiceImpl.getCartDetailsByCartId()");
		Cart cart = cartRepository.findById(cartId);
		if (ObjectUtils.isEmpty(cart)) {
			throw new ResourceNotFoundException(Cart.class, "id", cartId);
		}

		return cart;
	}

	@Override
	public List<Cart> getByOrderId(String orderId) {
		logger.info("CartServiceImpl.getCartDetailsByOrderId()");
		return cartRepository.findByOrderId(orderId);
	}
	
	@Override
	public List<Cart> getByUserIdAndOrderId(String userId, String orderId) {
		return cartRepository.findByUserIdAndOrderId(userId,orderId);
	}
	
	@Override
	public List<Cart> getByUserId(String userId) {
		logger.info("CartServiceImpl.getCartDetailsByOrderId()");
		return cartRepository.findByUserId(userId);
	}
	
	@Override
	public List<Cart> getByUserIdAndProductType(String userId, String productType) {
		logger.info("CartServiceImpl.getCartDetailsByOrderId()");
		return cartRepository.findByUserIdAndProductType(userId, productType);
	}

	/*@Override
	public List<Cart> getCartDetailsByUserId(String userId) throws ResourceNotFoundException {
		logger.info("CartServiceImpl.getCartDetailsByUserId()");
		userService.getUserByUserID(userId);
		List<Cart> cartList = cartRepository.findByUserId(userId);
		return cartList;
	}*/

	/*@Override
	public List<Cart> getCartDetailsByUserIdAndProductType(String userId, String productType)
			throws ResourceNotFoundException {
		logger.info("CartServiceImpl.getCartDetailsByUserIdAndProductType()");
		userService.getUserByUserID(userId);
		return cartRepository.findByUserIdAndProductType(userId, productType);
	}*/

	/*@Override
	public Cart addToUserCart(String orderId, double requestPrice, ServiceRequest sReq) {
		logger.info("CartServiceImpl.addToUserCart()");
		Cart cart = new Cart();
		cart.setOrderId(orderId);
		cart.setProductType(sReq.getServiceType());
		cart.setProduct(sReq);
		cart.setAmount(requestPrice);
		cart.setUserId(sReq.getUserId());
		cart.setServiceDate(sReq.getServiceDate());
		saveToCart(cart);

		return cart;
	}*/

	@Override
	public Cart saveToCart(Cart cart) {
		logger.info("CartServiceImpl.saveToCart()");
		return cartRepository.save(cart);
	}

	@Override
	public void removeFromCart(Cart c) {
		logger.info("CartServiceImpl.removeFromCart()");
		cartRepository.delete(c);

	}

	@Override
	public void removeFromCart(String cartId) {
		logger.info("CartServiceImpl.removeFromCart()");
		cartRepository.delete(cartId);

	}

	/*
	 * @Override public List<Cart> getCartDetailsById(String orderId) throws
	 * ResourceNotFoundException { List<Cart> cartOrder =
	 * cartRepository.findByOrderId(orderId);
	 * 
	 * if (ObjectUtils.isEmpty(cartOrder)) { throw new
	 * ResourceNotFoundException(Cart.class, "id", String.valueOf(orderId)); }
	 * 
	 * return cartOrder; }
	 */

	@Override
	public List<Cart> removeFromUserCart(String userId, String cartId) throws ResourceNotFoundException {
		Cart cart = getCartDataofUser(cartId, userId);
		cartRepository.delete(cart);

		return getByUserId(userId);
	}

	public Cart getCartDataofUser(String cartId, String userId) throws ResourceNotFoundException {
		//
		Cart cart = cartRepository.findByIdAndUserId(cartId, userId);
		if (ObjectUtils.isEmpty(cart)) {
			throw new ResourceNotFoundException(Cart.class, "cart id", cartId, "user id", userId);
		}
		return cart;
	}
	
	
}
