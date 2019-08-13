package com.gsg.services;

import java.util.List;

import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.OrderDetails;
import com.gsg.mongo.model.ServiceRequest;

public interface OrderDetailService {

	OrderDetails getById(String id) throws ResourceNotFoundException;
	
	//List<OrderDetails> getByUserIdAndProdutType(String userId, String productType) throws ResourceNotFoundException;
	List<OrderDetails> getByUserIdAndProdutType(String userId, String productType);
	OrderDetails saveDetails(OrderDetails dbOrderDtl);
	
	OrderDetails updateOrderDetails(String orderDtlId, ServiceRequest sr) throws ResourceNotFoundException;
}
