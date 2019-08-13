package com.gsg.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gsg.mongo.model.master.Counters;

public interface CountersRepository extends MongoRepository<Counters, String>, CountersRepositoryCustom {


}
