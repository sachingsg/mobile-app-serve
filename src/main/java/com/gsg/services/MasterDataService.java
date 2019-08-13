package com.gsg.services;

import java.util.List;
import java.util.Set;

import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.master.SchemeData;
import com.gsg.mongo.model.master.Services;
import com.gsg.mongo.model.master.States;
import com.gsg.mongo.model.master.VehicleData;
import com.gsg.mongo.model.master.VehicleData.Vehicle;

public interface MasterDataService {

	// counter

	void resetAllSequence();

	void resetSequeceByID(String id);

	// Vehicle
	List<Vehicle> getModelsByMaker(String make);

	List<VehicleData> getAllVehicleDetails();

	void deleteAllVehicleData();

	void saveBulkVehicleData(List<VehicleData> vehicleList);

	// scheme
	boolean checkIfSchemePresent();

	SchemeData getScheme(String schemeId) throws ResourceNotFoundException;

	void deleteScheme(String schemeId) throws ResourceNotFoundException;

	void deleteAllSchemes();

	List<SchemeData> getAllSchemes();

	List<SchemeData> getAllActiveSchemes();

	SchemeData deactiveSchemeData(String schemeId) throws ResourceNotFoundException;

	// Service Related

	boolean checkIfServicePresent();

	Services getService(String ServiceId) throws ResourceNotFoundException;
	
	List<Services> getServiceIn(List<String> ServiceIds) throws ResourceNotFoundException;

	void deleteService(String ServiceId);

	Services updateService(Services Service, String ServiceId) throws ResourceNotFoundException;

	Services saveService(Services Service);

	void deleteAllServices();

	List<Services> getAllServices();

	void saveBulkSerivces(List<Services> services);

	// States related
	List<States> getAllStates();

	Set<String> getDistrictsOfState(String stateCd);

	void deleteAllStateData();

	void saveBulkStates(List<States> stateList);

}