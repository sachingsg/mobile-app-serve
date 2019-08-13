package com.gsg.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gsg.component.ResponseWrapper;
import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.AppUser;
import com.gsg.mongo.model.UserVehicle;
import com.gsg.services.AppUserService;

@RestController
@RequestMapping("/api/users")
public class UserVehicleController {

	private static final Logger logger = LoggerFactory.getLogger(UserVehicleController.class);
	@Autowired
	private AppUserService userService;

	/*@GetMapping("/vehicles")
	ResponseEntity<List<Vehicle>> getAllVehicles(@PathVariable("id") String userId) throws DataNotFoundException {
		logger.info("UserVehicleController.getAllVehicles()");
		AppUser user = userService.getUserByID(userId);
		return new ResponseEntity<List<Vehicle>>(user.getVehicles(), HttpStatus.OK);
	}*/

	@GetMapping("/{id}/vehicles")
	ResponseEntity<AppUser> getUserVehicles(@PathVariable("id") String userId) throws ResourceNotFoundException {
		logger.info("AppUserAddressController.getUserVehicles()");
		AppUser user = userService.getByUserID(userId);
		
		return new ResponseWrapper<>(HttpStatus.OK, user).sendResponse();
		//return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PostMapping("/{id}/vehicle")
	ResponseEntity<AppUser> saveUserVehicle(@RequestBody UserVehicle vehicle, @PathVariable("id") String userId)
			throws ResourceNotFoundException {
		logger.info("UserVehicleController.saveUserVehicle()");
		AppUser dbUser = userService.saveVehicle(userId, vehicle);
		return new ResponseWrapper<>(HttpStatus.OK, dbUser).sendResponse();
		//return new ResponseEntity<>(dbUser, HttpStatus.OK);
	}

	@PutMapping("/{id}/vehicle/{addPosition}")
	ResponseEntity<AppUser> updateUserVehicle(@RequestBody UserVehicle vehicle, @PathVariable("id") String userId,
			@PathVariable("addPosition") String addPosition) throws ResourceNotFoundException {

		logger.info("AppUserAddressController.updateUserVehicle()");
		AppUser user = userService.updateVehicle(userId, vehicle, Integer.valueOf(addPosition).intValue());
		return new ResponseWrapper<>("Vehicle Updated Successfully",HttpStatus.OK, user).sendResponse();
		//return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@DeleteMapping("{id}/vehicle/{addPosition}")
	//@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_CS')")
	ResponseEntity<AppUser> deleteUserVehicle(@PathVariable("id") String userId,
			@PathVariable("addPosition") String addPosition) throws ResourceNotFoundException {
		logger.info("AppUserAddressController.deleteUserVehicle()");

		AppUser user = userService.deleteVehicle(userId, Integer.valueOf(addPosition).intValue());
		return new ResponseWrapper<>(HttpStatus.OK, user).sendResponse();
		//return new ResponseEntity<>(user, HttpStatus.OK);
	}

}
