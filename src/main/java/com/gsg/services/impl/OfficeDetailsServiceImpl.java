package com.gsg.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.gsg.error.GenericException;
import com.gsg.mongo.model.OfficeDetails;
import com.gsg.mongo.repository.OfficeRepository;
import com.gsg.services.OfficeDetailsService;

@Service
public class OfficeDetailsServiceImpl implements OfficeDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(OfficeDetailsServiceImpl.class);

	@Autowired
	OfficeRepository officeRepository;

	@Override
	public List<OfficeDetails> getOfficeDetails() {
		logger.info("OfficeDetailsService.getOfficeDetails()");
		return officeRepository.findAll();
	}

	@Override
	public OfficeDetails saveOfcDetail(OfficeDetails ofc) throws GenericException {
		OfficeDetails dbOfc = officeRepository.findOne(StringUtils.trimToEmpty(ofc.getId()));

		if (ObjectUtils.isEmpty(dbOfc)) {// new
			dbOfc = officeRepository.getByAddressState(ofc.getAddress().getState());
			if (!ObjectUtils.isEmpty(dbOfc)) {
				throw new GenericException("Multiple office per State is not allowed.");
			}
		}

		return officeRepository.save(ofc);

	}

	@Override
	public OfficeDetails getByState(String state) {
		logger.info("OfficeDetailsServiceImpl.getByState()");
		return officeRepository.getByAddressState(state);

	}
	
	@Override
	public List<String> getAllOfficeStates() {
		List<String> ofcList = new ArrayList<String>();
		List<OfficeDetails> ofcs = officeRepository.getAllByAddressState();
		for(OfficeDetails ofc:ofcs){
			ofcList.add(StringUtils.trimToEmpty(ofc.getAddress().getState()).toUpperCase());
		}
		return ofcList;
	}

}
