package com.gsg.mongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gsg.mongo.model.master.Services;

public interface ServicesRepository extends MongoRepository<Services, String> {

	Services findByServiceId(String serviceId);

	List<Services> findByServiceIdIn(List<String> services);
	
	
}
