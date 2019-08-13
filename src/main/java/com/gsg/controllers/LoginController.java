package com.gsg.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gsg.component.ResponseWrapper;
import com.gsg.constants.AppUserConst;
import com.gsg.error.GenericException;
import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.AppUser;
import com.gsg.mongo.model.LoginBean;
import com.gsg.mongo.model.WorkShopBean;
import com.gsg.services.AppUserService;
import com.gsg.services.LoginService;
import com.gsg.utilities.OtpResponse;

@RestController
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private LoginService loginService;

	@Autowired
	private AppUserService userService;

//	@GetMapping("/static")
//	public ModelAndView getSamplPage() {
//		return new ModelAndView("abc.html");
//
//	}
//
//	@GetMapping("/sredirect")
//	public RedirectView getRedirectPage() {
//		return new RedirectView("abc.html");
//
//	}
//
//	@GetMapping("/redirect")
//	public RedirectView redirectWithRedirectAttributes(RedirectAttributes attributes) {
//		attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectAttributes");
//		attributes.addAttribute("q", "google");
//		return new RedirectView("https://www.google.co.in/search");
//	}

	
	
	
	@GetMapping("/resendOtp/{contactNo}")
	ResponseEntity<OtpResponse> resendOTP(@PathVariable("contactNo") String contactNo) {
		logger.info("LoginController.resendOTP()");

		OtpResponse otpRes = this.loginService.resendOtp(contactNo);
		return new ResponseWrapper<>(HttpStatus.OK, otpRes).sendResponse();
		// return new ResponseEntity<OtpResponse>(otpRes, HttpStatus.OK);
	}

	@GetMapping("/validateContactNbr/{contactNbr}")
	ResponseEntity validateContactNbr(@PathVariable String contactNbr) throws GenericException {
		// mobile,email
		logger.info("LoginController.validateContactNbr()");

		try {
			AppUser user = userService.getUserByContactNbr(contactNbr);
			if (user != null)
				throw new GenericException("Mobile nbr is already registered.");
			// loginDtl.setMsg("Mobile nbr is already registered.");
			// loginDtl.setStatus("FAILURE");

			return new ResponseWrapper<>("Mobile No already exists", HttpStatus.CONFLICT).sendResponse();
			// return new ResponseEntity<>(loginDtl, HttpStatus.CONFLICT);

		} catch (ResourceNotFoundException dex) {
			// user not present - success
			return new ResponseWrapper<>("Mobile No is valid", HttpStatus.OK).sendResponse();
		}
	}
	
	@PostMapping("/preregister")
	ResponseEntity<LoginBean> preRegister(@RequestBody LoginBean loginDtl) throws GenericException {
		// mobile,email
		logger.info("LoginController.proceedForSignup()");

		String contactNbr = loginDtl.getContactNbr();
		try {
			AppUser user = userService.getUserByContactNbr(contactNbr);
			if (user != null)
				throw new GenericException("Mobile nbr is already registered.");
			// loginDtl.setMsg("Mobile nbr is already registered.");
			// loginDtl.setStatus("FAILURE");

			return new ResponseWrapper<>(loginDtl.getMsg(), HttpStatus.CONFLICT, loginDtl).sendResponse();
			// return new ResponseEntity<>(loginDtl, HttpStatus.CONFLICT);

		} catch (ResourceNotFoundException dex) {
			// user not present - success
			OtpResponse otpRes = loginService.generateOtp(contactNbr);

			loginDtl.setStatus(otpRes.getType());
			if (otpRes.getType().toUpperCase().contains("SUCCESS")) {
				loginDtl.setMsg("OTP has been sent to XXXXX" + contactNbr.substring(5));

				return new ResponseWrapper<>(loginDtl.getMsg(), HttpStatus.OK, loginDtl).sendResponse();
				// return new ResponseEntity<>(loginDtl, HttpStatus.OK);
			} else {
				loginDtl.setMsg("OTP Request failed.");

				return new ResponseWrapper<>(loginDtl.getMsg(), HttpStatus.REQUEST_TIMEOUT, loginDtl).sendResponse();
				// return new ResponseEntity<>(loginDtl,
				// HttpStatus.REQUEST_TIMEOUT);
			}
		}

	}

	@PostMapping("/register")
	ResponseEntity<LoginBean> register(@RequestBody LoginBean loginDtl) throws ResourceNotFoundException, GenericException {
		logger.info("LoginController.register()");

		OtpResponse otpRes = loginService.verifyUserOtp(loginDtl.getContactNbr(), loginDtl.getOtp());
		loginDtl.setStatus(otpRes.getType());
		if (otpRes.getType().toUpperCase().contains("SUCCESS")) {
			AppUser user = new AppUser(loginDtl.getEmail(), loginDtl.getContactNbr(), loginDtl.getPassword());
			userService.registerUser(user);
			loginDtl.setMsg("User Created");

			return new ResponseWrapper<>(loginDtl.getMsg(), HttpStatus.CREATED, loginDtl).sendResponse();
			// return new ResponseEntity<>(loginDtl, HttpStatus.CREATED);
		} else {
			loginDtl.setMsg("Invalid OTP");

			return new ResponseWrapper<>(loginDtl.getMsg(), HttpStatus.NOT_ACCEPTABLE, loginDtl).sendResponse();
			// return new ResponseEntity<>(loginDtl, HttpStatus.NOT_ACCEPTABLE);
		}

	}
	@PostMapping("/ws/register")
	ResponseEntity<AppUser> registerWorkShop(@RequestBody  AppUser reqUser) throws  GenericException {
		try {
			logger.info("LoginController.registerWorkShop()");
			String contactNbr = reqUser.getContactNbr();
			AppUser user = userService.getUserByContactNbr(contactNbr);

			if (user != null) {
				throw new GenericException("Mobile nbr is already registered.");
			}
		}
		catch(ResourceNotFoundException ex) {
			// user not registered add new user
			createWorkShopUser(reqUser);
			userService.registerUser(reqUser);
			return new ResponseWrapper<>("WorkShop user created", HttpStatus.CREATED, reqUser).sendResponse();
			
		}
		catch(GenericException ex) {
			return new ResponseWrapper<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, reqUser).sendResponse();
		}
		return null;
	}
	/*
	 * Used to create workshop specific object from AppUser object with setter 
	 * */
	public AppUser createWorkShopUser(AppUser appUser) throws GenericException{
		// validation for server end fields
		if(appUser.getEmail() == null)
			throw new GenericException("Invalid Email");
		if(appUser.getContactNbr() == null)
			throw new GenericException("Invalid ContactNbr");
		if(appUser.getWsCreatedBy() == null)
			throw new GenericException("Invalid Get id");
//		if(this.getPassword() == null)
//			throw new GenericException("Invalid Password");
		List<String> roles = new ArrayList<>();
		roles.add("ROLE_WORK_SHOP");
		appUser.setRoles(roles);
		appUser.setWsStatus(AppUserConst.STATUS_PENDING); // default status is pending
		return appUser;
	}
	
	@PostMapping("/preresetpwd/{contactNo}")
	ResponseEntity<?> preResetPassword(@RequestBody LoginBean loginDtl) {
		logger.info("LoginController.preResetPassword()");

		String contactNbr = loginDtl.getContactNbr();
		try {
			userService.getUserByContactNbr(contactNbr);
			OtpResponse otpRes = loginService.generateOtp(contactNbr);

			loginDtl.setStatus(otpRes.getType());
			if (otpRes.getType().toUpperCase().contains("SUCCESS")) {
				loginDtl.setMsg("OTP has been sent to " + contactNbr);
				return new ResponseWrapper<>(loginDtl.getMsg(), HttpStatus.OK, loginDtl).sendResponse();
				// return new ResponseEntity<>(loginDtl, HttpStatus.OK);
			} else {
				loginDtl.setMsg("OTP Request failed.");

				return new ResponseWrapper<>(loginDtl.getMsg(), HttpStatus.REQUEST_TIMEOUT, loginDtl).sendResponse();
				// return new ResponseEntity<>(loginDtl,
				// HttpStatus.REQUEST_TIMEOUT);
			}

		} catch (ResourceNotFoundException dex) {
			// user not present - success
			loginDtl.setMsg("Mobile Nbr not present.");
			loginDtl.setStatus("FAILURE");
			return new ResponseWrapper<>(loginDtl.getMsg(), HttpStatus.NOT_FOUND, loginDtl).sendResponse();
			// return new ResponseEntity<>(loginDtl, HttpStatus.NOT_FOUND);
		}

	}

	@PostMapping("/resetpwd")
	ResponseEntity<?> resetPassword(@RequestBody LoginBean loginDtl) throws ResourceNotFoundException {
		logger.info("LoginController.resetPassword()");

		OtpResponse otpRes = loginService.verifyUserOtp(loginDtl.getContactNbr(), loginDtl.getOtp());
		loginDtl.setStatus(otpRes.getType());
		if (otpRes.getType().toUpperCase().contains("SUCCESS")) {
			// if (loginDtl.getPassword().equals(loginDtl.getConfirmPassword()))
			// {
			userService.resetPassword(loginDtl.getContactNbr(), loginDtl.getPassword());
			loginDtl.setMsg("Password Reset");
			return new ResponseWrapper<>(loginDtl.getMsg(), HttpStatus.OK, loginDtl).sendResponse();
			// return new ResponseEntity<>(loginDtl, HttpStatus.OK);
			/*
			 * } else {
			 * loginDtl.setMsg("password/confirm password not matched."); return
			 * new ResponseWrapper<>(loginDtl.getMsg(),
			 * HttpStatus.NOT_ACCEPTABLE, loginDtl).sendResponse(); // return
			 * new ResponseEntity<>(loginDtl, // HttpStatus.NOT_ACCEPTABLE); }
			 */
		} else {
			loginDtl.setMsg("Invalid OTP");
			return new ResponseWrapper<>(loginDtl.getMsg(), HttpStatus.NOT_ACCEPTABLE, loginDtl).sendResponse();
			// return new ResponseEntity<>(loginDtl, HttpStatus.NOT_ACCEPTABLE);
		}

	}

}
