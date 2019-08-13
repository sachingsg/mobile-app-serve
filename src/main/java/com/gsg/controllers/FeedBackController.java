package com.gsg.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gsg.component.ResponseWrapper;
import com.gsg.error.GenericException;
import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.Order.FeedBack;
import com.gsg.services.FeedBackService;
import com.gsg.utilities.GSGCommon;

@RestController
@RequestMapping("/api/feedback")
public class FeedBackController {

	private static final Logger logger = LoggerFactory.getLogger(FeedBackController.class);
	@Autowired
	FeedBackService feedBackService;

	public List<FeedBack> getAllFeedBacks() {
		return null;

	}

	public void getFeedBackByOrder(String orderId) {

	}

	public void getFeedBackByOrderAndUserType(@PathVariable String orderId, @PathVariable String userId,
			@RequestBody FeedBack review) {

	}

	@PostMapping("/{orderId}")
	// @PreAuthorize("hasAuthority('ROLE_USER','ROLE_OPERATION')")
	public ResponseEntity<?> saveReviewsByUser(@PathVariable String orderId, @RequestBody FeedBack feedback)
			throws GenericException, ResourceNotFoundException {
		logger.info("FeedBackController.saveReviewsByUser()");

		feedback.setUserType(GSGCommon.USER);
		feedBackService.saveFeedBack(orderId, feedback);
		return new ResponseWrapper<>("FeedBack Saved", HttpStatus.OK).sendResponse();

	}

}
