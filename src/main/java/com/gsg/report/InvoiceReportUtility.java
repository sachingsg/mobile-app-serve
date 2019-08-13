package com.gsg.report;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.gsg.mongo.model.AppUser;
import com.gsg.mongo.model.AppUser.AddressBook;
import com.gsg.mongo.model.OfficeDetails;
import com.gsg.mongo.model.Order;
import com.gsg.mongo.model.OrderDetails;
import com.gsg.mongo.model.ServiceRequest;
import com.gsg.mongo.model.UserVehicle;
import com.gsg.mongo.model.master.SchemeData;
import com.gsg.mongo.model.master.Services;
import com.gsg.services.AppUserService;
import com.gsg.services.OfficeDetailsService;
import com.gsg.services.OrderService;
import com.gsg.utilities.GSGCommon;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public class InvoiceReportUtility extends ReportUtility {

	@Autowired
	OrderService orderService;

	@Autowired
	AppUserService userService;
	
	@Autowired
	OfficeDetailsService officeService;

	@Autowired
	Invoice invoice;
	
	@Autowired
	ApplicationContext context;
	
	JRBeanCollectionDataSource servicesDataSource;
	
	@Autowired
	ResourceLoader resourceLoader;

	@Override
	public void reportConstructor(Object... inData) throws Exception {
		logger.info("reportConstructor() -- start");
		//officeService.getByState(state)
		
		
		
		AppUser user = (AppUser) inData[0];//userService.getByUserID(order.getUserId());//user
		Order order = (Order) inData[1];
		invoice.setOrder(order);
		invoice.setAppUser(user);
		invoice.setCreationDate(LocalDateTime.now());
		
		
		//AppUser svcEngr = userService.getByUserID(order.getAssignedToUserId());//service engineer
		
		// get ofc address
		//OfficeDetails ofc = officeService.getByState(svcEngr.getServiceArea().getState());
		OfficeDetails ofc = officeService.getOfficeDetails().get(0);

		invoice.setOrderId(order.getOrderId());
		invoice.setInvoiceId(order.getInvoiceNbr());
		invoice.setCompanyName(ofc.getCompanyName());
		invoice.setCompanyAddress(ofc.getAddress().getAddressFull());
		invoice.setOfcStateCd(ofc.getAddress().getStateCd());
		invoice.setGstin(ofc.getGstin());
		invoice.setPlaceOfSupply(ofc.getAddress().getState().toUpperCase());
		
		invoice.setBank(ofc.getBankdetails());
		
		
		//invoice.setAppUser(user);

		String userFirstName = (user.getFirstName() == null) ? "" : (user.getFirstName());
		String userMiddleName = (user.getMiddleName() == null) ? "" : (user.getMiddleName());
		String userLastName = (user.getLastName() == null) ? "" : (user.getLastName());
		
		invoice.setCustomerName(userFirstName + " " + userMiddleName + " " + userLastName);
		invoice.setCustomerId(user.getUserId());
		
		AddressBook address = null;
		List<AddressBook> addressList = user.getAddress();
		if (!addressList.isEmpty())
			address = addressList.get(0);
		
		if(address == null)	{
			invoice.setCustomerAddress("No address found");
		}	else	{
			invoice.setCustomerAddress(address.getAddressFull());
			invoice.setCustomerState(address.getState());
//			invoice.setCustomerStateCd(address.getStateCd());
		}
		
		double grandTotal = 0.0;
		invoice.setProductType(order.getProdutType());
		if(!order.getOrderDtls().isEmpty()){
			
			if(order.getProdutType().equals(GSGCommon.TYPE_SCHEME)){
				SchemeData sd = (SchemeData) order.getOrderDtls().get(0).getProduct();
				List<SchemeData> sdList = new ArrayList<SchemeData>();
				sdList.add(sd);
				invoice.setSchemeList(sdList);
			}else{ //service
				UserVehicle userVehicle = ((ServiceRequest)order.getOrderDtls().get(0).getProduct()).getUsrVehicle();
				invoice.setVehicleType(userVehicle.getVehicle().getMake()+" - "+userVehicle.getVehicle().getModels());
				invoice.setVehicleRegdNbr(userVehicle.getRegistrationNumber());
				
				List<Services> tempList = new ArrayList<Services>();
				
				for (OrderDetails odtl : order.getOrderDtls()) {
					List<Services> services = ((ServiceRequest) odtl.getProduct()).getServices();
					grandTotal += getServicesPrice(services);
					tempList.addAll(services);
					//invoice.setTotalDiscount(invoice.getTotalDiscount() + odtl.getDiscount());
				}
				servicesDataSource = new JRBeanCollectionDataSource(tempList);
				invoice.setGrandTotal(grandTotal);
				invoice.setServiceList(tempList);
			}
		}
		
		invoice.setCreationDate(order.getCreationDate());
		
		
		// create datasource for service sub report
		
		// Create the data source
		List<Object> listDataSource = new ArrayList<Object>();
		listDataSource.add(invoice);
		dataSource = new JRBeanCollectionDataSource(listDataSource);
		logger.info("reportConstructor() -- end");
		

	}

	private double getServicesPrice(List<Services> services) {
		double servicePrice = 0.0;
		for(Services service: services) {
			servicePrice += service.getEffectivePrice();
		}
		return servicePrice;
	}

	@Override
	public void reportMetadata() throws JRException, IOException {
		logger.info("reportMetadata() -- start");
		/*java.awt.Image image_c = Toolkit.getDefaultToolkit().createImage(JRLoader.loadBytesFromResource("chk_c.jpg"));
		
		jasperReport = JasperCompileManager.compileReport("/root/report/invoice.jrxml");
		JasperReport serviceReport = JasperCompileManager.compileReport("/root/report/subInvoiceService.jrxml");
		JasperReport schemeReport = JasperCompileManager.compileReport("/root/report/subInvoiceScheme.jrxml");
		String test = context.getResource("/").getURI().getPath();
		reportParam.put("CONTEXT","C:\\Users\\Rajendra\\Desktop\\gsg\\report");
		reportParam.put("serviceReport", serviceReport);
		reportParam.put("schemeReport", schemeReport);*/
		reportParam.put("serviceSubreport", servicesDataSource);
		
//		jasperReport = JasperCompileManager.compileReport("E:\\projects\\goApps\\gsg\\gsg-server\\src\\main\\resources\\report\\invoice1.jrxml");
//		serviceSubReport = JasperCompileManager.compileReport("E:\\projects\\goApps\\gsg\\gsg-server\\src\\main\\resources\\report\\serviceSubReport.jrxml");
		jasperReport = JasperCompileManager.compileReport("/root/report/invoice1.jrxml");
		serviceSubReport = JasperCompileManager.compileReport("/root/report/serviceSubReport.jrxml");
		logger.info("reportMetadata() -- end");
	}
	
	@Override
	public void fillReport() throws JRException {
		logger.info("fillReport() -- start");
		jasperPrint = JasperFillManager.fillReport(jasperReport, reportParam, dataSource);
		logger.info("fillReport() -- end");
	}

	@Override
	public byte[] generateReportByte(String orderId) throws JRException {
		logger.info("generateReportByte() -- start");
		//testing
		generateReport(orderId);
		////////
		
		byte[] inputByte = JasperExportManager.exportReportToPdf(jasperPrint);
		logger.info("InvoiceUtility.generatePDF() -- end");
		return inputByte;

	}
	
	@Override
	public void generateReport(String reportName) throws JRException {
		JasperExportManager.exportReportToPdfFile(jasperPrint, "/root/report/prod/" + reportName + "_"+ System.currentTimeMillis() + ".pdf");
	}



}
