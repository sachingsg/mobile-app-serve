package com.gsg.report;

import org.springframework.stereotype.Component;

@Component
public class InvoiceUtility  {/*
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	AppUserService userService;
	
	
	Logger logger = LoggerFactory.getLogger(InvoiceUtility.class);
	
	public Invoice getDataForReport(String orderId) throws ResourceNotFoundException	{
		
		logger.info("InvoiceUtility.getInvoiceData() -- start");
		
		Invoice invoice = new Invoice();
		AddressBook address;
		
		Order order = this.orderService.getByOrderId(orderId);
	
		invoice.setOrder(order);
		
		AppUser user = userService.getByUserID(order.getUserId());
		
		String userFirstName = (user.getFirstName() == null) ? "" : (user.getFirstName());
		String userMiddleName = (user.getMiddleName() == null) ? "" : (user.getMiddleName());
		String userLastName = (user.getLastName() == null) ? "" : (user.getLastName());
		
		invoice.setBillingName(userFirstName + " " + userMiddleName + " " + userLastName);
		
//		invoice.setAppUser(user);
		
		logger.info("InvoiceUtility.getInvoiceData() -- address -- start");
		List<AddressBook> addressList = user.getAddress();
		if (addressList.isEmpty())
			address = null;
		else
			address = addressList.get(0);
		
		logger.info("InvoiceUtility.getInvoiceData() -- address -- end");
		
		if(address == null)	{
			invoice.setBillingAddress("No address found");
		}	else	{
			String houseNbr = (address.getHouseNbr() == null) ? "" : (address.getHouseNbr() + ", ");
			String apartmentName = (address.getApartmentName() == null) ? "" : (address.getApartmentName() + ",\n");
			String street = (address.getStreet() == null) ? "" : (address.getStreet() + ", ");
			String locality = (address.getLocality() == null) ? "" : (address.getLocality() + ",\n");
			String po_ps_name = (address.getPo_ps_name() == null) ? "" : (address.getPo_ps_name() + ",\n");
			String city = (address.getCity() == null) ? "" : (address.getCity() + ", ");
			String state = (address.getState() == null) ? "" : (address.getState() + ",\n");
			String country = (address.getCountry() == null) ? "" : address.getCountry();
			
			invoice.setBillingAddress(houseNbr + apartmentName + street + locality + po_ps_name + city + state + country);
		}

		invoice.setGsgRegdNo("21XXXXXXXXXX5");
		invoice.setInvoiceDate(LocalDateTime.now());
		
		double grandTotal = 0; 
		
		for(OrderDetails dtls : order.getOrderDtls())	{
			grandTotal += dtls.getPayableAmount();
		}
		
		invoice.setGrandTotal(new BigDecimal(grandTotal));
		
//		invoice.setOrderId(order.getOrderId());
//		invoice.setOrderDate(order.getCreationDate());
		
		
		logger.info("InvoiceUtility.getInvoiceData() -- end");
		
		return invoice;
	}
	
	public byte[] generatePDF(Invoice invoice) throws Exception	{
		
		logger.info("InvoiceUtility.generatePDF() -- start");
					
//		ArrayList<OrderDetails> orderList = (ArrayList<OrderDetails>) invoice.getOrder().getOrderDtls();
		
		//Compile the template and create the input stream from the template
		JasperReport jasperReport = JasperCompileManager.compileReport("/root/report/Invoice.jrxml");
		
		JasperReport serviceReport = JasperCompileManager.compileReport("/root/report/subInvoiceService.jrxml");
		JasperReport schemeReport = JasperCompileManager.compileReport("/root/report/subInvoiceScheme.jrxml");
		//Create the data source
		
		List<Object> mainData = new ArrayList<Object>();
		mainData.add(invoice);
		JRBeanCollectionDataSource invoiceData = new JRBeanCollectionDataSource(mainData);
		
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("title", "Invoice Data");
		params.put("serviceReport", serviceReport);
		params.put("schemeReport", schemeReport);
		
		
		
		//Print the data source in the input stream
		JasperPrint print =  JasperFillManager.fillReport(jasperReport, params, 
				invoiceData);		
		
		//Output the print into a PDF file
		JasperExportManager.exportReportToPdfFile(print, "/root/Invoice"+System.currentTimeMillis()+".pdf");
		byte[] inputByte = JasperExportManager.exportReportToPdf(print);
		logger.info("InvoiceUtility.generatePDF() -- end");
		return inputByte;
		
		
	}
	
*/}