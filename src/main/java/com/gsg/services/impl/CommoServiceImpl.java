package com.gsg.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.Cart;
import com.gsg.mongo.model.Order;
import com.gsg.mongo.model.OrderDetails;
import com.gsg.services.AppUserService;
import com.gsg.services.CartService;
import com.gsg.services.CommonService;
import com.gsg.services.MasterDataService;
import com.gsg.services.OrderDetailService;
import com.gsg.services.OrderService;
import com.gsg.services.ServiceModule;
import com.gsg.utilities.GSGCommon;
import com.gsg.utilities.SMSUtility;

@Service
public class CommoServiceImpl implements CommonService {

	static Logger logger = LoggerFactory.getLogger(CommoServiceImpl.class);

	@Autowired
	CartService cartService;

	@Autowired
	AppUserService appUserService;

	@Autowired
	OrderService orderService;

	@Autowired
	ServiceModule serviceModule;

	@Autowired
	OrderDetailService orderDetailService;

	@Autowired
	MasterDataService masterDataService;

	@Autowired
	GSGCommon gsgCommon;

	@Autowired
	SMSUtility smsUtility;

	@Autowired
	ServiceModule serviceModules;

	private void getLatestAppVersion() {
		

	}
	
	// Order Related
	@Override
	public List<Order> getAllOrdersAssighedToUser(String userId) throws ResourceNotFoundException {
		logger.info("OrderServiceImpl.getAllOrdersOfaUser()");
		// check if user is present
		appUserService.getByUserID(userId);

		List<Order> olList = orderService.getOrdersAssignedToUserId(userId);
		// orderRepository.findByAssignedToUserId(userId, new
		// Sort(Direction.DESC,"creationDate"));
		return olList;
	}

	@Override
	public List<Order> getAllOrdersOfaUser(String userId) throws ResourceNotFoundException {
		logger.info("OrderServiceImpl.getAllOrdersOfaUser()");

		// check if user is present
		appUserService.getByUserID(userId);

		List<Order> olList = orderService.getByUserIdSortedByCreationDate(userId);
		// orderRepository.findByUserId(userId, new
		// Sort(Direction.DESC,"creationDate"));
		return olList;
	}
	@Override
	public List<OrderDetails> getOrdersByUserIdAndProdutType(String userId, String productType)
			throws ResourceNotFoundException {
		logger.info("OrderDetailServiceImpl.getByUserIdAndProdutType()");
		appUserService.getByUserID(userId);
		return orderDetailService.getByUserIdAndProdutType(userId, productType);
	}
	// Cart Related
	@Override
	public List<Cart> getCartDetailsByUserId(String userId) throws ResourceNotFoundException {
		logger.info("CartServiceImpl.getCartDetailsByUserId()");
		appUserService.getByUserID(userId);
		List<Cart> cartList = cartService.getByUserId(userId);
		return cartList;
	}

	@Override
	public List<Cart> getCartDetailsByUserIdAndProductType(String userId, String productType)
			throws ResourceNotFoundException {
		logger.info("CartServiceImpl.getCartDetailsByUserIdAndProductType()");
		appUserService.getByUserID(userId);
		return cartService.getByUserIdAndProductType(userId, productType);
	}

}
