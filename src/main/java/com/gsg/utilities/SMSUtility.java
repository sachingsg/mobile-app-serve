package com.gsg.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@PropertySource("classpath:sms.properties")
public class SMSUtility {

	@Value("${sms.authKey}")
	private String authKey;

	@Value("${sms.otp.senderId}")
	private String otpSenderId;
	

	@Value("${sms.msg.senderId}")
	private String msgSenderId;
	
	static Logger logger = LoggerFactory.getLogger(SMSUtility.class);

	public String generateRandom() {
		Random rand = new Random();
		String randomCode = String.format("%04d%n", rand.nextInt(10000)).trim();
		return randomCode;
	}

	public OtpResponse sendOtp(String mobileNo) throws UnsupportedEncodingException {
		logger.info("SMSUtility.sendOtp()");
		ObjectMapper mapper = new ObjectMapper();
		OtpResponse response = new OtpResponse();

		String randomOtp = generateRandom();

		String message = "Your One Time Password for registering on GSG is " + randomOtp + ". "
				+ "OTP expires in 10 minutes.";

		int expiry_time = 10;

		// Prepare Url
		URLConnection myURLConnection = null;
		URL myURL = null;
		BufferedReader reader = null;

		// encoding message
		// @SuppressWarnings("deprecation")
		String encoded_message = URLEncoder.encode(message, "UTF-8");

		// Send SMS API
		String mainUrl = "http://api.msg91.com/api/sendotp.php?";

		// Prepare parameter string
		StringBuilder sbPostData = new StringBuilder(mainUrl);
		sbPostData.append("authkey=" + authKey);
		sbPostData.append("&mobile=" + mobileNo);
		sbPostData.append("&message=" + encoded_message);
		sbPostData.append("&sender=" + otpSenderId);
		sbPostData.append("&otp=" + randomOtp);
		sbPostData.append("&otp_expiry=" + expiry_time);

		// final string
		mainUrl = sbPostData.toString();

		try {
			// prepare connection
			myURL = new URL(mainUrl);
			myURLConnection = myURL.openConnection();
			myURLConnection.connect();
			reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
			// reading response

			String responseStr, jsonVal = "";
			while ((responseStr = reader.readLine()) != null)
				jsonVal += responseStr; // Concat the response string with the
										// jsonVal object

			reader.close();

			// return jsonVal;

			response = mapper.readValue(jsonVal, OtpResponse.class);
			return response;
		} catch (IOException e) {
			e.printStackTrace();
			response.setMessage(null);
			response.setType("failed");
			return response;
			// return "{\"type\" : \"Failed\"}";
		} finally {
			mapper = null;
			response = null;
			reader = null;
			myURL = null;
			myURLConnection = null;
		}

		
		/*String randomOtp = generateRandom();
		int expiry_time = 5;
		OtpResponse response = new OtpResponse();
		String url = "http://control.msg91.com/api/sendotp.php?";

		String message = "Your One Time Password for registering on GSG is " + randomOtp + ". "
				+ "OTP will expire in "+expiry_time+" minutes.";
		String encoded_message = URLEncoder.encode(message, "UTF-8");
		
		// Prepare parameter string
				StringBuilder sbPostData = new StringBuilder(url);
				sbPostData.append("authkey=" + authKey);
				sbPostData.append("&mobile=" + mobileNo);
				sbPostData.append("&message=" + encoded_message);
				sbPostData.append("&sender=" + senderId);
				sbPostData.append("&otp=" + randomOtp);
				sbPostData.append("&otp_expiry=" + expiry_time);
		

		HttpEntity<String> requestEntity = new HttpEntity<>(sbPostData.toString());

		RestTemplate restTemplate = new RestTemplate();

		response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, OtpResponse.class).getBody();
		logger.info("response>>" + response);
		
		return response;*/
		
		
	}

