package com.gsg.services.impl;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsg.services.LoginService;
import com.gsg.utilities.OtpResponse;
import com.gsg.utilities.SMSUtility;

@Service
public class LoginServiceImpl implements LoginService {
	private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
	
	@Autowired
	private SMSUtility smsUtility;

	@Override
	public OtpResponse generateOtp(String contactNo) {
		logger.info("LoginServiceImpl.generateOtp()");
		OtpResponse otpResponse = null;
		try {
			otpResponse = smsUtility.sendOtp(contactNo);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return otpResponse;
	}

	@Override
	public OtpResponse verifyUserOtp(String contactNo, String otp) {
		logger.info("LoginServiceImpl.verifyUserOtp()");
		OtpResponse otpResponse = smsUtility.verifyOtp(contactNo, otp);
		return otpResponse;

	}

	@Override
	public OtpResponse resendOtp(String contactNo) {
		logger.info("LoginServiceImpl.resendOtp()");
		OtpResponse otpResponse = smsUtility.resendOtp(contactNo);
		return otpResponse;
	}

}
