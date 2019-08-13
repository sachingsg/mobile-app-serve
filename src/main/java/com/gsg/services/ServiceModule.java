package com.gsg.services;

import java.util.List;

import com.gsg.error.GenericException;
import com.gsg.error.RTException;
import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.AddOnService;
import com.gsg.mongo.model.AppUser;
import com.gsg.mongo.model.Cart;
import com.gsg.mongo.model.Order;
import com.gsg.mongo.model.OrderCheckout;
import com.gsg.mongo.model.OrderDetails;
import com.gsg.mongo.model.OrderStatus;
import com.gsg.mongo.model.ServiceRequest;
import com.gsg.mongo.model.master.SchemeData.ReferralDetails;

public interface ServiceModule {

	// MODULE 1 - Data Validation
	double validateAndProcessSRData(ServiceRequest sReq, AppUser user2) throws IllegalArgumentException,
	IllegalAccessException, NoSuchFieldException, ResourceNotFoundException, GenericException;

	// MODULE 2 - ADD TO CART
	//public Cart addToUserCart(String orderId, double requestPrice, ServiceRequest sReq) {
	Cart addToUserCart(String userId, String orderId, Object product, String productType);

	Order createOrder(String userId,String orderId, String prdType, Object product, double orderTotalPrice, OrderCheckout oc)
			throws ResourceNotFoundException;

	//void postProcessingScheme();

	void postProcessingNormalService(Order o, ServiceRequest sr) throws ResourceNotFoundException;

	OrderCheckout checkOut(AppUser user, Cart cart, OrderCheckout oc);

	// MODULE 6 - RT TICKET CREATION
	void createTicketInRT(ServiceRequest sr, String string);

	void updateRTdetailsToOrder(String orderId, String rtDetail) throws ResourceNotFoundException;

	AppUser subscribeScheme(OrderDetails od) throws ResourceNotFoundException;

	OrderCheckout buyScheme(String userId, String schemeId, ReferralDetails rfrDtl) throws ResourceNotFoundException, GenericException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException;

	Order updateOrderStatusByOrderId(String ccUserId, String orderId, OrderStatus os) throws ResourceNotFoundException,
			GenericException;

//	OrderCheckout createServiceRequest(ServiceRequest sReq) throws IllegalArgumentException, IllegalAccessException,
//			NoSuchFieldException, ResourceNotFoundException, GenericException, RTException;

	OrderCheckout buyCartOrders(String userId, String cartId) throws ResourceNotFoundException, GenericException,
			NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException;

	Cart addOnServiceRequest(String orderId, List<AddOnService> aosList) throws ResourceNotFoundException, GenericException;

	
	
	Order emergencyServiceRequest(ServiceRequest sReq) throws ResourceNotFoundException;

	void cancelOrderProcessing(String orderId) throws ResourceNotFoundException, GenericException,
			NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException;

	OrderCheckout createServiceRequest(ServiceRequest sReq, AppUser user) throws IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, ResourceNotFoundException, GenericException, RTException;

	List<Cart> addOnCartDetails(String userId, String orderId);

	void removeServiceFromOrder(String orderDtlId, int postion) throws ResourceNotFoundException, GenericException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException;

	Cart removeServiceFromCart(String cartId, int svcPostion) throws GenericException, ResourceNotFoundException,
			IllegalArgumentException, IllegalAccessException, NoSuchFieldException;

	double calculateRequestPriceUsingOffer(AppUser user, ServiceRequest sReq);

	

}