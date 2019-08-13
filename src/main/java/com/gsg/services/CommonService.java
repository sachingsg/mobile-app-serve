package com.gsg.services;

import java.util.List;

import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.Cart;
import com.gsg.mongo.model.Order;
import com.gsg.mongo.model.OrderDetails;

public interface CommonService {

	List<Cart> getCartDetailsByUserIdAndProductType(String userId, String productType) throws ResourceNotFoundException;

	List<Cart> getCartDetailsByUserId(String userId) throws ResourceNotFoundException;

	List<OrderDetails> getOrdersByUserIdAndProdutType(String userId, String productType)
			throws ResourceNotFoundException;

	List<Order> getAllOrdersAssighedToUser(String userId) throws ResourceNotFoundException;

	List<Order> getAllOrdersOfaUser(String userId) throws ResourceNotFoundException;

}