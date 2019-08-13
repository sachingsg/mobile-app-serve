package com.gsg.report;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;

public abstract class AbstractReportManager {

	Map<String, Object> reportParam = new HashMap<String, Object>();
	private JasperPrint jasperPrint;
	private String fileName;

	public Map<String, Object> getReportParam() {
		return reportParam;
	}

	public void setReportParam(Map<String, Object> reportParam) {
		this.reportParam = reportParam;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public JasperPrint getJasperPrint() {
		return jasperPrint;
	}

	public void setJasperPrint(JasperPrint jasperPrint) {
		this.jasperPrint = jasperPrint;
	}

	public abstract void reportConstructor();

	public abstract void reportMetadata();

	public abstract void generateReport();

	public abstract void fillReport();

	public abstract void compileReport();

	public void processReport() {
		compileReport();
		fillReport();
		generateReport();
	}

}
