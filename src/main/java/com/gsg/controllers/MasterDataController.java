package com.gsg.controllers;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gsg.component.ResponseWrapper;
import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.master.SchemeData;
import com.gsg.mongo.model.master.Services;
import com.gsg.mongo.model.master.States;
import com.gsg.mongo.model.master.VehicleData;
import com.gsg.mongo.model.master.VehicleData.Vehicle;
import com.gsg.services.MasterDataService;
import com.gsg.services.OrderService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/master")
@Api(value="Master")
public class MasterDataController {

	@Autowired
	public MasterDataService masterDataService;
	

	@Autowired
	public OrderService orderService;
	
	private static final Logger logger = LoggerFactory.getLogger(MasterDataController.class);

	@GetMapping("/vehicles")
	public ResponseEntity<?> getAllVeicleData() {
		logger.info("MasterDataController.getAllVeicleData()");
		
		List<VehicleData> vehicleList = masterDataService.getAllVehicleDetails();

		return new ResponseWrapper<>("List of Vehicles", HttpStatus.OK, vehicleList).sendResponse();
	}

	@GetMapping("/vehicles/{make}")
	public ResponseEntity<?> getAllVeicleByBrands(@PathVariable("make") String make) {
		List<Vehicle> vehicles = masterDataService.getModelsByMaker(make);

		return new ResponseWrapper<>("List of Vehicles By Maker", HttpStatus.OK, vehicles).sendResponse();

	}
	

	// Schemes
	@GetMapping("/schemes")
	public ResponseEntity<List<SchemeData>> getAllSchemes() {
		logger.info("MasterDataController.getAllSchemes()");
		
		List<SchemeData> schemeList = masterDataService.getAllActiveSchemes();

		return new ResponseWrapper<>("List of Schemes", HttpStatus.OK, schemeList).sendResponse();
		// return new ResponseEntity<>(schemeList, HttpStatus.OK);

	}

	@GetMapping("/schemes/{schemeId}")
	public ResponseEntity<SchemeData> getSchemeById(@PathVariable("schemeId") String schemeId)
			throws ResourceNotFoundException {
		logger.info("MasterDataController.getSchemeById()");
		
		SchemeData scheme = masterDataService.getScheme(schemeId);
		return new ResponseWrapper<>("Scheme Details", HttpStatus.OK, scheme).sendResponse();
		// return new ResponseEntity<>(scheme, HttpStatus.OK);

	}

	// Services
	@GetMapping("/services")
	public ResponseEntity<List<Services>> getAllServices() {
		logger.info("MasterDataController.getAllServices()");
		
		List<Services> serviceList = masterDataService.getAllServices();
		// ResponseWrapper<List<Services>> serviceList =
		return new ResponseWrapper<>("List of services", HttpStatus.OK, serviceList).sendResponse();
		// return new ResponseEntity<>(serviceList, HttpStatus.OK);

	}

	@GetMapping("/services/{serviceId}")
	public ResponseEntity<Services> getServiceById(@PathVariable("serviceId") String serviceId)
			throws ResourceNotFoundException {
		logger.info("MasterDataController.getServiceById()");
		
		Services dbService = masterDataService.getService(serviceId);
		return new ResponseWrapper<>("Service Detail", HttpStatus.OK, dbService).sendResponse();
		// return new ResponseEntity<>(dbService, HttpStatus.OK);

	}

	// states data
	@GetMapping("/states")
	public ResponseEntity<?> getAllStatesData() {
		logger.info("MasterDataController.getAllStatesData()");
		
		List<States> states = masterDataService.getAllStates();

		return new ResponseWrapper<>("List of States", HttpStatus.OK, states).sendResponse();
		// return new ResponseWrapper<>("List of States", HttpStatus.OK,
		// states).sendResponse();
	}

	@GetMapping("/states/{stateCd}")
	public ResponseEntity<?> getAllDistritOfState(@PathVariable("stateCd") String stateCd) {
		logger.info("MasterDataController.getAllDistritOfState()");
		
		Set<String> states = masterDataService.getDistrictsOfState(stateCd);

		return new ResponseWrapper<>("State Details", HttpStatus.OK, states).sendResponse();
		// return new ResponseWrapper<>("List of Districts", HttpStatus.OK,
		// states).sendResponse();
	}

}
