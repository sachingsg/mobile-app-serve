package com.gsg.report;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ReportUtility {
	Logger logger = LoggerFactory.getLogger(ReportUtility.class);

	protected JasperPrint jasperPrint;
	protected JasperReport jasperReport;
	protected JasperReport serviceSubReport;
	protected Map<String, Object> reportParam = new HashMap<String, Object>();
	
	protected  JRBeanCollectionDataSource dataSource;
	protected String fileName;
	
	
	public abstract void reportConstructor(Object... inputData) throws Exception;

	public abstract void reportMetadata() throws JRException, IOException;


	public abstract void fillReport() throws JRException;


	public void prepareReport(Object... inputData) throws Exception {
		reportConstructor(inputData);
		reportMetadata();
		fillReport();
	}

		// TODO Auto-generated method stub
	public void generateReport(String reportName) throws JRException {
		
	}

	public byte[] generateReportByte(String orderId) throws JRException {
		// TODO Auto-generated method stub
		return null;
	}


}
