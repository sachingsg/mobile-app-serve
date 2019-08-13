package com.gsg.mongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.gsg.mongo.model.master.VehicleData;
import com.gsg.mongo.model.master.VehicleData.Vehicle;

public interface VehicleMasterRepository extends MongoRepository<VehicleData, String> {
	
	List<Vehicle> findByMake(String make);
	
	@Query(value = "{$and:[{make:?0},{vehicle.model:?1}]}")
    Vehicle findVehicle(String make, String model);

	
}
