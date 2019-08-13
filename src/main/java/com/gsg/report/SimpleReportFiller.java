package com.gsg.report;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.springframework.stereotype.Component;

@Component
public class SimpleReportFiller {

	private String reportFileName;

	private JasperReport jasperReport;

	private JasperPrint jasperPrint;


	private Map<String, Object> parameters;

	public SimpleReportFiller() {
		parameters = new HashMap<>();
	}

	public void prepareReport() {
		compileReport();
		fillReport();
	}

	public void compileReport() {
		try {
			InputStream reportStream = getClass().getResourceAsStream("/".concat(reportFileName));
			jasperReport = JasperCompileManager.compileReport(reportStream);
			//JRSaver.saveObject(jasperReport, reportFileName.replace(".jrxml", ".jasper"));
		} catch (JRException ex) {
		}
	}

	public void fillReport() {
		try {
			jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
		} catch (JRException e) {
		}
	}


	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public String getReportFileName() {
		return reportFileName;
	}

	public void setReportFileName(String reportFileName) {
		this.reportFileName = reportFileName;
	}

	public JasperPrint getJasperPrint() {
		return jasperPrint;
	}

}
