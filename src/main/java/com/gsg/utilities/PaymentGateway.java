package com.gsg.utilities;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gsg.utilities.SMSUtility.ShortMessage;

@PropertySource("classpath:gateway.properties")
@Component
public class PaymentGateway {

	static Logger logger = LoggerFactory.getLogger(PaymentGateway.class);

	private static String pgBaseUrl = "https://eazypay.icicibank.com";
	private static String AES_KEY = "1602841122905007";
	private static String merchantId = "162297";
	private static String callBackURL = "/paymentResponse";
	private static String paymentMode = "9";// All

	@Value("${server.url}")
	private String serverUrl;
	
	public String getCodUrl() {
		return serverUrl+"/codPayemnt";
	}
	
	private String encrypt(String input) {
		String key = AES_KEY;
		String encryptedStr = "";
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] encrypted = cipher.doFinal(input.getBytes("UTF-8"));
			encryptedStr = Base64.getEncoder().encodeToString(encrypted);

			// logger.info("encrypted string: " + encryptedStr);
			return encryptedStr;

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return encryptedStr;
	}

	public String formEncryptedURL(String pgRefNbr, String amount,String userId, String mobileNbr) {
		// mandatory fields PGReferenceNo|submerchentid|amount|MobileNo

		amount = "1";

		String subMerchantId = userId;
		String mandatoryField = pgRefNbr + "|" + subMerchantId + "|" + amount + "|" + mobileNbr;
		logger.info("mandatoryField>>" + mandatoryField);

		String optionalFields = "";

		StringBuilder pgUrl = new StringBuilder(pgBaseUrl+"/EazyPG?");
		// removed encryption this will be taken care by client end plugin razor pay
		/*pgUrl.append("merchantid=" + merchantId).append("&mandatory fields=" + encrypt(mandatoryField))
				.append("&optional fields=" + optionalFields).append("&returnurl=" + encrypt(serverUrl+callBackURL))
				.append("&Reference No=" + encrypt(pgRefNbr)).append("&submerchantid=" + encrypt(subMerchantId))
				.append("&transaction amount=" + encrypt(amount)).append("&paymode=" + encrypt(paymentMode));*/
		pgUrl.append("merchantid=" + merchantId).append("&mandatory fields=" + mandatoryField)
		.append("&optional fields=" + optionalFields).append("&returnurl=" + serverUrl+callBackURL)
		.append("&Reference No=" + encrypt(pgRefNbr)).append("&submerchantid=" + subMerchantId)
		.append("&transaction amount=" + encrypt(amount)).append("&paymode=" + paymentMode);

		logger.info("pgurl >>" + pgUrl.toString());
		return pgUrl.toString();
	}
	
	public String checkStatusOfAPayment(String pgRefNbr) {
		System.out.println("PaymentGateway.checkStatusOfAPayment()");
		
		StringBuilder verifyUrl = new StringBuilder(pgBaseUrl + "/EazyPGVerify?");
		verifyUrl.append("ezpaytranid=&amount=&paymentmode=&trandate=")
			.append("&merchantid=" + merchantId)
			.append("&pgreferenceno=" + pgRefNbr );
			
			logger.info("Verify URL >>" +verifyUrl.toString());
			
			HttpHeaders requestHeaders = new HttpHeaders();
			//requestHeaders.setAccept(Arrays.asList(MediaType.TEXT_PLAIN));
			requestHeaders.setContentType(MediaType.TEXT_PLAIN);

			HttpEntity<ShortMessage> requestEntity = new HttpEntity<>(requestHeaders);

			RestTemplate restTemplate = new RestTemplate();

			String response = restTemplate.exchange(verifyUrl.toString(), HttpMethod.GET, requestEntity, String.class).getBody();
			logger.info("response>>" + response);
			
			String sts="";
			
			Map<String, String> paramMap = new HashMap<String, String>();
			if(StringUtils.isNotEmpty(response)){
				String[] params = response.split("&");
				for(String param:params){
					logger.info(param);
					String[] paramParts = param.split("=");
					if(paramParts.length>1){
						paramMap.put(paramParts[0],paramParts[1]);
					}else{
						paramMap.put(paramParts[0],"");
					}
					
				}
			}
			sts = paramMap.get("status");
			System.out.println("PaymentGateway.checkStatusOfAPayment()");
			
			return StringUtils.trimToEmpty(sts);
	
	}
	
	public static void main(String[] args) {
		//checkStatusOfAPayment("5a5b9ea0bd439762f80b0ad1");
	}


	@Document(collection = "transactions")
	@TypeAlias("PGResponse")
	public static class PGResponse {

		// Response Code :: E00329
		// Unique Ref Number :: ICL8011217699010
		// Service Tax Amount :: 0.92
		// Processing Fee Amount :: 5.08
		// Total Amount :: 8.00
		// Transaction Amount :: 2
		// Transaction Date :: 12-01-2018 01:39:11
		// Interchange Value ::
		// TDR ::
		// Payment Mode :: NEFT_RTGS
		// SubMerchantId :: 1111
		// ReferenceNo :: 5a57a17c3aa1b03c80af76c5
		// ID :: 162297
		// RS ::
		// df9758f63ab1fb00a024e6fae13a460119e5d4e5263ada8169367b37bbe3cbbd325c87bcf1df6ef888ae1526b09fc62e2592a0112563a4dbe58b70c73fa4bebc
		// TPS :: Y

		@Id
		private String uniquerefnumber;
		@Field("merchantid")
		private String id;
		private String responsecode;
		private String response;
		private String servicetaxamount;
		private String processingfeeamount;
		private String totalamount;
		private String transactionamount;
		private String transactiondate;
		private String interchangevalue;
		private String tdr;
		private String paymentmode;
		private String submerchantid;
		private String referenceno;
		private String tps;
		private String rs;
		private String aes_key;
		// razor pay
		private String razorPaymentId;
		private String razorPayStatus;

		public PGResponse() {}

		
		///////////////////
		public String getResponse() {
			return response;
		}

		public void setResponse(String response) {
			this.response = response;
		}


		public String getUniquerefnumber() {
			return uniquerefnumber;
		}

		public void setUniquerefnumber(String uniquerefnumber) {
			this.uniquerefnumber = uniquerefnumber;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getResponsecode() {
			return responsecode;
		}

		public void setResponsecode(String responsecode) {
			this.responsecode = responsecode;
		}

		public String getServicetaxamount() {
			return servicetaxamount;
		}

		public void setServicetaxamount(String servicetaxamount) {
			this.servicetaxamount = servicetaxamount;
		}

		public String getProcessingfeeamount() {
			return processingfeeamount;
		}

		public void setProcessingfeeamount(String processingfeeamount) {
			this.processingfeeamount = processingfeeamount;
		}

		public String getTotalamount() {
			return totalamount;
		}

		public void setTotalamount(String totalamount) {
			this.totalamount = totalamount;
		}

		public String getTransactionamount() {
			return transactionamount;
		}

		public void setTransactionamount(String transactionamount) {
			this.transactionamount = transactionamount;
		}

		public String getTransactiondate() {
			return transactiondate;
		}

		public void setTransactiondate(String transactiondate) {
			this.transactiondate = transactiondate;
		}

		public String getInterchangevalue() {
			return interchangevalue;
		}

		public void setInterchangevalue(String interchangevalue) {
			this.interchangevalue = interchangevalue;
		}

		public String getTdr() {
			return tdr;
		}

		public void setTdr(String tdr) {
			this.tdr = tdr;
		}

		public String getPaymentmode() {
			return paymentmode;
		}

		public void setPaymentmode(String paymentmode) {
			this.paymentmode = paymentmode;
		}

		public String getSubmerchantid() {
			return submerchantid;
		}

		public void setSubmerchantid(String submerchantid) {
			this.submerchantid = submerchantid;
		}

		public String getReferenceno() {
			return referenceno;
		}

		public void setReferenceno(String referenceno) {
			this.referenceno = referenceno;
		}

		public String getTps() {
			return tps;
		}

		public void setTps(String tps) {
			this.tps = tps;
		}

		public String getRs() {
			return rs;
		}

		@JsonIgnore
		public void setRs(String rs) {
			this.rs = rs;
		}

		public String getAes_key() {
			return aes_key;
		}

		public void setAes_key(String aes_key) {
			this.aes_key = aes_key;
		}


		public String getRazorPaymentId() {
			return razorPaymentId;
		}


		public void setRazorPaymentId(String razorPaymentId) {
			this.razorPaymentId = razorPaymentId;
		}


		public String getRazorPayStatus() {
			return razorPayStatus;
		}


		public void setRazorPayStatus(String razorPayStatus) {
			this.razorPayStatus = razorPayStatus;
		}

	}

}
