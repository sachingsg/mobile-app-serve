package com.gsg.services;

import com.gsg.utilities.OtpResponse;

public interface LoginService {

	OtpResponse generateOtp(String contactNo);

	OtpResponse verifyUserOtp(String contactNo, String otp);

	OtpResponse resendOtp(String contactNo);


}