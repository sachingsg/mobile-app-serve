package com.gsg.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class GSGCommon {

	MessageSource message;
	static Map<String, String> payResCode = new HashMap<String, String>();

	public GSGCommon(MessageSource message) {
		this.message = message;
		
		
		payResCode.put("E000", "Success");
		payResCode.put("E001", "Unauthorized Payment Mode");
		payResCode.put("E002", "Unauthorized Key");
		payResCode.put("E003", "Unauthorized Packet");
		payResCode.put("E004", "Unauthorized Merchant");
		payResCode.put("E005", "Unauthorized Return URL");
		payResCode.put("E006", "Transaction Already Paid");
		payResCode.put("E007", "Transaction Failed");
		payResCode.put("E008", "Failure from Third Party due to Technical Error or Funds Shortage");
		payResCode.put("E009", "Bill Already Expired");
		payResCode.put("E0031", "Mandatory fields coming from merchant are empty");
		payResCode.put("E0032", "Mandatory fields coming from database are empty");
		payResCode.put("E0033", "Payment mode coming from merchant is empty");
		payResCode.put("E0034", "PG Reference number coming from merchant is empty");
		payResCode.put("E0035", "Sub merchant id coming from merchant is empty");
		payResCode.put("E0036", "Transaction amount coming from merchant is empty");
		payResCode.put("E0037", "Payment mode coming from merchant is other than 0 to 9");
		payResCode.put("E0038", "Transaction amount coming from merchant is more than 9 digit length");
		payResCode.put("E0039", "Mandatory value Email in wrong format");
		payResCode.put("E00310", "Mandatory value mobile number in wrong format");
		payResCode.put("E00311", "Mandatory value amount in wrong format");
		payResCode.put("E00312", "Mandatory value Pan card in wrong format");
		payResCode.put("E00313", "Mandatory value Date in wrong format");
		payResCode.put("E00314", "Mandatory value String in wrong format");
		payResCode.put("E00315", "Optional value Email in wrong format");
		payResCode.put("E00316", "Optional value mobile number in wrong format");
		payResCode.put("E00317", "Optional value amount in wrong format");
		payResCode.put("E00318", "Optional value pan card number in wrong format");
		payResCode.put("E00319", "Optional value date in wrong format");
		payResCode.put("E00320", "Optional value string in wrong format");
		payResCode
				.put("E00321",
						"Request packet mandatory columns is not equal to mandatory columns set in enrolment or optional columns are not equal to optional columns length set in enrolment");
		payResCode.put("E00324", "Merchant Reference Number and Mandatory Columns are Null");
		payResCode.put("E00325", "Merchant Reference Number Duplicate");
		payResCode.put("E00326", "Sub merchant id coming from merchant is non numeric");
		payResCode.put("E00327", "Cash Challan Generated");
		payResCode.put("E00328", "Cheque Challan Generated");
		payResCode.put("E00329", "NEFT Challan Generated");
		payResCode.put("E00330", "Transaction Amount and Mandatory Transaction Amount mismatch in Request URL");
		payResCode.put("E00331", "UPI Transaction Initiated Please Accept or Reject the Transaction");
		payResCode.put("E00332", "Challan Already Generated, Please re-initiate with unique reference number");

	}

	public static String TYPE_SCHEME = "SCHEME";

	public static String TYPE_SERVICE = "SERVICE";

	public static String SERVICE_NORMAL = "SERVICE";

	public static String SERVICE_EMERGENCY = "EMERGENCY";

	public static String TRANSACTION_PENDING = "PENDING";

	public static String TRANSACTION_COMPLETED = "COMPLETED";
	
	public static String TRANSACTION_NOT_APPLICABLE = "NA";
	public static String TRANSACTION_FROM_SCHEME = "SCHEME";

	public static final String PAYMENT_PG = "ONLINE";

	public static final String PAYMENT_COD = "COD";

	public static final String WORK_STS_EMERGENCY = "EMERGENCY";

	public static final String WORK_STS_CREATED = "CREATED";

	public static final String WORK_STS_WIP = "WIP";

	public static final String WORK_STS_RESOLVED = "RESOLVED";

	public static final String WORK_STS_SCHEME = "SCHEME";
	public static final String WORK_STS_CLOSED = "CLOSED";
	public static final String WORK_STS_CANCELED = "CANCELED";
	public static final String WORK_STS_REJECTED = "REJECTED";
	
	public static final String WORK_QUEUE_OPERATION = "CustomerSupport";
	public static final String WORK_QUEUE_ENGINEER = "ServiceEngineer";
	
	
	public static final String[] WORK_STS = { WORK_STS_EMERGENCY, WORK_STS_CREATED, WORK_STS_WIP,
			WORK_STS_RESOLVED, WORK_STS_CLOSED,WORK_STS_CANCELED,WORK_STS_REJECTED };

	
	// Service Type
	public static final String SVC_SERVICE = "SERVICE";
	public static final String SVC_CONSUMABLE = "CONSUMABLE";
	public static final String SVC_SPARE = "SPARE";
	public static final String SVC_LABOUR = "LABOUR";
	public static final String SVC_MAJOR = "MAJOR";
	public static final String SVC_MINOR = "MINOR";
	public static final String SVC_ROADSIDE_ASSISTANCE = "ROADSIDE_ASSISTANCE";
	
	// ROles
	public static final String ADMIN = "ROLE_ADMIN";
	public static final String USER = "ROLE_USER";
	public static final String ENGINEER = "ROLE_ENGINEER";
	public static final String OPERATION = "ROLE_OPERATION";
	
	public static final Object COD_CARD = "CARD";
	public static final Object COD_CASH = "CASH";
	
	// razor pay
	public static final String RAZOR_PAY_SUCCESS = "SUCCESS";
	public static final String RAZOR_PAY_FAIL = "FAIL";

	public String getMsg(String msgKey) {
		if (StringUtils.isEmpty(msgKey))
			return StringUtils.EMPTY;

		return message.getMessage(msgKey, null, Locale.US);

	}
	
	public static String getTransactionStatus(String tranCode) {
		return payResCode.get(tranCode);

	}
	
	public static double getTwoPrecisionDoubleValue(double amount) {
		return BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP).doubleValue();

	}
	
	public static double roundedValue(double amount) {
		return BigDecimal.valueOf(amount).setScale(0, RoundingMode.HALF_UP).doubleValue();

	}
	
	public static String getTwoPrecisionStringValue(double amount) {
		NumberFormat nf= NumberFormat.getInstance(new Locale("en", "in"));
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		nf.setRoundingMode(RoundingMode.HALF_UP);
		return nf.format(amount);

	}
	
	public static double getRetailPrice(double price, double gst) {
		return getTwoPrecisionDoubleValue((price/(100+gst))*100);

	}
	public static double getTaxPrice(double price, double gst) {
		return getTwoPrecisionDoubleValue(price * (1 + (gst / 100)));

	}
	
}
