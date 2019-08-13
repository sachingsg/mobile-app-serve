package com.gsg.mongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gsg.mongo.model.OrderDetails;

public interface OrderDetailRepository extends MongoRepository<OrderDetails, String> {

	OrderDetails findById(String id);

	List<OrderDetails> findByUserIdAndProdutType(String id, String productType);
	

}
