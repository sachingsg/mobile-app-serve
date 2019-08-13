package com.gsg.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gsg.mongo.model.master.SchemeData;

public interface UserSchemeDataRepository extends MongoRepository<SchemeData, String> {

}
