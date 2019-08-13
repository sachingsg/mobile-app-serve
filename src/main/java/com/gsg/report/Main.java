package com.gsg.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class Main {

	public static void main(String[] args) {

		SimpleReportFiller simpleReportFiller = new SimpleReportFiller();
		simpleReportFiller.setReportFileName("report/abc.jrxml");
		simpleReportFiller.compileReport();

		Dept dept = new Dept();
		dept.setId("1");
		Emp e1 = new Emp("sarbe");
		Emp e2 = new Emp("suji");
		
		List<Emp> empList = new ArrayList<Main.Emp>();
		empList.add(e1);
		empList.add(e2);
		dept.setEmpList(empList);

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("title", "Employee Report Example");

		List data = new ArrayList<>();
		data.add(dept);

		JRBeanCollectionDataSource jd = new JRBeanCollectionDataSource(data);
		parameters.put("data", jd);

		simpleReportFiller.setParameters(parameters);
		simpleReportFiller.fillReport();

		SimpleReportExporter simpleExporter = new SimpleReportExporter();
		simpleExporter.setJasperPrint(simpleReportFiller.getJasperPrint());

		simpleExporter.exportToPdf("abc", "baeldung");

	}

	public static class Dept {
		private String id;
		private Emp me = new Emp("ANashish");
		private List<Emp> empList = new ArrayList<Emp>();

		
		public Emp getMe() {
			return me;
		}

		public void setMe(Emp me) {
			this.me = me;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public List<Emp> getEmpList() {
			return empList;
		}

		public void setEmpList(List<Emp> empList) {
			this.empList = empList;
		}

	}

	public static class Emp {
		private String name;

		public Emp(String name) {
			super();
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}
