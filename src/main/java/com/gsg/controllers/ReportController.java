package com.gsg.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gsg.component.ResponseWrapper;
import com.gsg.services.ReportService;

@RestController
@RequestMapping("/api/report")
public class ReportController {
	@Autowired
	ReportService reportService;

	@GetMapping("/{orderId}")
	ResponseEntity<?> getInvoiceFile(@PathVariable("orderId") String orderId) throws Exception {

		reportService.sendInvoiceReport(orderId);
		
		return new ResponseWrapper<>("Report Generated", HttpStatus.OK).sendResponse();

	}

}
