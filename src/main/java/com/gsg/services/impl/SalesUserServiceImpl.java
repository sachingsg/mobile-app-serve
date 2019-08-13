package com.gsg.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.gsg.error.GenericException;
import com.gsg.mongo.model.SalesUser;
import com.gsg.mongo.repository.SalesUserRepository;
import com.gsg.services.SalesUserService;

@Service
public class SalesUserServiceImpl implements SalesUserService {
	Logger logger = LoggerFactory.getLogger(SalesUserServiceImpl.class);

	@Autowired
	private SalesUserRepository salesUserRepository;

	@Override
	public List<SalesUser> getAllSalesUserDetails() {
		return salesUserRepository.findAll(new Sort(Direction.DESC, "employeeId"));
	}

	@Override
	public SalesUser getSalesUserDetails(String empId) throws GenericException {
		SalesUser salesUser = salesUserRepository.findOne(empId);
		if (ObjectUtils.isEmpty(salesUser)) {
			throw new GenericException("Sales User Details Not found");
		}

		return salesUser;
	}

	@Override
	public boolean checkIfUserEmployeeIdExists(String empId) {
		return salesUserRepository.exists(empId);
	}

	@Override
	public boolean isReferralCodeValid(String referralCd) {
		SalesUser salesUser = salesUserRepository.findByRefCode(referralCd);
		return ObjectUtils.isEmpty(salesUser) ? false : true;
	}

	@Override
	public boolean checkReferralCdAvailability(String referralCd) {
		SalesUser salesUser = salesUserRepository.findByRefCode(referralCd);
		return ObjectUtils.isEmpty(salesUser) ? true : false;
	}

	@Override
	public void deleteSalesUser(String empId) {
		salesUserRepository.delete(empId);
	}

	@Override
	public void saveSalesUserDetails(SalesUser salesUser) throws GenericException {

		// 1. Verify Employee Id
		if (checkIfUserEmployeeIdExists(salesUser.getId()))
			throw new GenericException("Employee Id is already present. ");
		// 1. Verify Referral Code
		if (!checkReferralCdAvailability(salesUser.getRefCode()))
			throw new GenericException("Referral Code is already used.");

		salesUserRepository.save(salesUser);
	}

	@Override
	public void updateSalesUserDetails(String empId, SalesUser salesUser) throws GenericException {

		SalesUser dbSalesUser = getSalesUserDetails(empId);

		if (!dbSalesUser.getRefCode().equals(salesUser.getRefCode())) {
			if (!checkReferralCdAvailability(salesUser.getRefCode())) {
				throw new GenericException("Referral Code is already used.");
			} else {
				dbSalesUser.setRefCode(salesUser.getRefCode());
			}
		}

		dbSalesUser.setName(salesUser.getName());

		// salesUser.setId(dbSalesUser.getId());
		salesUserRepository.save(dbSalesUser);
	}

	@Override
	public SalesUser getSalesUserDetailsByReferralCode(String refCd) {
		return salesUserRepository.findByRefCode(refCd);
	}

}
