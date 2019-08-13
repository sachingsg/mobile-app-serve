package com.gsg.services;

import java.util.List;

import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.Cart;

public interface CartService {

	List<Cart> getByUserId(String userId) throws ResourceNotFoundException;

	Cart getByCartId(String cartId) throws ResourceNotFoundException;

	List<Cart> getByOrderId(String cartId);
	
	List<Cart> getByUserIdAndOrderId(String userId, String orderId);

	List<Cart> getByUserIdAndProductType(String userId, String productType) throws ResourceNotFoundException;

	Cart saveToCart(Cart c);

	void removeFromCart(Cart c);

	void removeFromCart(String cartId);

//	public OrderCheckout buyCartOrders(String userId, String orderId2) throws ResourceNotFoundException, GenericException,
//			NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException;
//
	//Cart addToUserCart(String orderId, double requestPrice, ServiceRequest sReq);
	
	List<Cart> removeFromUserCart(String userId, String cartId) throws ResourceNotFoundException;

	Cart getCartDataofUser(String cartId, String userId) throws ResourceNotFoundException;

//	OrderCheckout buyCartOrders(String userId, String cartId) throws ResourceNotFoundException, GenericException,
//			NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException;

}
