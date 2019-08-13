package com.gsg.services.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.gsg.constants.AppUserConst;
import com.gsg.error.GenericException;
import com.gsg.mongo.model.AppUser;
import com.gsg.mongo.repository.AppUserRepository;
import com.gsg.mongo.repository.CountersRepository;
import com.gsg.services.DashBoardService;
import com.gsg.utilities.GSGCommon;
import com.gsg.utilities.SMSUtility;

@Service
public class DashBoardServiceImpl implements DashBoardService {

	Logger logger = LoggerFactory.getLogger(DashBoardServiceImpl.class);

	
	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	CountersRepository counterRepo;
	
	@Autowired
	SMSUtility smsUtility;
	
	@Autowired
	GSGCommon gsgCommon;
	
	@Override
	public AppUser createNewUserByCC(AppUser usr) throws GenericException{
		logger.info("DashBoardServiceImpl.createNewUser()");

		//Validate for minimal info
		//mobile Nbr
		if(StringUtils.trimToEmpty(usr.getContactNbr()).isEmpty()){
			throw new GenericException("Mobile Nbr is Mandatory");
		}else if(StringUtils.trimToEmpty(usr.getEmail()).isEmpty()){
			throw new GenericException("Email Address is Mandatory");
		}
		
		// validate if user is present or not
		
		AppUser dbUser = appUserRepository.findByContactNbr(usr.getContactNbr());
		if(!ObjectUtils.isEmpty(dbUser)){
			throw new GenericException("Mobile nbr is already registered.");
		}
		
		String userId = String.valueOf(counterRepo.findAndModifySeq("userid").getSeq());
		
		usr.setUserId(userId);
		usr.setPassword(bCryptPasswordEncoder.encode(AppUserConst.DEFAULT_PASSWORD));
		// Default Value
		if(usr.getRoles().isEmpty()){
			usr.getRoles().add("ROLE_USER");
		}
		
		usr.setActive(true);
		this.appUserRepository.save(usr);
		
		// Send Message upon creation
		String message = gsgCommon.getMsg("user.cc.welcome");
		message = message.replaceAll("\\{password}", AppUserConst.DEFAULT_PASSWORD);
		smsUtility.sendStatusMessageToUser(message, usr.getContactNbr());

		return usr;
	}
}
