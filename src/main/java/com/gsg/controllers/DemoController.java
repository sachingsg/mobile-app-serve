package com.gsg.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gsg.mail.impl.GMailMailSender;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@RestController
@RequestMapping("/api/demo")
public class DemoController {
	
	@Autowired
	private GMailMailSender gMailMailSender;
	
	private static final Logger logger = LoggerFactory.getLogger(DemoController.class);
	
	@GetMapping("/sendSampleMail")
	public void sendSampleMail() {
		try {
			gMailMailSender.sendMessage("rajendrasahoodbpb@gmail.com", "test@gsg.com", "Test message from GSG", "this is a test message");
			logger.info("sent message");
			
		}
		catch (Exception e) {
			logger.error("error in sending mail "+e.getMessage());
		}
	}
	@GetMapping("/report1")
	public void report1() throws Exception{
		try {
			class Employee{
				private String name;
				private String city;
				
				
				public String getCity() {
					return city;
				}
				public void setCity(String city) {
					this.city = city;
				}
				public String getName() {
					return name;
				}
				public void setName(String name) {
					this.name = name;
				}
				public Employee() {
					super();
				}
				
			}
			List<Object> listDataSource = new ArrayList<Object>();
			int i = 0;
			List<Employee> employees = new ArrayList<Employee>();
			while(i++<3) {
				Employee emp = new Employee();
				emp.setCity("city - "+i);
				emp.setName("name - "+i);
				employees.add(emp);
			}
			listDataSource.add(employees);
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);

			JasperReport jasperReport = JasperCompileManager.compileReport("C:\\Users\\Rajendra\\Desktop\\gsg\\report\\report1.jrxml");
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
			JasperExportManager.exportReportToPdfFile(jasperPrint, "C:\\Users\\Rajendra\\Desktop\\gsg\\report\\employees.pdf");
			
		}
		catch (Exception e) {
			logger.error("error in sending mail "+e.getMessage());
			throw e;
		}
	}

}
