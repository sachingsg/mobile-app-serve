package com.gsg.mongo.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.gsg.mongo.model.AppVersion;

public interface VersioRepository extends MongoRepository<AppVersion, String>{
	
	
	AppVersion findOneOrderByLastUpdateDt();
	
	List<AppVersion> findAllOrderByLastUpdateDt(Sort sort);
}
