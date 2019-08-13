package com.gsg.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsg.error.GenericException;
import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.Order;
import com.gsg.mongo.model.Order.FeedBack;
import com.gsg.services.FeedBackService;
import com.gsg.services.OrderService;
import com.gsg.utilities.GSGCommon;

@Service
public class FeedBackServiceImpl implements FeedBackService {


	@Autowired
	private OrderService orderService;

	@Override
	public FeedBack getByOrderIdAndUserType(String orderId, String userType) {
		//return feedBackRepository.findByOrderIdAndFeedBacksUserType(orderId, userType);
		return orderService.getFeedBackByOrderIdAndUserType(orderId, userType);
	}

	@Override
	public FeedBack getByOrderIdAndUserId(String orderId, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveFeedBack(String orderId, FeedBack fDtl) throws GenericException, ResourceNotFoundException {

		Order order = orderService.getByOrderId(orderId);
		
		boolean allow = false;
		String orderSts = order.getRequestStatus();
		
		if(fDtl.getUserType().equals(GSGCommon.OPERATION)){
			if(orderSts.equals(GSGCommon.WORK_STS_RESOLVED)){
				allow = true;
			}
		}else if(fDtl.getUserType().equals(GSGCommon.USER)){
			if(orderSts.equals(GSGCommon.WORK_STS_CLOSED)){
				allow = true;
			}
		}
		if(allow){
			List<FeedBack> feedBkList = order.getFeedBacks();
			if (feedBkList.size() != 0) { // exists
				if(feedBkList.contains(fDtl)){
					throw new GenericException("Feedback has already been submitted");
				}else{
					feedBkList.add(fDtl);
				// feedback not present for the user type
				
				}
			}else{
				//new feedback for the user type
				feedBkList.add(fDtl);
			}
			
			orderService.saveOrder(order);
		}else{
			throw new GenericException("Feedback submission not allowed.");
		}

	}
}
