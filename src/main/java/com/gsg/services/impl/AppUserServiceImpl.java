package com.gsg.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.mongodb.core.geo.Sphere;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.gsg.component.ResponseWrapper;
import com.gsg.constants.AppUserConst;
import com.gsg.error.GenericException;
import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.AppUser;
import com.gsg.mongo.model.AppUser.AddressBook;
import com.gsg.mongo.model.AppUser.MapLocation;
import com.gsg.mongo.model.AppUser.ServiceArea;
import com.gsg.mongo.model.AppUser.WsDoc;
import com.gsg.mongo.model.CategoryCount;
import com.gsg.mongo.model.LoginBean;
import com.gsg.mongo.model.UserVehicle;
import com.gsg.mongo.repository.AppUserRepository;
import com.gsg.mongo.repository.CountersRepository;
import com.gsg.services.AppUserService;
import com.gsg.utilities.GSGCommon;
import com.gsg.utilities.SMSUtility;

@Service
public class AppUserServiceImpl implements UserDetailsService, AppUserService {
	Logger logger = LoggerFactory.getLogger(AppUserServiceImpl.class);

	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	CountersRepository counterRepository;

	@Autowired
	SMSUtility smsUtility;

	@Autowired
	GSGCommon gsgCommon;

	// @Autowired
	// MongoFunctions mongoFunction;

	@Override
	public UserDetails loadUserByUsername(String mobileNbr) throws UsernameNotFoundException {
		logger.info("AppUserService.loadUserByUsername()");

		AppUser user = appUserRepository.findByContactNbr(mobileNbr);

		if (ObjectUtils.isEmpty(user)) {
			throw new UsernameNotFoundException(String.format("User with mobile: %s doesn't exists", mobileNbr));
		}
		logger.info("User retrieved >>" + user);

		// Set user roles to authorities List
		List<GrantedAuthority> authorities = new ArrayList<>();
		user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

		return new User(user.getContactNbr(), user.getPassword(), authorities);
	}

	@Override
	public List<AppUser> getAllUserDetails() throws ResourceNotFoundException {
		logger.info("AppUserService.getAllUserDetails()");

		List<AppUser> users = appUserRepository.findAll();
		if (ObjectUtils.isEmpty(users)) {
			throw new ResourceNotFoundException(AppUser.class);
		}
		return users;
	}

	@Override
	public List<CategoryCount> getUserCountByRole() {
		logger.info("AppUserServiceImpl.getUserCountByRole()");

		List<AppUser> userList = appUserRepository.getAllUserMinimum();
		List<CategoryCount> userCountList = new ArrayList<>();

		CategoryCount catCnt = null;
		for (AppUser usr : userList) {
			for (String role : usr.getRoles()) {
				catCnt = new CategoryCount(role);
				if (userCountList.contains(catCnt)) {
					int ix = userCountList.indexOf(catCnt);
					CategoryCount tempcc = userCountList.get(ix);
					tempcc.setCount(tempcc.getCount() + 1);
				} else {
					userCountList.add(catCnt);
				}
			}

		}
		// int totalCount = (int) appUserRepository.count();
		// count all
		// userCountList.add(new CategoryCount("ALL",totalCount));
		return userCountList;
	}

	@Override
	public List<AppUser> getAllUserOfRole(String role) {
		logger.info("AppUserServiceImpl.getAllUserOfRole()");

		List<AppUser> users = appUserRepository.findUsersByroleDetails(role);
		// appUserRepository.findUsersByroleDetails(role);
		return users;
	}

	@Override
	public AppUser getByUserID(String userId) throws ResourceNotFoundException {
		logger.info("UserService.getUserByID()-start");

		AppUser user = this.appUserRepository.findByUserId(userId);

		if (ObjectUtils.isEmpty(user)) {
			throw new ResourceNotFoundException(AppUser.class, "userId", userId);
		}
		logger.info("UserService.getUserByID()-end");
		return user;
	}

	@Override
	public AppUser getBasicDetailsById(String userId) throws ResourceNotFoundException {
		logger.info("UserService.getBasicDetailsById()-start");

		AppUser user = this.appUserRepository.getBasicDetailsById(userId);

		if (ObjectUtils.isEmpty(user)) {
			throw new ResourceNotFoundException(AppUser.class, "userId", userId);
		}
		logger.info("UserService.getBasicDetailsById()-end");
		return user;
	}
	
	@Override
	public List<AppUser> getBasicDetailsByIds(String[] userIds) throws ResourceNotFoundException {
		logger.info("UserService.getBasicDetailsById()-start");
		
		List<AppUser> users = this.appUserRepository.getBasicDetailsByIds(userIds);

//		if (ObjectUtils.isEmpty(users)) {
//			throw new Exception(AppUser.class, "users", users);
//		}
		logger.info("UserService.getBasicDetailsById()-end");
		return users;
	}

