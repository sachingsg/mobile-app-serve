package com.gsg.mongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.gsg.mongo.model.OfficeDetails;

public interface OfficeRepository extends MongoRepository<OfficeDetails, String> {
	@Query(value = "{address.state:{ $eq : ?0 }}", fields = "{}")
	OfficeDetails getByAddressState(String state);
	
	@Query(value = "{}", fields = "{address.state:1}")
	List<OfficeDetails> getAllByAddressState();
}
