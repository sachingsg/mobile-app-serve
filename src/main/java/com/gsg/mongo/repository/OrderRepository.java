package com.gsg.mongo.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.gsg.mongo.model.Order;
import com.gsg.mongo.model.Order.FeedBack;

public interface OrderRepository extends MongoRepository<Order, String> {
	
	Order findByOrderId(String orderId);

	// void findByLocationNear(Point point, Distance max);

	List<Order> findByUserId(String userId, Sort sort);
	
	Order findByRtTktId(String tktId);

	List<Order> findByAssignedToUserId(String userId, Sort sort);

	@Query(value = "{requestStatus:{ $eq : ?0 }}", fields = "{requestStatus : 1, orderId : 1, produtType:1, userName:1, contactNbr:1,serviceDate:1, totalPrice:1, assignedToUserId:1}")
	List<Order> findByRequestStatus(String requestStatus);

	@Query(value = "{}", fields = "{requestStatus : 1, orderId : 1, produtType:1, userName:1, contactNbr:1,serviceDate:1, totalPrice:1, assignedToUserId:1}")
	List<Order> getAllOrdersMinimum();
	
	@Query(value = "{produtType:{ $ne : 'SCHEME' }}", fields = "{requestStatus : 1, orderId : 1, produtType:1, userName:1, contactNbr:1,serviceDate:1, totalPrice:1, assignedToUserId:1}")
	List<Order> getAllOrdersMinimumWithoutScheme();

	Order findByOrderDtlsId(String string);

	@Query(fields = "{feedBack:1}", value = "{orderId: ?0}")
	FeedBack getOrderFeedback(String orderId);

	@Query(fields = "{feedBack:1}", value = "{feedBack.feedBacks.userType : ?1}")
	FeedBack findByFeedBackFeedBacksUserType(String orderId, String userType);
	
	
	// Near findByLocationNear(Point point) {"location" : {"$near" : [x,y]}}

	// Near findByLocationNear(Point point, Distance max) {"location" : {"$near"
	// : [x,y], "$maxDistance" : max}}

	// Near findByLocationNear(Point point, Distance min, Distance max)
	// {"location" : {"$near" : [x,y], "$minDistance" : min, "$maxDistance" :
	// max}}

	// Within findByLocationWithin(Circle circle) {"location" : {"$geoWithin" :
	// {"$center" : [ [x, y], distance]}}}

	// Within findByLocationWithin(Box box) {"location" : {"$geoWithin" :
	// {"$box" : [ [x1, y1], x2, y2]}}}

}
