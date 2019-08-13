package com.gsg.controllers;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gsg.component.ResponseWrapper;
import com.gsg.error.GenericException;
import com.gsg.error.RTException;
import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.OrderCheckout;
import com.gsg.mongo.repository.PaymentRepository;
import com.gsg.services.AppUserService;
import com.gsg.services.OrderDetailService;
import com.gsg.services.OrderService;
import com.gsg.services.PaymentService;
import com.gsg.utilities.GSGCommon;
import com.gsg.utilities.PaymentGateway;
import com.gsg.utilities.PaymentGateway.PGResponse;
import com.gsg.utilities.SMSUtility;

@RestController
public class PaymentController {

	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

	@Autowired
	private OrderDetailService orderDetailService;

	@Autowired
	private AppUserService appUserService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	PaymentRepository paymentRepo;

	@Autowired
	SMSUtility smsutility;
	
	@Autowired
	GSGCommon gsgCommon;

	private PGResponse mapParamDataToObj(HttpServletRequest request) {
		logger.info("PaymentController.mapParamDataToObj()");

		Class<?> pclass = new PaymentGateway.PGResponse().getClass();
		Field fp = null;// pclass.getDeclaredField(uv.getType());

		PGResponse pr = new PGResponse();
		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			String paramValue = request.getParameter(paramName);
			logger.info(paramName + " :: " + paramValue);

			try {
				fp = pclass.getDeclaredField(paramName.replace(" ", "").trim().toLowerCase());
				fp.setAccessible(true);
				fp.set(pr, paramValue);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				logger.info(e.getMessage());
			}

		}

		logger.info("data:" + pr);