	@Override
	public AppUser getUserByContactNbr(String contactNbr) throws ResourceNotFoundException {
		logger.info("AppUserServiceImpl.getUserByContactNbr()");
		AppUser user = this.appUserRepository.findByContactNbr(contactNbr);

		if (ObjectUtils.isEmpty(user)) {
			throw new ResourceNotFoundException(AppUser.class, "contact", String.valueOf(contactNbr));
		}
		logger.info("UserService.getUserByID()-end");
		return user;
	}

	@Override
	public AppUser registerUser(AppUser usr) throws GenericException {
		logger.info("AppUserServiceImpl.createNewUser()");

		String userId = String.valueOf(counterRepository.findAndModifySeq("userid").getSeq());

		usr.setUserId(userId);
		if(!usr.getRoles().contains("ROLE_WORK_SHOP")) {
			usr.setPassword(bCryptPasswordEncoder.encode(usr.getPassword())); // skip password for intiating work shop user regd
		}
		// Default Value
		if (!usr.getRoles().contains("ROLE_USER")) {
			usr.getRoles().add("ROLE_USER");
		}
		usr.setActive(true);
		this.appUserRepository.save(usr);

		// Send Message upon creation
		smsUtility.sendStatusMessageToUser(gsgCommon.getMsg("user.welcome"), usr.getContactNbr());

		return usr;
	}
	
	@Override
	public AppUser updateUser(AppUser user) throws ResourceNotFoundException {

		logger.info("AppUserServiceImpl.updateUser()");
		return appUserRepository.save(user);

	}

	@Override
	public AppUser updateUserById(String userId, AppUser user) throws ResourceNotFoundException {

		logger.info("AppUserServiceImpl.updateUserById()");

		AppUser dbuser = getByUserID(userId);

		// need to chk////////////////////////
		// /////////
		user.setId(dbuser.getId());
		return appUserRepository.save(user);

	}

	@Override
	public void deleteUser(String userId) {
		logger.info("AppUserServiceImpl.deleteUser()");
		this.appUserRepository.delete(userId);
	}

	

	@Override
	public void deleteAllUser() {
		logger.info("AppUserServiceImpl.deleteAllUser()");
		this.appUserRepository.deleteAll();

	}

	@Override
	public void resetPassword(String contactNbr, String password) throws ResourceNotFoundException {

		logger.info("AppUserServiceImpl.resetPassword()");
		AppUser dbuser = getUserByContactNbr(contactNbr);
		dbuser.setPassword(bCryptPasswordEncoder.encode(password));
		appUserRepository.save(dbuser);

	}

	// Address related

	@Override
	public List<AddressBook> getUserAddress(String userId) throws ResourceNotFoundException {
		logger.info("AppUserServiceImpl.getUserAddress");
		AppUser user = getByUserID(userId);
		return user.getAddress();
	}

	@Override
	public AppUser saveAddress(String userId, AddressBook address) throws ResourceNotFoundException {

		logger.info("AppUserServiceImpl.saveAddress()");
		AppUser user = getByUserID(userId);

		user.getAddress().add(address);
		return this.appUserRepository.save(user);

	}

	@Override
	public AppUser updateAddress(String userId, AddressBook address, int position) throws ResourceNotFoundException {
		logger.info("AppUserServiceImpl.updateAddress()");
		AppUser user = getByUserID(userId);
		user.getAddress().set(position, address);
		return appUserRepository.save(user);

	}

	@Override
	public AppUser deleteAddress(String userId, int position) throws ResourceNotFoundException {
		logger.info("AppUserServiceImpl.deleteAddress()");
		AppUser user = getByUserID(userId);
		user.getAddress().remove(position);
		return appUserRepository.save(user);

	}

	// Vehicle related

	@Override
	public AppUser saveVehicle(String userId, UserVehicle vehicle) throws ResourceNotFoundException {
		logger.info("AppUserServiceImpl.saveVehicle()");
		AppUser user = getByUserID(userId);
		user.getUserVehicles().add(vehicle);
		return this.appUserRepository.save(user);
	}

	@Override
	public AppUser updateVehicle(String userId, UserVehicle vehicle, int position) throws ResourceNotFoundException {
		logger.info("AppUserServiceImpl.updateVehicle()");
		AppUser user = getByUserID(userId);

		user.getUserVehicles().set(position, vehicle);
		return appUserRepository.save(user);

	}

	@Override
	public AppUser deleteVehicle(String userId, int position) throws ResourceNotFoundException {
		logger.info("AppUserServiceImpl.deleteVehicle()");
		AppUser user = getByUserID(userId);
		user.getUserVehicles().remove(position);
		return appUserRepository.save(user);
	}

