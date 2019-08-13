package com.gsg.mongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gsg.mongo.model.Cart;

public interface CartRepository extends MongoRepository<Cart, String> {

	List<Cart> findByOrderId(String orderId);
	
	List<Cart> findByUserIdAndOrderId(String userId, String orderId);
	
	List<Cart> findByUserId(String userid);
	
	List<Cart> findByUserIdAndProductType(String userid, String productType);

	Cart findById(String cartId);
	
	Cart findByIdAndUserId(String cartId, String userId);
	

}