	public OtpResponse verifyOtp(String mobileNo, String otp) {
		
		logger.info("SMSUtility.verifyOtp()");

		ObjectMapper mapper = new ObjectMapper();
		OtpResponse response = new OtpResponse();

		URLConnection myURLConnection = null;
		URL myURL = null;
		BufferedReader reader = null;

		String mainUrl = "http://api.msg91.com/api/verifyRequestOTP.php?";

		StringBuilder sbPostData = new StringBuilder(mainUrl);
		sbPostData.append("authkey=" + authKey);
		sbPostData.append("&mobile=" + mobileNo);
		sbPostData.append("&otp=" + otp);

		// final string
		mainUrl = sbPostData.toString();

		try {
			// prepare connection
			myURL = new URL(mainUrl);
			myURLConnection = myURL.openConnection();
			myURLConnection.connect();
			reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
			// reading response
			String responseStr, jsonVal = "";
			while ((responseStr = reader.readLine()) != null)
				jsonVal += responseStr;

			reader.close();
			response = mapper.readValue(jsonVal, OtpResponse.class);
			return response;
		} catch (IOException e) {
			e.printStackTrace();
			response.setMessage(null);
			response.setType("failed");
			return response;
			// return "{\"type\" : \"Failed\"}";
		} finally {
			mapper = null;
			response = null;
			reader = null;
			myURL = null;
			myURLConnection = null;
		}
	}

	public OtpResponse resendOtp(String mobileNo) {

		logger.info("SMSUtility.resendOtp()");
		
		ObjectMapper mapper = new ObjectMapper();
		OtpResponse response = new OtpResponse();

		String retryType = "text";

		URLConnection myURLConnection = null;
		URL myURL = null;
		BufferedReader reader = null;

		String mainUrl = "http://api.msg91.com/api/retryotp.php?";

		StringBuilder sbPostData = new StringBuilder(mainUrl);
		sbPostData.append("authkey=" + authKey);
		sbPostData.append("&mobile=" + mobileNo);
		sbPostData.append("&retrytype=" + retryType);

		// final string
		mainUrl = sbPostData.toString();

		try {
			// prepare connection
			myURL = new URL(mainUrl);
			myURLConnection = myURL.openConnection();
			myURLConnection.connect();
			reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
			// reading response
			String responseStr, jsonVal = "";
			while ((responseStr = reader.readLine()) != null)
				jsonVal += responseStr;

			response = mapper.readValue(jsonVal, OtpResponse.class);
			return response;
		} catch (IOException e) {
			e.printStackTrace();
			response.setMessage(null);
			response.setType("failed");
			return response;
			// return "{\"type\" : \"Failed\"}";
		} finally {
			mapper = null;
			response = null;
			reader = null;
			myURL = null;
			myURLConnection = null;
		}
	}
	
	@Async
	public void testFn() throws InterruptedException {
		Thread.sleep(5000);
		throw new IllegalStateException();


	}

	@Async
	public void sendStatusMessageToUser(String message, String... number) {

		logger.info("SMSUtility.sendStatusMessageToUser()");
		logger.info("Sending message to " + number);
		
		ShortMessage msg = new ShortMessage();
		msg.setSender(msgSenderId);
		msg.getSms().add(new SMS(message, number));

		String url = "http://api.msg91.com/api/v2/sendsms";

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		requestHeaders.setContentType(new MediaType("application", "json"));
		requestHeaders.add("authkey", authKey);

		HttpEntity<ShortMessage> requestEntity = new HttpEntity<>(msg, requestHeaders);

		RestTemplate restTemplate = new RestTemplate();

		String response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class).getBody();
		logger.info("response>>" + response);

	}

	

	public static class ShortMessage {
		private String sender;
		private String route = "4";
		private String country = "91";
		private List<SMS> sms = new ArrayList<SMS>();

		public ShortMessage() {
		}

		public String getSender() {
			return sender;
		}

		public void setSender(String sender) {
			this.sender = sender;
		}

		public String getRoute() {
			return route;
		}

		public void setRoute(String route) {
			this.route = route;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public List<SMS> getSms() {
			return sms;
		}

		public void setSms(List<SMS> sms) {
			this.sms = sms;
		}

	}

	public static class SMS {
		private String message;
		private List<String> to = new ArrayList<String>();

		public SMS(String message, String... numbers) {
			super();
			this.message = message;
			this.to = Arrays.asList(numbers);
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public List<String> getTo() {
			return to;
		}

		public void setTo(List<String> to) {
			this.to = to;
		}

	}
}
