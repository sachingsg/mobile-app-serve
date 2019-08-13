package com.gsg.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gsg.component.ResponseWrapper;
import com.gsg.error.GenericException;
import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.AppUser;
import com.gsg.mongo.model.AppUser.MapLocation;
import com.gsg.mongo.model.AppUser.ServiceArea;
import com.gsg.mongo.model.OfficeDetails;
import com.gsg.mongo.model.Order;
import com.gsg.mongo.model.Order.FeedBack;
import com.gsg.mongo.model.SalesUser;
import com.gsg.mongo.model.ServiceRequest;
import com.gsg.services.AppUserService;
import com.gsg.services.DashBoardService;
import com.gsg.services.FeedBackService;
import com.gsg.services.OfficeDetailsService;
import com.gsg.services.OrderService;
import com.gsg.services.SalesUserService;
import com.gsg.services.ServiceModule;
import com.gsg.utilities.GSGCommon;
import com.gsg.utilities.SMSUtility;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

	private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

	@Autowired
	AppUserService appUserService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private DashBoardService dashBoardService;

	@Autowired
	private FeedBackService feedBackService;

	@Autowired
	ServiceModule serviceModule;

	@Autowired
	private SMSUtility smsUtility;

	@Autowired
	OfficeDetailsService officeService;
	
	@Autowired
	SalesUserService salesUserService;

	@GetMapping("/user/contact/{contactNbr}")
	// @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_OPERATION')")
	ResponseEntity<AppUser> getUserByContact(@PathVariable("contactNbr") String contactNbr)
			throws ResourceNotFoundException {
		logger.info("DashboardController.getUserByContact()");

		AppUser user = appUserService.getUserByContactNbr(contactNbr);
		logger.info("Retrieved User >>" + user);

		return new ResponseWrapper<>("Users Details found.", HttpStatus.OK, user).sendResponse();
	}

	// By Cc
	@PostMapping("/order/create")
	ResponseEntity<Order> createTicketForUser(@RequestBody ServiceRequest sReq) throws ResourceNotFoundException {
		logger.info("DashboardController.createTicketForUser()");
		Order order = serviceModule.emergencyServiceRequest(sReq);

		return new ResponseWrapper<>("Ticket Created", HttpStatus.OK, order).sendResponse();
	}

	@PostMapping("/user/create")
	ResponseEntity<AppUser> createUser(@RequestBody AppUser user) throws GenericException {
		logger.info("DashboardController.createUser()");
		dashBoardService.createNewUserByCC(user);
		return new ResponseWrapper<>("Users created successfully", HttpStatus.OK, user).sendResponse();
	}

	public void getServiceEngrByLocation() {

	}

	@PostMapping("/{orderId}/feedback")
	// @PreAuthorize("hasAuthority('ROLE_ADMIN','ROLE_OPERATION')")
	public ResponseEntity<Object> saveReviewsForByUser(@PathVariable String orderId, @RequestBody FeedBack fDetail)
			throws GenericException, ResourceNotFoundException {
		logger.info("DashboardController.saveReviewsForByUser()");

		fDetail.setUserType(GSGCommon.OPERATION);

		feedBackService.saveFeedBack(orderId, fDetail);
		return new ResponseWrapper<>("Review Saved", HttpStatus.OK).sendResponse();

	}

	@DeleteMapping("/{orderDtlId}/{position}/remove")
	// @PreAuthorize("hasAuthority('ROLE_ADMIN','ROLE_OPERATION')")
	public ResponseEntity<?> removeServiceFromOrder(@PathVariable String orderDtlId, @PathVariable int position)
			throws GenericException, ResourceNotFoundException, IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException {
		logger.info("DashboardController.removeServiceFromOrder()");

		serviceModule.removeServiceFromOrder(orderDtlId, position);
		return new ResponseWrapper<>("Service Removed.", HttpStatus.OK).sendResponse();

	}
	
	@PutMapping("/updateSA/{userId}")
	//@PreAuthorize("hasAnyAuthority('ROLE_OPERATION','ROLE_ADMIN')")
	public ResponseEntity<?> updateServiceAreaData(@PathVariable String userId, @RequestBody ServiceArea svcArea) throws ResourceNotFoundException, GenericException {
		logger.info("AppUserController.updateServiceAreaData()");

		AppUser user = appUserService.updateServiceArearDetails(userId, svcArea);
		return new ResponseWrapper<>("Ticket Counts", HttpStatus.OK, user).sendResponse();
	}

	@PostMapping("/nearest")
	private ResponseEntity<?> getServiceRegionDetails(@RequestBody MapLocation location) throws GenericException {
		logger.info("DashboardController.getServiceRegionDetails()");
		List<AppUser> userList =  appUserService.getUsersWithin(location);
		
		return new ResponseWrapper<>("Ticket Counts", HttpStatus.OK, userList).sendResponse();
		
	}
	
	// Office Details

	@GetMapping("/office")
	ResponseEntity<?> getOfficeDetails() throws Exception {

		logger.info("OfficeController.getOfficeDetails()");

		List<OfficeDetails> ofc = officeService.getOfficeDetails();
		return new ResponseWrapper<>("Office Details", HttpStatus.OK, ofc).sendResponse();

	}

	@PostMapping("/office")
	ResponseEntity<?> saveNewOfficeDetail(@RequestBody OfficeDetails ofc) throws GenericException {
		logger.info("OfficeController.saveNewOfficeDetail()");

		OfficeDetails ofcDtl = officeService.saveOfcDetail(ofc);

		return new ResponseWrapper<>("Office Details Saved", HttpStatus.OK, ofcDtl).sendResponse();
	}
	
	@GetMapping("/office/state/{state}")
	ResponseEntity<?> getOfficeByStateDetail(@PathVariable String state) {
		logger.info("DashboardController.getOfficeByStateDetail()");

		OfficeDetails ofcDtl = officeService.getByState(state);
		return new ResponseWrapper<>("Office Details", HttpStatus.OK, ofcDtl).sendResponse();
	}

	/////////
	//SalesUserDetails
	
	@GetMapping("/salesUser")
	ResponseEntity<?> getSalesUserDetails() {
		logger.info("DashboardController.getSalesUserDetails()");
		List<SalesUser> details = salesUserService.getAllSalesUserDetails();

		return new ResponseWrapper<>("Sales User Details", HttpStatus.OK, details).sendResponse();
	}
	
	@PostMapping("/salesUser")
	ResponseEntity<?> saveSalesUserDetails(@RequestBody SalesUser salesUser) throws GenericException{
		logger.info("DashboardController.saveSalesUserDetails()");
		salesUserService.saveSalesUserDetails(salesUser);
		return new ResponseWrapper<>("Sales User Details Saved", HttpStatus.OK, null).sendResponse();
	}
	
	@PutMapping("/salesUser/{empid}")
	ResponseEntity<?> updateSalesUserDetails(@PathVariable String empid, @RequestBody SalesUser salesUser) throws GenericException{
		logger.info("DashboardController.saveSalesUserDetails()");
		salesUserService.updateSalesUserDetails(empid, salesUser);
		return new ResponseWrapper<>("Sales User Details Updated", HttpStatus.OK, null).sendResponse();
	}
	
	@DeleteMapping("/salesUser/{empid}")
	ResponseEntity<?> deleteSalesUserDetails(@PathVariable String empid) throws GenericException{
		logger.info("DashboardController.deleteSalesUserDetails()");
		salesUserService.deleteSalesUser(empid);
		return new ResponseWrapper<>("Sales User Details Deleted", HttpStatus.OK, null).sendResponse();
	}
	
	
	

}
