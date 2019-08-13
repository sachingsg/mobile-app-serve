package com.gsg.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gsg.mongo.model.master.States;

public interface StatesRepository extends MongoRepository<States, String>{

	States findByStateCd(String stateCd);
	
	
}