		return pr;

	}

	@PostMapping("/paymentFailure")
	public void paymentFailure(HttpServletRequest request) {
		logger.info("PaymentController.paymentFailure()");

		/*
		 * Enumeration paramNames = request.getParameterNames(); while
		 * (paramNames.hasMoreElements()) { String paramName = (String)
		 * paramNames.nextElement(); String paramValue =
		 * request.getParameter(paramName); logger.info(paramName + " :: " +
		 * paramValue); }
		 */

	}

	@PostMapping("/paymentResponse")
	public ResponseEntity<?> paymentResponse(@RequestBody PGResponse pr) throws IOException,
			ResourceNotFoundException, GenericException, RTException {
		logger.info("PaymentController.paymentResponse() called.");

		OrderCheckout oc = new OrderCheckout();
		//PGResponse pr = mapParamDataToObj(request);

		logger.info("<<PaymentController.paymentResponse()>><<Response::" + pr.getResponsecode());

		double tranAmount = 0;
		if(pr.getTransactionamount() != null && !pr.getTransactionamount().equals("null")){
			tranAmount = Double.valueOf(pr.getTransactionamount()).doubleValue();
		}
		
		oc.setAmount(tranAmount);
		oc.setPayableAmount(tranAmount);// 08102019 - added as order total price always shows 0.0
		oc.setReferenceno(pr.getReferenceno());
		oc.setTranMode(pr.getPaymentmode());
		oc.setTranDate(LocalDateTime.now());
		oc.setTranId(pr.getUniquerefnumber());
		oc.setUserId(pr.getSubmerchantid());
		oc.setTranSts(GSGCommon.TRANSACTION_COMPLETED);
		String tranResponse;
		if(pr.getRazorPayStatus().equalsIgnoreCase(GSGCommon.RAZOR_PAY_SUCCESS)) {
			// Save data to db
			String message = paymentService.processPayment(oc);
			tranResponse = GSGCommon.RAZOR_PAY_SUCCESS;
		}
		else {
			oc.setTranSts(GSGCommon.RAZOR_PAY_FAIL);
			tranResponse = GSGCommon.RAZOR_PAY_FAIL;
		}
		/*String tranResponse = GSGCommon.getTransactionStatus(pr.getResponsecode());
		
		if (StringUtils.trimToEmpty(pr.getResponsecode()).equals("E000")) {
			logger.info("Inside success");

//			oc.setAmount(Double.valueOf(pr.getTransactionamount()).doubleValue());
//			oc.setReferenceno(pr.getReferenceno());
//			oc.setTranMode(pr.getPaymentmode());
//			oc.setTranDate(LocalDateTime.now());
//			oc.setTranId(pr.getUniquerefnumber());
			oc.setTranSts(GSGCommon.TRANSACTION_COMPLETED);

			// Save data to db
			//paymentRepo.save(pr);

			String message = paymentService.processPayment(oc);
		}else{
			oc.setTranSts(tranResponse);
		}*/
		
		// Save data to db
		pr.setResponse(tranResponse);
		paymentRepo.save(pr);

		logger.info("After processing done");
		logger.info("paymentResponse OC >> " + oc);
		return new ResponseWrapper<>("Request Created", HttpStatus.OK, oc).sendResponse();
	}	
	
	/*@RequestMapping("/paymentResponse")
	public void paymentResponse(HttpServletRequest request, HttpServletResponse response) throws IOException,
			ResourceNotFoundException, GenericException, RTException {
		logger.info("PaymentController.paymentResponse() called.");

		OrderCheckout oc = new OrderCheckout();
		PGResponse pr = mapParamDataToObj(request);

		logger.info("<<PaymentController.paymentResponse()>><<Response::" + pr.getResponsecode());

		double tranAmount = 0;
		if(pr.getTransactionamount() != null && !pr.getTransactionamount().equals("null")){
			tranAmount = Double.valueOf(pr.getTransactionamount()).doubleValue();
		}
		
		oc.setAmount(tranAmount);
		oc.setReferenceno(pr.getReferenceno());
		oc.setTranMode(pr.getPaymentmode());
		oc.setTranDate(LocalDateTime.now());
		oc.setTranId(pr.getUniquerefnumber());
		oc.setUserId(pr.getSubmerchantid());
		
		String tranResponse = GSGCommon.getTransactionStatus(pr.getResponsecode());
		
		if (StringUtils.trimToEmpty(pr.getResponsecode()).equals("E000")) {
			logger.info("Inside success");

			oc.setAmount(Double.valueOf(pr.getTransactionamount()).doubleValue());
//			oc.setReferenceno(pr.getReferenceno());
//			oc.setTranMode(pr.getPaymentmode());
//			oc.setTranDate(LocalDateTime.now());
//			oc.setTranId(pr.getUniquerefnumber());

			// Save data to db
			//paymentRepo.save(pr);

			String message = paymentService.processPayment(oc);
		}else{
			oc.setTranSts(tranResponse);
		}
		
		// Save data to db
		pr.setResponse(tranResponse);
		paymentRepo.save(pr);

		logger.info("After processing done");
		logger.info("paymentResponse OC >> " + oc);
	}*/
	
	/*@PostMapping("/fake")
	public void fakePG(@RequestBody OrderCheckout oc) throws ResourceNotFoundException, GenericException, RTException {
		oc.setTranSts(GSGCommon.TRANSACTION_COMPLETED);
		paymentService.processPayment(oc);
	}*/
	
	// COD
	@PostMapping("/codPayemnt") // for services only(Normal service)
	public ResponseEntity<?> codPayemnt(@RequestBody OrderCheckout oc) throws ResourceNotFoundException,
			GenericException, RTException {
		logger.info("PaymentController.codPayemnt()");


		oc.setTranMode(GSGCommon.PAYMENT_COD);
		// oc.setTranDate(LocalDateTime.now());
		oc.setTranSts(GSGCommon.TRANSACTION_PENDING);

		String message = paymentService.processPayment(oc);
		return new ResponseWrapper<>("Request processed", HttpStatus.OK, message).sendResponse();
	}

	@GetMapping("/paymentStatus/{subMerchantId}")
	public ResponseEntity<String> getTransactionStatus(@PathVariable String subMerchantId)
			throws ResourceNotFoundException, InterruptedException, ExecutionException {

		logger.info("PaymentController.getTransactionStatus() called");

		String  tranSts = "Payment Failed";
		try {
			tranSts = paymentService.getPaymentStatus(subMerchantId);
		} catch (Exception e) {
			logger.error("Error Occured While getting response from bank." + e.getMessage());
		}

		logger.info("PaymentController.getTransactionStatus() called");
		logger.info("Transaction sts >>" + tranSts);

		return new ResponseWrapper<>("Payment Status", HttpStatus.OK, tranSts).sendResponse();

	}

	//sample
	/*@PostMapping("/fwd")
	public void fwd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("PaymentController.paymentFailure()");
		PrintWriter writer = response.getWriter();

		String htmlResponse = "";
		writer.println(htmlResponse);

	}*/

}
