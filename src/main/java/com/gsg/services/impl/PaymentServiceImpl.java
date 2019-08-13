package com.gsg.services.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.gsg.error.GenericException;
import com.gsg.error.RTException;
import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.Cart;
import com.gsg.mongo.model.Order;
import com.gsg.mongo.model.OrderCheckout;
import com.gsg.mongo.model.ServiceRequest;
import com.gsg.mongo.model.master.SchemeData;
import com.gsg.mongo.repository.PaymentRepository;
import com.gsg.services.AppUserService;
import com.gsg.services.CartService;
import com.gsg.services.OrderService;
import com.gsg.services.PaymentService;
import com.gsg.services.ServiceModule;
import com.gsg.utilities.GSGCommon;
import com.gsg.utilities.PaymentGateway;
import com.gsg.utilities.PaymentGateway.PGResponse;
import com.gsg.utilities.SMSUtility;

@Service
public class PaymentServiceImpl implements PaymentService {

	Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

	@Autowired
	PaymentRepository paymentRepository;

	@Autowired
	PaymentGateway paymentGateway;

	@Autowired
	ServiceModule serviceModules;

	@Autowired
	private OrderService orderService;

	@Autowired
	private CartService cartService;


	@Autowired
	private AppUserService appUserService;

	@Autowired
	SMSUtility smsutility;
	
	// 
	/* This method will be called form both COD/PG
	 * For Service, both COD and PG is allowed
	 * 		Order is created and post processing is done
	 * 			
	 * For Scheme, only COD is applied
	 * 		Order is created
	 * 
	 */
	@Override
	public String processPayment(OrderCheckout oc) throws ResourceNotFoundException,
			GenericException, RTException {
		logger.info("PaymentController.processPayemnt()");

		String msg = "";
		Cart cart = cartService.getByCartId(oc.getReferenceno());
		appUserService.getByUserID(cart.getUserId());

		// /////// IF SERVICE, both PG and COD
		if (StringUtils.trimToEmpty(cart.getProductType()).equals(GSGCommon.TYPE_SERVICE)) // Service
		{
			ServiceRequest sr = (ServiceRequest) cart.getProduct();
			// 1. move from cart -> order, Order Detail - modification - COD/PAY
			Order order = serviceModules.createOrder(cart.getUserId(),cart.getOrderId(), cart.getProductType(), cart.getProduct(), cart.getAmount(), oc);
			// 2. Tkt creation
			//serviceModules.createTicket(order, sr);
			
			try{
				serviceModules.createTicketInRT(sr,order.getOrderId());
			}catch(Exception e){
				logger.error("RT Error " + e.getMessage());
			}
			
			
			/*if(StringUtils.trimToEmpty(cart.getOrderId()).isEmpty()){
				msg = "Your request has been received with Request Id - " + order.getOrderId()
						+ ". Our customer support will get back to you.";
			}else{
				msg = "Your Addon services has been added to your Request:"+order.getOrderId();
			}*/
			
			
		} else { // FOR SCHEME
			// for scheme - no tkt
			// add scheme to user
			SchemeData sd = (SchemeData) cart.getProduct();
			// Create Order
			Order order = serviceModules.createOrder(cart.getUserId(), null, cart.getProductType(), cart.getProduct(), cart.getAmount(), oc);

/*			if (!StringUtils.trimToEmpty(oc.getTranMode()).equals(GSGCommon.PAYMENT_COD)) { // Payment Gateway

				if (order.getOrderDtls().get(0) != null) {
					serviceModules.subscribeScheme(order.getOrderDtls().get(0));

					//msg = "Successfully subscribed to scheme - " + sd.getDescription();
				}

			} */

		}
		//
		// remove from cart
		cartService.removeFromCart(oc.getReferenceno());
		//smsutility.sendStatusMessageToUser(msg, user.getContactNbr());
		
		//return new ResponseWrapper<>("Request processed", HttpStatus.OK, msg).sendResponse();
		
		return msg;

	}

	@Override
	public String getPaymentStatus(String refNbr) throws ResourceNotFoundException, InterruptedException {
		logger.info("PaymentServiceImpl.getPaymentStatus()");

		String sts = "";
		int counter = 0;
		PGResponse dbPaymentDetails = null;
		while (true) {
			Thread.sleep(10000);
			counter++;
			logger.info("Called Counter >>" + counter);
			dbPaymentDetails = paymentRepository.findByReferenceno(refNbr);
			if (dbPaymentDetails != null) {
				logger.info("Trsaction details found");
				sts = dbPaymentDetails.getResponsecode();

				if (sts.equals("E000")) {
					sts = "SUCCESSFUL";
				} else {
					sts = "Payment Failed(" + sts + "). For any Help, Please contact GSG Support Team";
				}
				break;
			}

			if (counter > 20) { // After 20th try
				// logic for status check
				String paymentSts = paymentGateway.checkStatusOfAPayment(refNbr);
				if (!paymentSts.isEmpty()) {

					logger.info("PaymentServiceImpl.getPaymentStatus() - Check Status >>" + paymentSts);

					if (paymentSts.equalsIgnoreCase("Transaction Initiated")) {
						sts = "Your session has been timed out. Please retry.";
					} else if (paymentSts.equalsIgnoreCase("NotInitiated")) {
						sts = "NotInitiated";
					} else if (paymentSts.equalsIgnoreCase("RIP") || paymentSts.equalsIgnoreCase("SIP")) {
						sts = "Payment is in Progress. We will inform you once it is done.";
					} else {
						sts = paymentSts + " For more information, contact GSG Support Team";
					}

				} else {
					sts = "No Status found. For any Help, Please contact GSG Support Team";
				}
				logger.info("Payment Status >>" + sts +"::" + GSGCommon.getTransactionStatus(sts));

				break;
			}
		}

		if (ObjectUtils.isEmpty(sts)) {
			sts = "Payment Failed";
		}

		return sts;
	}

}
