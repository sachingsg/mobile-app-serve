package com.gsg.services;

import java.util.List;

import com.gsg.error.GenericException;
import com.gsg.mongo.model.SalesUser;

public interface SalesUserService {

	List<SalesUser> getAllSalesUserDetails();

	boolean checkIfUserEmployeeIdExists(String empId);

	boolean isReferralCodeValid(String referralCd);

	void deleteSalesUser(String empId);

	void saveSalesUserDetails(SalesUser salesUser) throws GenericException;

	void updateSalesUserDetails(String empId, SalesUser salesUser) throws GenericException;

	SalesUser getSalesUserDetails(String empId) throws GenericException;

	SalesUser getSalesUserDetailsByReferralCode(String refCd);
	boolean checkReferralCdAvailability(String referralCd);

}