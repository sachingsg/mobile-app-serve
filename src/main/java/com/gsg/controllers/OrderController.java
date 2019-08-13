package com.gsg.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gsg.component.ResponseWrapper;
import com.gsg.error.GenericException;
import com.gsg.error.RTException;
import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.AddOnService;
import com.gsg.mongo.model.AppUser;
import com.gsg.mongo.model.Cart;
import com.gsg.mongo.model.CategoryCount;
import com.gsg.mongo.model.CodPayment;
import com.gsg.mongo.model.Order;
import com.gsg.mongo.model.OrderCheckout;
import com.gsg.mongo.model.OrderDetails;
import com.gsg.mongo.model.OrderStatus;
import com.gsg.mongo.model.ServiceRequest;
import com.gsg.mongo.model.master.Services;
import com.gsg.services.AppUserService;
import com.gsg.services.CommonService;
import com.gsg.services.OrderDetailService;
import com.gsg.services.OrderService;
import com.gsg.services.ServiceModule;
import com.gsg.utilities.GSGCommon;
import com.gsg.utilities.SMSUtility;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderDetailService orderDetailService;
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private SMSUtility smsUtility;
	
	@Autowired
	private ServiceModule serviceModule;

	@Autowired
	private CommonService commonService;

	@GetMapping
	// admin
	// @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	public ResponseEntity<?> getAllOrderDetails() {
		logger.info("OrderController.getAllOrderDetails()");

		List<Order> srList = orderService.getAllOrders();

		return new ResponseWrapper<>("List of orders", HttpStatus.OK, srList).sendResponse();
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getOrdersByUser(@PathVariable("userId") String userId) throws ResourceNotFoundException {
		logger.info("OrderController.getOrdersByUser()");

		List<Order> orderList = commonService.getAllOrdersOfaUser(userId);

		return new ResponseWrapper<>("List of orders", HttpStatus.OK, orderList).sendResponse();
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<?> getOrderDetailsByOrderId(@PathVariable("orderId") String orderId)
			throws ResourceNotFoundException {
		logger.info("OrderController.getOrderDetailsByOrderId()");

		Order order = orderService.getByOrderId(orderId);

		return new ResponseWrapper<>("List of orders", HttpStatus.OK, order).sendResponse();
	}

	@PostMapping
	public ResponseEntity<?> createRequest(@RequestBody ServiceRequest sReq)
			throws IOException, RTException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException,
			GenericException, ResourceNotFoundException {
		logger.info("OrderController.createRequest()");

		AppUser user = appUserService.getByUserID(sReq.getUserId());
		OrderCheckout oc = serviceModule.createServiceRequest(sReq, user);

		return new ResponseWrapper<>("Request Created", HttpStatus.OK, oc).sendResponse();
	}

	// by service Engineer
	@PostMapping("/{orderId}/addOn")
	// @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CS','ROLE_SE')")
	public ResponseEntity<?> addAdditionalServices(@PathVariable String orderId, @RequestBody List<AddOnService> aosList)
			throws IOException, RTException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException,
			GenericException, ResourceNotFoundException {
		logger.info("OrderController.addAdditionalServices()");

		serviceModule.addOnServiceRequest(orderId, aosList);

		return new ResponseWrapper<>("Added to user cart", HttpStatus.OK).sendResponse();
	}
	
	@GetMapping("/{orderId}/addOnCart/{userId}")
	// @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CS','ROLE_SE')")
	public ResponseEntity<?> addOnServiceOfUser(@PathVariable String orderId,@PathVariable String userId){
		logger.info("OrderController.addAdditionalServices()");

		List<Cart> cartList = serviceModule.addOnCartDetails(userId, orderId);

		return new ResponseWrapper<>("Add On Cart Details", HttpStatus.OK, cartList).sendResponse();
	}

	/////////////////////////////////

	@GetMapping("/{userId}/assigned")
	// @PreAuthorize("hasAnyAuthority('ROLE_SE','ROLE_CS')")
	public ResponseEntity<?> getOrdersAssignedToAUser(@PathVariable("userId") String userId)
			throws ResourceNotFoundException {

		logger.info("OrderController.getOrdersAssignedToAUser()");
		List<Order> orderList = commonService.getAllOrdersAssighedToUser(userId);

		return new ResponseWrapper<>("List of orders", HttpStatus.OK, orderList).sendResponse();
	}

	@PutMapping("/{ccUserId}/{orderId}")
	// @PreAuthorize("hasAnyAuthority('ROLE_SE','ROLE_CS','ROLE_ADMIN')")
	public ResponseEntity<?> updateOrderStatus(@PathVariable String ccUserId, @PathVariable String orderId, @RequestBody OrderStatus os)
			throws RTException, ResourceNotFoundException, GenericException {
		
		//appUserService.getUserByUserID(ccUserId)
		
		logger.info("OrderController.updateOrderStatus()");
		Order order = serviceModule.updateOrderStatusByOrderId(ccUserId, orderId, os);

		return new ResponseWrapper<>("List of orders", HttpStatus.OK, order).sendResponse();
	}

	@GetMapping("/count")
	// @PreAuthorize("hasAnyAuthority('ROLE_SE','ROLE_CS','ROLE_ADMIN')")
	public ResponseEntity<?> getAllOrdersByStatusCount() {
		logger.info("OrderController.getAllOrdersByCount()");

		List<CategoryCount> categoryCount = orderService.getAllOrderCountByStatus();
		return new ResponseWrapper<>("Ticket Counts", HttpStatus.OK, categoryCount).sendResponse();
	}

	@GetMapping("/status/{status}")
	// @PreAuthorize("hasAnyAuthority('ROLE_SE','ROLE_CS','ROLE_ADMIN')")
	public ResponseEntity<?> getAllordersByStatus(@PathVariable String status) {

		logger.info("OrderController.getAllordersByStatus()");
		List<Order> orderList = orderService.getOrdersByStaus(status);

		return new ResponseWrapper<>("List of orders", HttpStatus.OK, orderList).sendResponse();
	}
	
	@PutMapping("/orderDtl/{orderDtlId}")
	// @PreAuthorize("hasAnyAuthority('ROLE_SE','ROLE_CS','ROLE_ADMIN')")
	public ResponseEntity<?> updateOrderDetailsInfo(@PathVariable String orderDtlId, @RequestBody ServiceRequest sReq) throws ResourceNotFoundException {
		logger.info("OrderController.updateOrderDetailsInfo()");

		logger.info(sReq.toString());
		OrderDetails odtl =	orderDetailService.updateOrderDetails(orderDtlId, sReq);
		return new ResponseWrapper<>("Update OrderDetails Data", HttpStatus.OK, odtl).sendResponse();
	}

	@PostMapping("/confirmCODPayment/{orderDtlId}")
	public ResponseEntity<String> confirmCODPayment(HttpServletRequest request, @PathVariable String orderDtlId, 
			//@PathVariable double receivedAmnt
			@RequestBody CodPayment cod
			)
			throws ResourceNotFoundException, GenericException {
		
		logger.info("PaymentController.confirmCODPayment()");
		String msg = "";

		OrderDetails dbOrderDtl = orderDetailService.getById(orderDtlId);
		Order order = orderService.getByOrderDtlId(orderDtlId);
		// OrderDetails dbOrderDtl = orderService.getOrderDetail(dbOrderDtl.get)

		dbOrderDtl.setTransactionDate(LocalDateTime.now());
		dbOrderDtl.setTransactionStatus(GSGCommon.TRANSACTION_COMPLETED);
		
		double rcvAmnt = 0;
		
		if(cod.getCodMode().equals(GSGCommon.COD_CARD)){
			rcvAmnt = dbOrderDtl.getPayableAmount();
			if(StringUtils.trimToEmpty(cod.getPosReceiptNbr()).isEmpty()){
				throw new GenericException("Receipt Nbr is required for POS payment.");
			}
			
			dbOrderDtl.setPosReceiptNbr(cod.getPosReceiptNbr());
			
		}else{ //cash
			rcvAmnt = cod.getReceivedAmount();
			dbOrderDtl.setReceivedAmount(rcvAmnt);
			double codDiff = dbOrderDtl.getPayableAmount() - rcvAmnt;
			
			if(codDiff > 0){
				
				double per = (codDiff/dbOrderDtl.getPayableAmount())*100;
				if(per > 10){ // if discount is > 10%
					throw new GenericException("Disocunt amount can not be more than 10% of actual amount");
				}
				
				//check if labour component is present
				ServiceRequest sReq = (ServiceRequest)dbOrderDtl.getProduct();
				List<Services> svcList = sReq.getServices();
				boolean labourAdded = false;
				Services labourSvc = new Services();
				for(Services svc:svcList ){
					if(svc.getCategory().equals(GSGCommon.SVC_LABOUR)){
						labourAdded = true;
						labourSvc = svc;
						break;
					}
				}
				
				if(labourAdded){
					// change the labour price
//					double efePrc = labourSvc.getTotalPrice() - codDiff;
//					double pricePerHr = efePrc/labourSvc.getQuantity();
//					labourSvc.setPrice(new Price(pricePerHr));
					labourSvc.setDiscount(codDiff);
					
					//labourSvc.calculatePrices(sReq.getUsrVehicle().getVehicle().getType());
					dbOrderDtl.calculatePrices();
					
				}else{
					throw new GenericException("No discount can be given for this payment.");
				}
				
				//dbOrderDtl.setDiscount(codDiff);
			}else if(codDiff < 0){
				throw new GenericException("Can not receive more than payable amount");
			}
		}
		
		orderDetailService.saveDetails(dbOrderDtl);

		if (StringUtils.trimToEmpty(dbOrderDtl.getProdutType()).equals(GSGCommon.TYPE_SCHEME)) {
			serviceModule.subscribeScheme(dbOrderDtl);

			msg = "Your payment has been received successfully for your Request "+ order.getOrderId()+" and scheme has been activated.";
		} else {// services
			msg = "Your payment of INR " + GSGCommon.getTwoPrecisionStringValue(rcvAmnt) + " has been received sussessfully for your Request "+ order.getOrderId();
		}

		smsUtility.sendStatusMessageToUser(msg, order.getContactNbr());

		return new ResponseWrapper<>("COD Received", HttpStatus.OK, msg).sendResponse();

	}

}
