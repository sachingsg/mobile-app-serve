package com.gsg.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.CategoryCount;
import com.gsg.mongo.model.Order;
import com.gsg.mongo.model.Order.FeedBack;
import com.gsg.mongo.repository.CountersRepository;
import com.gsg.mongo.repository.OrderRepository;
import com.gsg.services.OrderService;
import com.gsg.utilities.GSGCommon;

@Service
public class OrderServiceImpl implements OrderService {

	Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CountersRepository counterRepo;
	

	@Override
	public List<Order> getAllOrders() {
		logger.info("OrderServiceImpl.getAllOrders()");
		return orderRepository.findAll(new Sort(Direction.DESC, "creationDate"));
	}


	@Override
	public List<Order> getOrdersAssignedToUserId(String userId) {
		return orderRepository.findByAssignedToUserId(userId, new Sort(Direction.DESC, "creationDate"));
	}

	@Override
	public Order saveOrderDetails(Order order) {
		logger.info("OrderServiceImpl.saveOrderDetails()");
		return orderRepository.save(order);
	}

	public String chooseCustomerCarePerson() {
		return "";
	}

	@Override
	public Order getByOrderId(String orderId) throws ResourceNotFoundException {
		logger.info("OrderServiceImpl.getOrderById()");
		Order order = orderRepository.findByOrderId(orderId);
		if (ObjectUtils.isEmpty(order)) {
			throw new ResourceNotFoundException(Order.class, "Order Id", String.valueOf(orderId));
		}
		return order;
	}

	@Override
	public List<Order> getByUserIdSortedByCreationDate(String userId) throws ResourceNotFoundException {
		logger.info("OrderServiceImpl.getOrderById()");
		List<Order> orderList = orderRepository.findByUserId(userId, new Sort(Direction.DESC, "creationDate"));

		return orderList;
	}

	@Override
	public Order getByOrderDtlId(String orderDtlId) {
		return orderRepository.findByOrderDtlsId(orderDtlId);
	}
	@Override
	public void saveOrder(Order o) {
		
		/*if(o.getFeedBack().getFeedBacks() == null || o.getFeedBack().getFeedBacks().size() == 0){
			FeedBack fb = new FeedBack();
		}*/
		orderRepository.save(o);

	}

	@Override
	public List<Order> getOrdersByStaus(String status) {
		logger.info("TicketServiceImpl.getOrdersByStaus()");
		if (status.equals("ALL")) {
			return orderRepository.getAllOrdersMinimumWithoutScheme();
		}
		return orderRepository.findByRequestStatus(status);
	}

	@Override
	public List<CategoryCount> getAllOrderCountByStatus() {
		logger.info("TicketServiceImpl.getAllTicketCount()");

		List<Order> orderList = orderRepository.getAllOrdersMinimum();
		List<CategoryCount> orderCatCountList = new ArrayList<CategoryCount>();
		int serviceTypeCount = 0;

		CategoryCount tktCat = null;
		for (Order tkt : orderList) {
			tktCat = new CategoryCount(tkt.getRequestStatus());
			if (orderCatCountList.contains(tktCat)) {
				int ix = orderCatCountList.indexOf(tktCat);
				CategoryCount tempTkt = orderCatCountList.get(ix);
				tempTkt.setCount(tempTkt.getCount() + 1);
			} else {
				orderCatCountList.add(tktCat);
			}

			if (tkt.getProdutType().equals(GSGCommon.TYPE_SERVICE)
					|| tkt.getProdutType().equals(GSGCommon.SERVICE_EMERGENCY)) {
				serviceTypeCount++;
			}
		}

		// count all
		// int totalCount = (int) orderRepository.countServiceType();
		orderCatCountList.add(new CategoryCount("ALL", serviceTypeCount));
		return orderCatCountList;
	}

	@Override
	public List<Order> getMinimalOrderDetails() {
		return orderRepository.getAllOrdersMinimum();

	}

	@Override
	public FeedBack getFeedBackByOrderId(String orderId) {
		return orderRepository.getOrderFeedback(orderId);
	}
	
	@Override
	public FeedBack getFeedBackByOrderIdAndUserType(String orderId, String userType) {
		return orderRepository.findByFeedBackFeedBacksUserType(orderId, userType);
	}
	
	@Override
	public String getInvoiceNbr() {
		String invoiceId = String.valueOf(counterRepo.findAndModifySeq("invoiceid").getSeq());
		return "GSG/1" + StringUtils.leftPad(invoiceId, 10, '0');
	}
}
