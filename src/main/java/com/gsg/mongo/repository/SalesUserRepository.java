package com.gsg.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gsg.mongo.model.SalesUser;

public interface SalesUserRepository extends MongoRepository<SalesUser, String>  {

	SalesUser findByRefCode(String referralCd);
	SalesUser findById(String employeeId);
	boolean exists(String empId);
	
}
