package com.gsg.services;

import java.util.List;

import com.gsg.error.GenericException;
import com.gsg.mongo.model.OfficeDetails;

public interface OfficeDetailsService {

	List<OfficeDetails> getOfficeDetails();

	OfficeDetails getByState(String state);

	OfficeDetails saveOfcDetail(OfficeDetails ofc) throws GenericException;

	List<String> getAllOfficeStates();

}