package com.gsg.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gsg.component.ResponseWrapper;
import com.gsg.mongo.model.SalesUser;
import com.gsg.services.SalesUserService;

@RestController
@RequestMapping("/api/salesusers")
public class SalesUserController {

	@Autowired
	private SalesUserService salesUserService;
	private static final Logger logger = LoggerFactory.getLogger(SalesUserController.class);

	@GetMapping("/referral/{refCd}")
	// @PreAuthorize("hasAnyAuthority('ROLE_OPERATION','ROLE_ADMIN')")
	public ResponseEntity<?> getSalesUserDetails(@PathVariable String refCd) {
		logger.info("SalesUserController.getSalesUserDetails()");

		SalesUser salesUser = salesUserService.getSalesUserDetailsByReferralCode(refCd);
		return new ResponseWrapper<>("Ticket Counts", HttpStatus.OK, salesUser).sendResponse();
	}
}
