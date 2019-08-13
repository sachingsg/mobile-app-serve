package com.gsg.mongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gsg.mongo.model.master.SchemeData;

public interface SchemeDataRepository extends MongoRepository<SchemeData, String> {

	SchemeData findBySchemeId(String SchemeId);

	List<SchemeData> findByActive(boolean active);

	List<SchemeData> findByActiveIsTrue();

	SchemeData findById(String schemeId);

}
