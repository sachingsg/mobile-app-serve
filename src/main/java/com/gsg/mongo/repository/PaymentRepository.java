package com.gsg.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gsg.utilities.PaymentGateway.PGResponse;

public interface PaymentRepository extends MongoRepository<PGResponse, String>{
	PGResponse findByReferenceno(String refNbr);
	
	PGResponse findBysubmerchantid(String subMerchantId);
}
