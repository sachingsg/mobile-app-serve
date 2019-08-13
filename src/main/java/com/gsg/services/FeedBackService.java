package com.gsg.services;

import com.gsg.error.GenericException;
import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.Order.FeedBack;

public interface FeedBackService {

	FeedBack getByOrderIdAndUserType(String orderId, String userType);

	FeedBack getByOrderIdAndUserId(String orderId, String userId);

	// FeedBack saveFeedBackOfUser(FeedBack review) throws GenericException;

	void saveFeedBack(String orderId, FeedBack fDtl) throws GenericException, ResourceNotFoundException;

}