	@Override
	public AppUser changePasswordForUser(String userId, LoginBean loginBean) throws ResourceNotFoundException,
			GenericException {
		logger.info("AppUserServiceImpl.changePasswordForUser()");
		AppUser user = getByUserID(userId);
		if (bCryptPasswordEncoder.matches(loginBean.getPassword(), user.getPassword())) {
			user.setPassword(bCryptPasswordEncoder.encode(loginBean.getNewPassword()));
			appUserRepository.save(user);
		} else {
			throw new GenericException("current password is not valid");
		}

		return user;
	}
	
	@Override
	public List<AppUser> getUsersWithin(MapLocation location) throws GenericException{
		//longitude, latitude
		List<AppUser> userList = appUserRepository.findByRolesInAndServiceAreaMapLocationWithin(GSGCommon.ENGINEER,
				new Sphere(new Circle(location.getLng(),location.getLat() , 10/3963.2))); //in miles
		
		if(userList.size() == 0){
			throw new GenericException("Location is outside of our service area");
		}
		
		return userList;
	}

	@Override
	public AppUser updateServiceArearDetails(String userId, ServiceArea svcArea) throws ResourceNotFoundException, GenericException {
		AppUser user = getByUserID(userId);
		
		if(!user.getRoles().contains(GSGCommon.ENGINEER)){
			throw new GenericException("User should be Service Engineer");
		}
		
		user.setServiceArea(svcArea);
		
		return appUserRepository.save(user);
	
	}
	
	/*
	 * Workshop service starts
	 * */
	
	@Override
	public AppUser updateWorkShopStatus(String userId, String status) throws ResourceNotFoundException,GenericException {
		// TODO Auto-generated method stub
		logger.info("AppUserServiceImpl.updateWorkShopStatus()");

		AppUser user = getByUserID(userId);
		String message = null;
		switch (status) {
		case "pending":
		case "progress":
		case "rejected":
		case "onhold":
			user.setWsStatus(status);
			user = appUserRepository.save(user);
			// Send Message upon creation
			message = gsgCommon.getMsg("ws.statusUpdate");
			message = message.replaceAll("\\{name}", user.getFirstName()).replaceAll("\\{status}", status);
			smsUtility.sendStatusMessageToUser(message, user.getContactNbr());
			break;
		case "completed":
			// reset password
			user.setPassword(bCryptPasswordEncoder.encode(AppUserConst.DEFAULT_PASSWORD));
			user.setWsStatus(status);
			user = appUserRepository.save(user);
			// Send Message upon creation
			message = gsgCommon.getMsg("ws.statusUpdate");
			message = message.replaceAll("\\{name}", user.getFirstName())
						.replaceAll("\\{password}", AppUserConst.DEFAULT_PASSWORD)
						.replaceAll("\\{status}", status);
			smsUtility.sendStatusMessageToUser(message, user.getContactNbr());
			break;

		default:
			throw new GenericException("Not a valid status for workshop");
//			break;
		}
		return user;
	}

	@Override
	public List<AppUser> getWorkShopByStatus(String wsStatus) throws ResourceNotFoundException, GenericException {
		logger.info("AppUserServiceImpl.getWorkShopByStatus()");
		List<AppUser> user = this.appUserRepository.getWorkShopByStatus(wsStatus);

		if (ObjectUtils.isEmpty(user)) {
			throw new ResourceNotFoundException(AppUser.class, "work shop status", String.valueOf(wsStatus));
		}
		logger.info("UserService.getWorkShopByStatus()-end");
		return user;
	}

	@Override
	public AppUser updateWorkShopDocs(String userId, List<WsDoc> wsDocs)
			throws ResourceNotFoundException, GenericException {
		AppUser user = getByUserID(userId);
		user.setWsDocs(wsDocs);
		user = appUserRepository.save(user);
		return user;
	}

	@Override
	public AppUser updateGETDetails(AppUser appUser) throws ResourceNotFoundException, GenericException {
		AppUser user = getByUserID(appUser.getUserId());
		user.setPrimaryGet(appUser.getPrimaryGet());
		user.setAsstGet(appUser.getAsstGet());
		user = appUserRepository.save(user);
		return user;
	}

	@Override
	public List<AppUser> getNearestWorkShop(int distance, String location)
			throws ResourceNotFoundException, GenericException {
		logger.info("AppUserServiceImpl.getNearestWorkShop()");
		String[] temp = location.split(",");
		double[] dLocation = new double[] {Double.parseDouble(temp[0]),Double.parseDouble(temp[1])};
		List<AppUser> users = this.appUserRepository.getWorkShopByLocation(distance, dLocation);
		logger.info("UserService.getWorkShopByStatus()-end");
		return users;
	}
	
	
	
}
