package com.gsg.services.impl;

import java.time.Duration;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.gsg.mail.MailSender;
import com.gsg.mongo.model.AppUser;
import com.gsg.mongo.model.Order;
import com.gsg.report.ReportUtility;
import com.gsg.services.AppUserService;
import com.gsg.services.OrderService;
import com.gsg.services.ReportService;

@Service
public class ReportServiceImpl implements ReportService {
	Logger logger = LoggerFactory.getLogger(ReportService.class);
	@Autowired
	ReportUtility invoiceUtility;

	@Autowired
	MailSender mailSender;
	
	@Autowired
	AppUserService userService;
	
	@Autowired
	OrderService orderService;

	@Async
	@Override
	public void sendInvoiceReport(String orderId) throws Exception  {
		logger.info("ReportServiceImpl.sendInvoiceReport()");

		LocalDateTime start = LocalDateTime.now();
		logger.debug("orderService.getByOrderId orderId "+orderId );
		Order order = orderService.getByOrderId(orderId);
		
		/*if(!order.getRequestStatus().equals(GSGCommon.WORK_STS_RESOLVED)||
				!order.getRequestStatus().equals(GSGCommon.WORK_STS_CLOSED)){
			throw new GenericException("Report can be downloaded upon completion only");
		}*/
		logger.debug("userService.getByUserID "+userService.getByUserID(order.getUserId()) );
		AppUser user = userService.getByUserID(order.getUserId());
		invoiceUtility.prepareReport(user, order);
		
		byte dataByte[] = invoiceUtility.generateReportByte(orderId);
		LocalDateTime end = LocalDateTime.now();
		long diff = Duration.between(start, end).toMillis();

		logger.info("Time taken for report generation >> " + diff);

		
		String sub = "Invoice for Request :: " + orderId;
		String body = "Please find attached invoice for your Request " + orderId;
		
		
		
		mailSender.sendMessageWithAttachment(user.getEmail(), "gsg@gmail.com", sub, body, dataByte, orderId + ".pdf");

	}
}
