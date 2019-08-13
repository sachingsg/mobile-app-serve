package com.gsg.report;

import java.io.File;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import org.springframework.stereotype.Component;

@Component
public class SimpleReportExporter {

	private JasperPrint jasperPrint;

	public SimpleReportExporter() {
	}

	public SimpleReportExporter(JasperPrint jasperPrint) {
		this.jasperPrint = jasperPrint;
	}

	public JasperPrint getJasperPrint() {
		return jasperPrint;
	}

	public void setJasperPrint(JasperPrint jasperPrint) {
		this.jasperPrint = jasperPrint;
	}

	public void exportToPdf(String fileName, String author) {

		// print report to file
		JRPdfExporter exporter = new JRPdfExporter();

		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new File("/root/" + fileName
				+ System.currentTimeMillis() + ".pdf")));

		/*
		 * SimplePdfReportConfiguration reportConfig = new
		 * SimplePdfReportConfiguration();
		 * reportConfig.setSizePageToContent(true);
		 * reportConfig.setForceLineBreakPolicy(false);
		 * 
		 * SimplePdfExporterConfiguration exportConfig = new
		 * SimplePdfExporterConfiguration();
		 * exportConfig.setMetadataAuthor(author);
		 * exportConfig.setEncrypted(true);
		 * exportConfig.setAllowedPermissionsHint("PRINTING");
		 * 
		 * exporter.setConfiguration(reportConfig);
		 * exporter.setConfiguration(exportConfig);
		 */try {
			exporter.exportReport();
		} catch (JRException ex) {
		}
	}

	public void exportToXlsx(String fileName, String sheetName) {
		JRXlsxExporter exporter = new JRXlsxExporter();

		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fileName));

		SimpleXlsxReportConfiguration reportConfig = new SimpleXlsxReportConfiguration();
		reportConfig.setSheetNames(new String[] { sheetName });

		exporter.setConfiguration(reportConfig);

		try {
			exporter.exportReport();
		} catch (JRException ex) {
		}
	}

}
