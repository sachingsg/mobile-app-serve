package com.gsg.controllers;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gsg.component.ResponseWrapper;
import com.gsg.constants.AppUserConst;
import com.gsg.error.GenericException;
import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.AppUser;
import com.gsg.mongo.model.CategoryCount;
import com.gsg.mongo.model.LoginBean;
import com.gsg.mongo.model.OrderCheckout;
import com.gsg.mongo.model.master.SchemeData.ReferralDetails;
import com.gsg.services.AppUserService;
import com.gsg.services.ServiceModule;

@RestController
@RequestMapping("/api/users")
public class AppUserController {

	@Autowired
	private AppUserService userService;

	@Autowired
	ServiceModule serviceModule;

	// @Autowired
	// OAuth2Authentication authentication;

	private static final Logger logger = LoggerFactory.getLogger(AppUserController.class);

	@GetMapping
	//@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	ResponseEntity<?> getAllUser() throws ResourceNotFoundException, InterruptedException {
		logger.info("AppUserController.getAllUser()");
		List<AppUser> user = userService.getAllUserDetails();
		logger.info("AppUserController.getAllUser()-end");
		return new ResponseWrapper<>("List of Users", HttpStatus.OK, user).sendResponse();

	}

	@GetMapping("/role/{role}")
	//@PreAuthorize("hasAuthority('ROLE_OPERATION')")
	ResponseEntity<?> getAllUserOfRole(@PathVariable String role) throws ResourceNotFoundException {
		logger.info("AppUserController.getAllUserOfRole()");
		List<AppUser> user = userService.getAllUserOfRole(role.toUpperCase());
		return new ResponseWrapper<>("List of Users", HttpStatus.OK, user).sendResponse();
	}

	@GetMapping("/id/{id}")
	ResponseEntity<AppUser> getUserById(@PathVariable("id") String userId) throws ResourceNotFoundException {
		logger.info("AppUserController.getUserById()");

		AppUser user = userService.getByUserID(userId);
		logger.info("Retrieved User >>" + user);

		return new ResponseWrapper<>("Users Details", HttpStatus.OK, user).sendResponse();
		// return new ResponseEntity<AppUser>(user, HttpStatus.OK);
	}
	
	@GetMapping("/basic/{id}")
	ResponseEntity<AppUser> getBasicDetailsById(@PathVariable("id") String userId) throws ResourceNotFoundException {
		logger.info("AppUserController.getBasicDetailsById()");

		AppUser user = userService.getBasicDetailsById(userId);
		logger.info("Retrieved User >>" + user);

		return new ResponseWrapper<>("Users Details", HttpStatus.OK, user).sendResponse();
		// return new ResponseEntity<AppUser>(user, HttpStatus.OK);
	}

	@GetMapping("/contact/{contactNbr}")
	ResponseEntity<AppUser> getUserByContact(@PathVariable("contactNbr") String contactNbr)
			throws ResourceNotFoundException {
		logger.info("AppUserController.getUserByContact()");

		AppUser user = userService.getUserByContactNbr(contactNbr);
		logger.info("Retrieved User >>" + user);

		return new ResponseWrapper<>("Users Details found.", HttpStatus.OK, user).sendResponse();
		// return new ResponseEntity<AppUser>(user, HttpStatus.OK);
	}

	@PutMapping("/id/{id}")
	ResponseEntity<AppUser> updateUserById(@PathVariable("id") String userId, @RequestBody AppUser user)
			throws ResourceNotFoundException {
		logger.info("AppUserController.saveUserById()");

		userService.updateUserById(userId, user);
		return new ResponseWrapper<>("Users Details updated", HttpStatus.OK, user).sendResponse();
		// /return new ResponseEntity<AppUser>(user, HttpStatus.OK);
	}

	@PutMapping("/id/{id}/changePassword")
	ResponseEntity<AppUser> changePassword(@PathVariable("id") String userId, @RequestBody LoginBean loginBean)
			throws ResourceNotFoundException, GenericException {
		logger.info("AppUserController.changePassword()");

		AppUser user = userService.changePasswordForUser(userId, loginBean);
		return new ResponseWrapper<>("Password changed successfully", HttpStatus.OK, user).sendResponse();
	}

	/*@DeleteMapping("/id/{id}")
	//@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	ResponseEntity<AppUser> removeUser(@PathVariable("id") String userId) {
		logger.info("AppUserController.removeUser()");
		this.userService.deleteUser(userId);

		return new ResponseWrapper<AppUser>("Users removed.", HttpStatus.NO_CONTENT).sendResponse();
		// return new ResponseEntity<AppUser>(HttpStatus.NO_CONTENT);
	}*/

	@GetMapping("/{id}/schemes")
	ResponseEntity<?> getUserScheme(@PathVariable("id") String userId) throws ResourceNotFoundException {
		logger.info("AppUserController.getUserScheme()");

		AppUser dbuser = this.userService.getByUserID(userId);

		return new ResponseWrapper<>("Users Scheme Details", HttpStatus.OK, dbuser.getSchemes()).sendResponse();
		// return new ResponseEntity<>(dbuser.getScheme(), HttpStatus.OK);
	}

	// by contact

	// admin, customer care
	/*
	 * @PostMapping("/create") //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	 * public ResponseEntity<AppUser> createUser(@RequestBody AppUser user)
	 * throws ResourceNotFoundException {
	 * 
	 * // AppUser adminUser = userService.getUserByUserID(adminId);
	 * userService.createNewUser(user); return new
	 * ResponseWrapper<>("Users created successfully", HttpStatus.OK,
	 * user).sendResponse();
	 * 
	 * }
	 */

	// byemail

	// update user status

	@PostMapping("/buyScheme/{userId}/{schemeId}")
	public ResponseEntity<?> buyScheme(@PathVariable("schemeId") String schemeId,
			@PathVariable("userId") String userId,@RequestBody ReferralDetails rfrDtl) throws ResourceNotFoundException,
			GenericException, IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		logger.info("AppUserController.buyScheme()");

		OrderCheckout oc = serviceModule.buyScheme(userId, schemeId, rfrDtl);
		return new ResponseWrapper<>(HttpStatus.OK, oc).sendResponse();

	}

	@GetMapping("/count")
	//@PreAuthorize("hasAnyAuthority('ROLE_OPERATION','ROLE_ADMIN')")
	public ResponseEntity<?> getUserCountByRole() {
		logger.info("AppUserController.getUserCountByRole()");

		List<CategoryCount> categoryCount = userService.getUserCountByRole();
		return new ResponseWrapper<>("Ticket Counts", HttpStatus.OK, categoryCount).sendResponse();
	}
	
	/*
	 * Workshop services starts
	 * 
	 * */
	
	@GetMapping("/ws/{wsStatus}")
	ResponseEntity<List<AppUser>> getWorkShopByStatus(@PathVariable("wsStatus") String wsStatus)
			throws ResourceNotFoundException {
		logger.info("AppUserController.getWorkShopByStatus()");
		List<AppUser> users = null;
		try {
			users =  userService.getWorkShopByStatus(wsStatus);
			return new ResponseWrapper<List<AppUser>>("Users Details updated", HttpStatus.OK, users).sendResponse();
			
		} catch (GenericException e) {
			return new ResponseWrapper<List<AppUser>>("Users Details updated", HttpStatus.BAD_REQUEST, users).sendResponse();
		}
	}
	
	@PutMapping("/ws/status")
	ResponseEntity<AppUser> updateWorkShopStatus(@RequestBody AppUser user)
			throws ResourceNotFoundException {
		logger.info("AppUserController.updateWorkshopUserById()");
		try {
			user =  userService.updateWorkShopStatus(user.getUserId(),user.getWsStatus());
			return new ResponseWrapper<>("Users Details updated", HttpStatus.OK, user).sendResponse();
			
		} catch (GenericException e) {
			return new ResponseWrapper<>("Users Details updated", HttpStatus.BAD_REQUEST, user).sendResponse();
		}
	}
	@PutMapping("/ws/updateDocs/{id}")
	ResponseEntity<AppUser> updateWorkShopDocs(@PathVariable String id,@RequestBody List<AppUser.WsDoc> wsDocs)
			throws ResourceNotFoundException {
		logger.info("AppUserController.updateWorkShopDocs()");
		AppUser user = null;
		try {
			user =  userService.updateWorkShopDocs(id, wsDocs);
			return new ResponseWrapper<>("Users Details updated", HttpStatus.OK, user).sendResponse();
			
		} catch (GenericException e) {
			return new ResponseWrapper<>("Users Details updated", HttpStatus.BAD_REQUEST, user).sendResponse();
		}
	}
	@GetMapping("/nearestWorkShop/{distance}/{location}")
	ResponseEntity<?> getNearestWorkShop(@PathVariable int distance,@PathVariable String location)
			throws ResourceNotFoundException {
		List<AppUser> users  = null;
		try {
		logger.info("AppUserController.getNearestWorkShop()");
		users = userService.getNearestWorkShop(distance, location);
		logger.info("AppUserController.getAllUser()-end");
		return new ResponseWrapper<>("List of Users", HttpStatus.OK, users).sendResponse();
		}
		catch (GenericException e) {
			return new ResponseWrapper<>("Users Details updated", HttpStatus.BAD_REQUEST, users).sendResponse();
		}
	}
	@PutMapping("/ws/updateGETDetails")
	ResponseEntity<AppUser> updateGETDetails(@RequestBody AppUser appUser)
			throws ResourceNotFoundException {
		logger.info("AppUserController.updateWorkShopDocs()");
		AppUser user = null;
		try {
			user =  userService.updateGETDetails(appUser);
			return new ResponseWrapper<>("Users Details updated", HttpStatus.OK, user).sendResponse();
			
		} catch (GenericException e) {
			return new ResponseWrapper<>("Users Details updated", HttpStatus.BAD_REQUEST, user).sendResponse();
		}
	}
	/*
	 * Workshop services ends
	 * 
	 * */
}
