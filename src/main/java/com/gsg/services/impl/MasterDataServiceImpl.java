package com.gsg.services.impl;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.master.SchemeData;
import com.gsg.mongo.model.master.Services;
import com.gsg.mongo.model.master.States;
import com.gsg.mongo.model.master.VehicleData;
import com.gsg.mongo.model.master.VehicleData.Vehicle;
import com.gsg.mongo.repository.AppUserRepository;
import com.gsg.mongo.repository.CountersRepository;
import com.gsg.mongo.repository.SchemeDataRepository;
import com.gsg.mongo.repository.ServicesRepository;
import com.gsg.mongo.repository.StatesRepository;
import com.gsg.mongo.repository.UserSchemeDataRepository;
import com.gsg.mongo.repository.VehicleMasterRepository;
import com.gsg.services.MasterDataService;

@Service
public class MasterDataServiceImpl implements MasterDataService {

	private static final Logger logger = LoggerFactory.getLogger(MasterDataServiceImpl.class);
	@Autowired
	private VehicleMasterRepository vehicleMasterRepository;

	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	private UserSchemeDataRepository userSchemeDataRepository;

	@Autowired
	private ServicesRepository servicesRepository;
	@Autowired
	private SchemeDataRepository schemeRepository;

	@Autowired
	private StatesRepository statesRepository;

	@Autowired
	private CountersRepository counterRepository;

//	@Autowired
//	private MongoFunctions mongoFunction;

	// ////////////////
	// counter
	@Override
	public void resetAllSequence() {

	}

	@Override
	public void resetSequeceByID(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Vehicle> getModelsByMaker(String make) {
		logger.info("MasterDataServiceImpl.getModelsByMaker()");
		return vehicleMasterRepository.findByMake(make);

	}

	public List<VehicleData> getAllVehicleDetails() {
		logger.info("MasterDataServiceImpl.getAllVehicleDetails()");
		return vehicleMasterRepository.findAll(new Sort(Direction.ASC,"make"));

	}

	@Override
	public void deleteAllVehicleData() {
		logger.info("MasterDataServiceImpl.deleteAllVehicleData()");
		vehicleMasterRepository.deleteAll();

	}

	@Override
	public void saveBulkVehicleData(List<VehicleData> vehicleList) {
		vehicleMasterRepository.save(vehicleList);

	}

	// scheme related

	// SchemeData Related
	@Override
	public boolean checkIfSchemePresent() {
		return schemeRepository.count() > 0 ? true : false;
	}

	@Override
	public SchemeData getScheme(String schemeId) throws ResourceNotFoundException {
		SchemeData scheme = schemeRepository.findBySchemeId(schemeId);
		if (ObjectUtils.isEmpty(scheme)) {
			throw new ResourceNotFoundException(SchemeData.class, "id", String.valueOf(schemeId));
		}
		return scheme;
	}

	@Override
	// for admin only
	public void deleteScheme(String schemeId) {
		schemeRepository.delete(schemeId);
	}

	

	@Override
	// Test
	public void deleteAllSchemes() {
		schemeRepository.deleteAll();
		// /

	}

	@Override
	// user
	public List<SchemeData> getAllSchemes() {
		List<SchemeData> scemeList = schemeRepository.findAll();
		return scemeList;
	}

	@Override
	// user
	public List<SchemeData> getAllActiveSchemes() {
		List<SchemeData> scemeList = schemeRepository.findByActiveIsTrue();
		// schemeRepository.findByActive(true);
		return scemeList;
	}

	// user
	@Override
	public SchemeData deactiveSchemeData(String schemeId) throws ResourceNotFoundException {
		SchemeData dbScheme = schemeRepository.findBySchemeId(schemeId);
		if (ObjectUtils.isEmpty(dbScheme)) {
			throw new ResourceNotFoundException(SchemeData.class, "id", String.valueOf(schemeId));
		}
		dbScheme.setActive(false);
		return schemeRepository.save(dbScheme);
	}

	
	// Service Related

	@Override
	public boolean checkIfServicePresent() {
		return servicesRepository.count() > 0 ? true : false;
	}

	@Override
	public Services getService(String serviceId) throws ResourceNotFoundException {
		Services service = servicesRepository.findByServiceId(serviceId);
		if (ObjectUtils.isEmpty(service)) {
			throw new ResourceNotFoundException(Services.class, "id", String.valueOf(serviceId));
		}
		return service;
	}
	
	@Override
	public List<Services> getServiceIn(List<String> ServiceIds) throws ResourceNotFoundException {
		List<Services> serviceList = servicesRepository.findByServiceIdIn(ServiceIds);
		return serviceList;
	}

	@Override
	// for admin only
	public void deleteService(String schemeId) {
		schemeRepository.delete(schemeId);
	}

	@Override
	// admin
	public Services updateService(Services scheme, String serviceId) throws ResourceNotFoundException {
		Services dbServices = servicesRepository.findByServiceId(serviceId);

		if (dbServices == null) {
			throw new ResourceNotFoundException(Services.class, "id", String.valueOf(serviceId));
		}
		dbServices.setId(dbServices.getId());
		return servicesRepository.save(dbServices);
	}

	// Admin only
	@Override
	public Services saveService(Services service) {

		//String serviceId = mongoFunction.nextUserSequence("serviceid");
		String serviceId = String.valueOf(counterRepository.findAndModifySeq("serviceid").getSeq());
		
		service.setServiceId(serviceId);

		return servicesRepository.save(service);
	}

	@Override
	// Test
	public void deleteAllServices() {
		servicesRepository.deleteAll();
	}

	@Override
	// user
	public List<Services> getAllServices() {
		List<Services> scemeList = servicesRepository.findAll();
		return scemeList;
	}

	@Override
	public void saveBulkSerivces(List<Services> services) {
		services.forEach(service -> {
			saveService(service);
		});

	}

	// States related data

	@Override
	public List<States> getAllStates() {
		return statesRepository.findAll();
	}

	@Override
	public Set<String> getDistrictsOfState(String stateCd) {
		Set<String> dsts = new TreeSet<String>();
		States st = statesRepository.findByStateCd(stateCd);
		if (!ObjectUtils.isEmpty(st)) {
			dsts = st.getDistricts();
		}
		return dsts;
	}

	@Override
	public void deleteAllStateData() {
		statesRepository.deleteAll();

	}

	@Override
	public void saveBulkStates(List<States> stateList) {
		statesRepository.save(stateList);

	}


}
