package com.gsg.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;

import com.gsg.error.GenericException;
import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.AppUser;
import com.gsg.mongo.model.AppUser.AddressBook;
import com.gsg.mongo.model.AppUser.MapLocation;
import com.gsg.mongo.model.AppUser.ServiceArea;
import com.gsg.mongo.model.CategoryCount;
import com.gsg.mongo.model.LoginBean;
import com.gsg.mongo.model.UserVehicle;

public interface AppUserService {

	UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException;

	List<AppUser> getAllUserDetails() throws ResourceNotFoundException;

	List<AppUser> getAllUserOfRole(String role);

	AppUser getByUserID(String userId) throws ResourceNotFoundException;
	AppUser getBasicDetailsById(String userId) throws ResourceNotFoundException;
	List<AppUser> getBasicDetailsByIds(String[] userIds) throws ResourceNotFoundException;

	AppUser getUserByContactNbr(String cotactNbr) throws ResourceNotFoundException;

	AppUser registerUser(AppUser usr) throws GenericException;

	AppUser updateUserById(String userId, AppUser user) throws ResourceNotFoundException;
	AppUser updateWorkShopStatus(String userId, String status) throws ResourceNotFoundException,GenericException;
	AppUser updateWorkShopDocs(String userId, List<AppUser.WsDoc> wsDocs) throws ResourceNotFoundException,GenericException;
	AppUser updateGETDetails(AppUser appUser) throws ResourceNotFoundException,GenericException;
	List<AppUser> getWorkShopByStatus(String wsStatus) throws ResourceNotFoundException,GenericException;
	List<AppUser> getNearestWorkShop(int distance,String location) throws ResourceNotFoundException,GenericException;
	
	AppUser updateUser(AppUser user) throws ResourceNotFoundException;

	void deleteUser(String userId);

	List<AddressBook> getUserAddress(String userId) throws ResourceNotFoundException;
	
	// get user is done through getUser
	AppUser saveAddress(String userId, AddressBook address) throws ResourceNotFoundException;

	AppUser updateAddress(String userId, AddressBook address, int addId) throws ResourceNotFoundException;

	AppUser deleteAddress(String userId, int addPosition) throws ResourceNotFoundException;

	// get vehicle is done through getUser
	AppUser saveVehicle(String userId, UserVehicle vehicle) throws ResourceNotFoundException;

	AppUser updateVehicle(String userId, UserVehicle vehicle, int position) throws ResourceNotFoundException;

	AppUser deleteVehicle(String userId, int position) throws ResourceNotFoundException;

	void deleteAllUser();

	void resetPassword(String contactNbr, String password) throws ResourceNotFoundException;

	AppUser changePasswordForUser(String userId, LoginBean loginBean) throws ResourceNotFoundException,
			GenericException;

	List<CategoryCount> getUserCountByRole();
	
	AppUser updateServiceArearDetails(String userId, ServiceArea svcArea) throws ResourceNotFoundException, GenericException;

	List<AppUser> getUsersWithin(MapLocation location) throws GenericException;

}