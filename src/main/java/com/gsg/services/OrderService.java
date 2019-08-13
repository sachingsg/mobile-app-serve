package com.gsg.services;

import java.util.List;

import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.CategoryCount;
import com.gsg.mongo.model.Order;
import com.gsg.mongo.model.Order.FeedBack;

public interface OrderService {

	List<Order> getAllOrders();

	Order getByOrderId(String orderId) throws ResourceNotFoundException;

	//List<Order> getAllOrdersOfaUser(String userId) throws ResourceNotFoundException;

/*	Order emergencyServiceRequest(ServiceRequest sReq) throws ResourceNotFoundException;
	Cart addOnServiceRequest(String orderId, List<AddOnService> aosList) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, ResourceNotFoundException, GenericException;
	
	OrderCheckout createRequest(ServiceRequest sReq) throws IllegalArgumentException, IllegalAccessException,
							NoSuchFieldException, ResourceNotFoundException, GenericException, RTException;
*/	
	Order saveOrderDetails(Order order);
	Order getByOrderDtlId(String orderDtlId);
/*	OrderCheckout buyScheme(String userId, String schemeId) throws ResourceNotFoundException, GenericException;
	AppUser subscribeScheme(OrderDetails od) throws ResourceNotFoundException, GenericException;
	Cart normalServiceRequest(ServiceRequest sReq, double totalPricePerRequest) throws IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, ResourceNotFoundException, GenericException;
*/
	//void createTicket(String orderId, ServiceRequest sr) throws GenericException, ResourceNotFoundException;
	
	
	////////////////////


	//List<Order> getAllTicketsAssignedToUser(String userId) throws ResourceNotFoundException;

	//void updateRTdetailsToOrder(String orderId, String rtDetail) throws ResourceNotFoundException;

	List<Order> getOrdersByStaus(String status);

	List<CategoryCount> getAllOrderCountByStatus();

//	Order updateOrderStatusByOrderId(String orderId, String orderId2, OrderStatus os) throws ResourceNotFoundException, GenericException;

	//List<Order> getAllOrdersAssighedToUser(String userId) throws ResourceNotFoundException;
	public List<Order> getOrdersAssignedToUserId(String userId);

//	void cancelOrderProcessing(String orderId) throws ResourceNotFoundException, GenericException,
//			NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException;
//
	
	public List<Order> getMinimalOrderDetails();
	public List<Order> getByUserIdSortedByCreationDate(String userId) throws ResourceNotFoundException;

	void saveOrder(Order o);
	
	FeedBack getFeedBackByOrderId(String orderId);
	FeedBack getFeedBackByOrderIdAndUserType(String orderId, String userType);

	String getInvoiceNbr();
	
}
