package com.gsg.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.OrderDetails;
import com.gsg.mongo.model.ServiceRequest;
import com.gsg.mongo.repository.OrderDetailRepository;
import com.gsg.services.OrderDetailService;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

	static Logger logger = LoggerFactory.getLogger(OrderDetailServiceImpl.class);
	
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	@Override
	public OrderDetails getById(String orderDtlId) throws ResourceNotFoundException {
		logger.info("OrderDetailServiceImpl.getOrderDetaisById()");
		OrderDetails od = orderDetailRepository.findById(orderDtlId);
		if (od == null) {
			throw new ResourceNotFoundException(OrderDetails.class, "Order Detail Id", String.valueOf(orderDtlId));
		}
		return od;
	}

	@Override
	public OrderDetails saveDetails(OrderDetails dbOrderDtl) {
		logger.info("OrderDetailServiceImpl.saveDetails()");
		return orderDetailRepository.save(dbOrderDtl);
		
	}

	/*@Override
	public List<OrderDetails> getByUserIdAndProdutType(String userId, String productType) throws ResourceNotFoundException {
		logger.info("OrderDetailServiceImpl.getByUserIdAndProdutType()");
		userService.getUserByUserID(userId);
		return orderDetailRepository.findByUserIdAndProdutType(userId, productType);
	}*/

	@Override
	public List<OrderDetails> getByUserIdAndProdutType(String userId, String productType) {
		// TODO Auto-generated method stub
		return orderDetailRepository.findByUserIdAndProdutType(userId, productType);

	}
	
	@Override
	public OrderDetails updateOrderDetails(String orderDtlId, ServiceRequest sr) throws ResourceNotFoundException {
		
		OrderDetails orderDtl = getById(orderDtlId);
		orderDtl.setProduct(sr);
		orderDetailRepository.save(orderDtl);
		
		return orderDtl;
	}

}
