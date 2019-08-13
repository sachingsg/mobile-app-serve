package com.gsg.services.impl;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.gsg.error.GenericException;
import com.gsg.error.RTException;
import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.AddOnService;
import com.gsg.mongo.model.AppUser;
import com.gsg.mongo.model.Cart;
import com.gsg.mongo.model.Order;
import com.gsg.mongo.model.OrderCheckout;
import com.gsg.mongo.model.OrderCheckout.ProductInfo;
import com.gsg.mongo.model.OrderDetails;
import com.gsg.mongo.model.OrderStatus;
import com.gsg.mongo.model.ServiceRequest;
import com.gsg.mongo.model.UserVehicle;
import com.gsg.mongo.model.master.SchemeData;
import com.gsg.mongo.model.master.SchemeData.ReferralDetails;
import com.gsg.mongo.model.master.SchemeData.SchemeService;
import com.gsg.mongo.model.master.Services;
import com.gsg.mongo.model.master.Services.Price;
import com.gsg.mongo.model.master.VehicleData.Vehicle;
import com.gsg.mongo.model.rt.TicketRequest;
import com.gsg.mongo.model.rt.TicketRequest.CustomFields;
import com.gsg.mongo.model.rt.TicketResponse;
import com.gsg.mongo.repository.CountersRepository;
import com.gsg.services.AppUserService;
import com.gsg.services.CartService;
import com.gsg.services.MasterDataService;
import com.gsg.services.OfficeDetailsService;
import com.gsg.services.OrderDetailService;
import com.gsg.services.OrderService;
import com.gsg.services.ReportService;
import com.gsg.services.SalesUserService;
import com.gsg.services.ServiceModule;
import com.gsg.utilities.GSGCommon;
import com.gsg.utilities.MapUtility;
import com.gsg.utilities.PaymentGateway;
import com.gsg.utilities.SMSUtility;
import com.gsg.utilities.TicketUtility;

@Service
public class ServiceModulesImpl implements ServiceModule {

	Logger logger = LoggerFactory.getLogger(ServiceModulesImpl.class);

	@Autowired
	private AppUserService appUserService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderDetailService orderDetailService;

	@Autowired
	private CartService cartService;

	@Autowired
	private MasterDataService masterDataService;
	
	@Autowired
	private OfficeDetailsService officeService;
	
	@Autowired
	private SalesUserService salesUserService;
	
	@Autowired
	private ReportService reportService;

	@Autowired
	private TicketUtility ticketUtility;

	@Autowired
	private SMSUtility smsUtility;
	
	@Autowired
	private MapUtility mapUtility;

	@Autowired
	CountersRepository counterRepo;

	@Autowired
	GSGCommon gsgCommon;

	@Autowired
	MessageSource messageResource;

	@Autowired
	PaymentGateway pg;
	
	@Autowired
	private Environment env;
	
	
	// ////////////

	// MODULE 1 - Data Validation
	@Override
	public double validateAndProcessSRData(ServiceRequest sReq, AppUser user) throws ResourceNotFoundException, GenericException {
		logger.info("ServiceModulesImpl.validateAndProcessSRData()");

		logger.debug("ServiceRequest>>" + sReq);

		
		// 1. check if location data is present
		if (sReq.getLocation() == null || sReq.getLocation().empty()) {
			throw new GenericException("Location data not found in the request");
		}else{
			/*List<String> ofcs = officeService.getAllOfficeStates();
			//appUserService.getUsersWithin(sReq.getLocation());
			try {
				String stateName = mapUtility.getLocationStateDetails(sReq.getLocation());
				
				if (!ofcs.contains(stateName.toUpperCase())) {
					throw new GenericException("Location is outside of service area.");
				}
			} catch (GenericException ex) {
				throw ex;
			} catch (Exception ex) {
				throw new GenericException("Could not process location details");
			}*/
		}

		logger.debug("location Details found and inside service region.");
		
		if (!StringUtils.trimToEmpty(sReq.getServiceType()).equalsIgnoreCase(GSGCommon.SERVICE_EMERGENCY)) {
		
			// 2. Check for service Date, it should not be older than today
			if (sReq.getServiceDate().isBefore(LocalDate.now())) {
				throw new GenericException("Service Date can not be older than today.");
			}
	
			// 3. check if the selected services are present and valid - refresh from DB
			List<Services> userOptedServicesMD = sReq.getServices();
			List<Services> userOptedServicesMDRefreshed = new ArrayList<Services>();
			// get refreshed value from DB using the serviceId only
			List<String> svcIds = new ArrayList<>();
			for (Services svc : userOptedServicesMD) {
				if (svc.getCategory().equals(GSGCommon.SVC_CONSUMABLE) || svc.getCategory().equals(GSGCommon.SVC_SPARE)
						|| svc.getCategory().equals(GSGCommon.SVC_LABOUR)) { // if not a service - for ADD On Service
					userOptedServicesMDRefreshed.add(svc);
				}
				else {
					svcIds.add(svc.getServiceId());
				}
			}
			
			List<Services> dbServices = masterDataService.getServiceIn(svcIds);
			for(Services svc:dbServices){
				if(userOptedServicesMD.contains(svc)){
					Services tempSvc = userOptedServicesMD.get(userOptedServicesMD.indexOf(svc));
					svc.setQuantity(tempSvc.getQuantity());
				}
			}
			
			userOptedServicesMDRefreshed.addAll(dbServices);
			
			// override the fetched service to request for further processing
			sReq.setServices(userOptedServicesMDRefreshed);
			
			
			
	
			// 4. get vehicle details
			Vehicle uv = new Vehicle();
			uv = sReq.getUsrVehicle().getVehicle();
			if (ObjectUtils.isEmpty(uv) || uv.isEmpty()) {
				throw new GenericException("Vehicle details not found in the request");
			}
	
			logger.debug("Vehicle Details found");
		}
		
		return 0;
	}

	public double calculateOrderTotalPrice(Order order, double curOdPrice) {
		
		double totalPrice = 0;
		for(OrderDetails odtl : order.getOrderDtls()){
			totalPrice += odtl.getPayableAmount();
		}
		totalPrice += curOdPrice;
		return totalPrice;

	}
	
	@Override
	public double calculateRequestPriceUsingOffer(AppUser user, ServiceRequest sReq){

		Vehicle v = sReq.getUsrVehicle().getVehicle();
		String vehicleType = v.getType();//luxury
		String vehicleSubType = v.getSubType(); //LMV
		
		logger.info("vehicleType>>" + vehicleType);
		logger.info("vehicleSubType>>" + vehicleSubType);

		SchemeData userSchemeData = null;
		boolean isSchemeToBeApplied = false;
		if (sReq.isUseUserScheme()) {
			// find appropriate scheme from user schemes
			for (SchemeData userSchme : user.getSchemes()) {
				// Select Based on the vehicle selected
				if (userSchme.getSchemeType().equals(vehicleSubType)) {
					userSchemeData = userSchme;
				}
			}

			if (userSchemeData != null) {
				isSchemeToBeApplied = true;
				logger.info("Appropriate Scheme found for user");
			}
			else {
				isSchemeToBeApplied = false;
				logger.info("No Appropriate Scheme found for user");
			}

		}
		else {
			isSchemeToBeApplied = false;
			logger.info("Use has not confimed to use form the subscribed schemes");

		}

		logger.info("Calculating the price per Order Details");
		double effectiveTotal = 0;

		List<Services> orderSvcs = sReq.getServices();

		if (isSchemeToBeApplied) { // With Valid scheme Data
			List<SchemeService> userSchemeServices = userSchemeData.getSchemeServiceDtls();
			logger.info(">>>>1");
			for (Services userOptedSvc : orderSvcs) {
				if (userSchemeServices.contains(new SchemeService(userOptedSvc.getServiceId()))) {
					SchemeService ss = userSchemeServices.get(userSchemeServices.indexOf(new SchemeService(userOptedSvc.getServiceId())));
					// if free service is available
					if ((ss.getFreeUsage() - ss.getUsed()) > 0) {
						userOptedSvc.setFreeApplied(true);
					}
				}
				
				userOptedSvc.calculatePrices(vehicleType);
				logger.info("1. Effectiveprice"+userOptedSvc.getEffectivePrice());
				effectiveTotal += userOptedSvc.getEffectivePrice();
				
			}
		}else{
			logger.info(">>>>2");
			for (Services userOptedSvc : orderSvcs) {
				userOptedSvc.calculatePrices(vehicleType);
				logger.info("2. Effectiveprice"+userOptedSvc.getEffectivePrice());
				effectiveTotal += userOptedSvc.getEffectivePrice();
				
			}
		}
		

		return effectiveTotal;

	}

	// MODULE 2 - ADD TO CART
	// public Cart addToUserCart(String orderId, double requestPrice,
	// ServiceRequest sReq) {
	@Override
	public Cart addToUserCart(String userId, String orderId, Object product, String productType) {
		logger.info("ServiceModulesImpl.addToUserCart()");

		double totalPrice = 0;
		// Calculate total Price
		if (productType.equals(GSGCommon.TYPE_SCHEME)) {
			SchemeData sc = (SchemeData) product;
			totalPrice = sc.getPrice();
		}
		else if (productType.equals(GSGCommon.TYPE_SERVICE)) {
			ServiceRequest sReq = (ServiceRequest) product;
			totalPrice = calculateRequsetPrice(sReq);
		}

		Cart cart = new Cart();
		cart.setOrderId(orderId);
		cart.setProductType(productType);
		
		cart.setProduct(product);
		cart.setAmount(totalPrice);
		cart.setUserId(userId);
		if (!productType.equals(GSGCommon.TYPE_SCHEME)) {
			ServiceRequest sr = (ServiceRequest) product;
			cart.setServiceDate(sr.getServiceDate());
		}

		cartService.saveToCart(cart);

		return cart;
	}

	// MODULE 3 -- ORDER CREATION

	@Override
	public Order createOrder(String userId, String orderId, String prdType, Object product, double orderTotalPrice, OrderCheckout oc) throws ResourceNotFoundException {

		AppUser user = appUserService.getByUserID(userId);
		String msg = "";

		// Calculate price based on scheme
		//double priceWitoutTax = 0;
		if (prdType.equals(GSGCommon.TYPE_SERVICE)) {
			ServiceRequest sReq = (ServiceRequest) product;
			logger.info("Calling calculateRequestPriceUsingOffer");
			calculateRequestPriceUsingOffer(user, sReq); // save price related details to Service request
			
		}else if (prdType.equals(GSGCommon.TYPE_SCHEME)){
			SchemeData sd = (SchemeData) product;
			sd.calculatePrice();
		}
		
		// Add to order/order Dtl
		OrderDetails od = new OrderDetails();
		od.setProdutType(prdType);
		od.setUserId(userId);
		od.setProduct(product);
		
		od.setCalculatedPrice(oc.getAmount()); // without tax
		od.setPayableAmount(oc.getPayableAmount());
		od.setRoundedAmount(oc.getRoundedAmount());
		od.setTotalGst(oc.getPayableAmount() - oc.getAmount());
		
		od.setTransactionStatus(oc.getTranSts());
		od.setTransactionMode(oc.getTranMode());
		od.setTransactionDate(oc.getTranDate());
		od.setTransactionId(oc.getTranId());
		// Tax Calculation
//		od.setCgst(oc.getCgst());
//		od.setSgst(oc.getSgst());
//		od.setIgst(oc.getIgst());
//		od.setPayableAmount(oc.getPayableAmount());

		orderDetailService.saveDetails(od);

		// Order
		Order o = new Order();
		// If not a ADD On Request
		if (StringUtils.trimToEmpty(orderId).isEmpty()) {

			String orderSeqId = String.valueOf(counterRepo.findAndModifySeq("orderid").getSeq());

			o.setOrderId("REQ" + StringUtils.leftPad(orderSeqId, 10, '0'));
			o.setTotalPrice(oc.getPayableAmount());
			o.setUserId(userId);
			o.setProdutType(prdType);
			o.getOrderDtls().add(od);
			// if (!prdType.equals(GSGCommon.TYPE_SCHEME)) {
			// ServiceRequest sr = (ServiceRequest) product;
			// o.setServiceDate(sr.getServiceDate());
			// }

			// user details

			o.setUserId(user.getUserId());
			o.setUserName(StringUtils.trimToEmpty(user.getFirstName()) + " "
					+ (StringUtils.trimToEmpty(user.getMiddleName()).isEmpty() ? " " : user.getMiddleName() + " ")
					+ StringUtils.trimToEmpty(user.getLastName()));
			o.setContactNbr(user.getContactNbr());
			// / Work related status
			o.setAssignedQueue(GSGCommon.WORK_QUEUE_OPERATION);
			o.setAssignedToUserId("");

			// status
			if (prdType.equals(GSGCommon.TYPE_SCHEME)) {
				o.setRequestStatus(GSGCommon.WORK_STS_SCHEME);
				//set invoice nbr as this is the only stage for scheme
				o.setInvoiceNbr(orderService.getInvoiceNbr());

			}
			else { // SERVICE
				ServiceRequest sr = (ServiceRequest) product;
				o.setServiceDate(sr.getServiceDate());
				if (sr.getServiceType().equalsIgnoreCase(GSGCommon.WORK_STS_EMERGENCY)) {
					o.setRequestStatus(GSGCommon.WORK_STS_EMERGENCY);
				}
				else {
					o.setRequestStatus(GSGCommon.WORK_STS_CREATED);
				}
			}
		}
		else { // If a ADD On Request
			o = orderService.getByOrderId(orderId);

			o.setTotalPrice(calculateOrderTotalPrice(o, oc.getPayableAmount())); // add order-total amount for add on service
			if (ObjectUtils.isEmpty(o)) {
				throw new ResourceNotFoundException(Order.class, "Order Id", orderId);
			}
			o.getOrderDtls().add(od);
		}
		// Save Order
		orderService.saveOrder(o);

		// orderService.saveOrderDetails(o);
		// For Service(COD/PG) call post processing
		// For Scheme (PG) call post processing
		if (prdType.equals(GSGCommon.TYPE_SERVICE)) {
			ServiceRequest sr = (ServiceRequest) product;
			// deduct free services from user scheme
			postProcessingNormalService(o, sr);
		}
		else if (prdType.equals(GSGCommon.TYPE_SCHEME)) {
			subscribeScheme(od);
		}

		

		// send msg
		if (prdType.equals(GSGCommon.TYPE_SCHEME)) { // Scheme
			SchemeData sd = (SchemeData) product;
			if (StringUtils.trimToEmpty(oc.getTranMode()).equals("COD")) { // COD
				msg = "Your scheme (" + sd.getDescription() + ") will be actived once the payemt is received. "
						+ "Please visit to any nearest GSG center for payment.";
			}
			else { // ONLINE
				msg = "Thank you for subscribing "+ sd.getDescription() + " scheme. For any more information contact our customer care. ";
			}

		}
		else { // Service
			String amountToShow = GSGCommon.getTwoPrecisionStringValue(od.getPayableAmount());
			
			if (StringUtils.trimToEmpty(orderId).isEmpty()) { // New Order

				if (prdType.equals(GSGCommon.SERVICE_EMERGENCY)) {
					msg = "Your Request has been registered with Reference ID " + o.getOrderId()+". "
							+ "Our customer support will get back to you soon.";
				}
				else { // Normal Service

					if (StringUtils.trimToEmpty(oc.getTranMode()).equals("COD")) {
						msg = "Your Request has been registered with Reference ID " + o.getOrderId()+". "+
								"Amount to pay INR " + amountToShow;
					}
					else { // ONLINE
						msg = "Your Service Request has been registered with Request:" + o.getOrderId()+". "
								+ "Amount paid INR " + amountToShow;
					}
				}
			}
			else { // AddOn Services
				if (StringUtils.trimToEmpty(oc.getTranMode()).equals("COD")) {
					msg = "Your Addon services have been added to your Request " + o.getOrderId() +
							" successfully. Amount to pay INR " + amountToShow;
				}
				else {// ONLINE
					msg = "Your Addon services have been added to your Request " + o.getOrderId() +
							" successfully. Amount paid INR " + amountToShow;
				}
			}
		}

		smsUtility.sendStatusMessageToUser(msg, user.getContactNbr());
		/////////////////

		return o;

	}

	@Override
	public void postProcessingNormalService(Order o, ServiceRequest sr) throws ResourceNotFoundException {
		logger.info("ServiceModulesImpl.postProcessingNormalService()");

		// Once order is done deduct the free usage from user scheme

		// ServiceRequest sr = (ServiceRequest) o.getProduct();
		// get user scheme service
		AppUser user = appUserService.getByUserID(o.getUserId());
		SchemeData userSchemeData = new SchemeData();
		for (SchemeData userScheme : user.getSchemes()) {
			if (StringUtils.trimToEmpty(userScheme.getSchemeType())
					.equals(sr.getUsrVehicle().getVehicle().getSubType())) {
				userSchemeData = userScheme;
			}
		}

		for (Services ordrService : sr.getServices()) {

			if (ordrService.isFreeApplied()) {
				for (SchemeService ss : userSchemeData.getSchemeServiceDtls()) {
					if (ordrService.getServiceId().equals(ss.getServiceId())) {
						ss.setUsed(ss.getUsed() + 1);
					}
				}
			}
		}

		// check expiry of scheme
		/*
		 * boolean isExpired = true; for (SchemeService ss : userSchemeData.getSchemeServiceDtls()) { if
		 * (ss.getFreeUsage() != ss.getUsed()) { isExpired = false; break; }
		 * 
		 * } userSchemeData.setUserSchemeExpired(isExpired);
		 */
		// Save user Details
		appUserService.updateUser(user);

	}

	// MODULE 4 - CHECKOUT

	@Override
	public OrderCheckout checkOut(AppUser user, Cart cart, OrderCheckout oc){

		logger.info("ServiceModulesImpl.checkOut()");

		double payableAmount = 0;//, totalPrice = 0;
		// Get product details
		if (cart.getProductType().equals(GSGCommon.TYPE_SCHEME)) {
			
			SchemeData sc = (SchemeData) cart.getProduct();
			payableAmount = //GSGCommon.getTwoPrecisionDoubleValue(sc.getPrice() + (sc.getPrice() * sc.getGst()/100));  
						GSGCommon.getTaxPrice(sc.getPrice(), sc.getGst());
			oc.getProductInfo().add(new ProductInfo(sc.getDescription(), sc.getPrice(), false, sc.getGst(), payableAmount));

		}
		else {
			ServiceRequest sr = (ServiceRequest) cart.getProduct();
			
			// Calculate the price using schemes
			payableAmount = GSGCommon.getTwoPrecisionDoubleValue(calculateRequestPriceUsingOffer(user, sr));
			
			for (Services s : sr.getServices()) {
				oc.getProductInfo().add(new ProductInfo(s.getSubCategory(), s.getEffectivePrice(), s.isFreeApplied(), s.getGst(), s.getEffectivePrice()));
				
				//payableAmount +=  s.getEffectivePrice();
				//totalPrice += s.getEffectivePrice();
				
			}
		}
		
		//oc.setTotalGst(payableAmount - totalPrice);
		oc.setRoundedAmount(GSGCommon.roundedValue(payableAmount));


		String bankEncryptUrl = pg.formEncryptedURL(cart.getId(), String.valueOf(oc.getRoundedAmount()),
				user.getUserId(), user.getContactNbr());

		////////////////////

		oc.setCodUrl(pg.getCodUrl());
		oc.setPgUrl(bankEncryptUrl);
		oc.setReferenceno(cart.getId());
		oc.setAmount(cart.getAmount());

		/*
		 * oc.setCgst(String.format("%.2f", cgstVal)); oc.setSgst(String.format("%.2f", sgstVal));
		 * oc.setIgst(String.format("%.2f", igstVal));
		 */
		oc.setPayableAmount(payableAmount);

		oc.setProductType(cart.getProductType());
		
		System.out.println("OC >>" + oc);

		return oc;
	}

	// MODULE 5 - RT TICKET CREATION
	@Override
	@Async
	public void createTicketInRT(ServiceRequest sr, String orderId) {
		logger.info("ServiceModulesImpl.createTicketInRT()");

		// Create RT ticket
		// Thread.sleep(10000);
		TicketRequest tr = new TicketRequest();
		tr.setQueue(GSGCommon.WORK_QUEUE_OPERATION); // give a valid Queue
		tr.setSubject("Ticket created Request:" + orderId + " by user id " + sr.getUserId());
		// tkt.setPriority("1");
		tr.setStatus("open");

		CustomFields cf = new CustomFields();
		cf.setLocation(sr.getLocation().getLat() + "," + sr.getLocation().getLng());
		sr.getServices().forEach(s -> {
			cf.getServices().add(s.getSubCategory());
		});

		if (!sr.getUsrVehicle().getVehicle().isEmpty())
			cf.setVehicle(sr.getUsrVehicle().getVehicle().toString());

		tr.setCustomFields(cf);

		logger.info("Creating Ticket in RT in Async Mode");
		// Call RT to create ticket
		TicketResponse tktRes = null;
		try {
			tktRes = ticketUtility.createRTTicket(tr);
			logger.info("Ticket:" + tktRes.getId() + "Created for Request:" + orderId);

			// Update details to Ticket DB
			updateRTdetailsToOrder(orderId, tktRes.getId());

		}
		catch (Exception rte) {
			logger.error("Could not Create ticket in RT for request:" + orderId + " >> Error::" + rte.getMessage());
		}

		// return new AsyncResult<TicketResponse>(tktRes);

	}

	@Override
	public OrderCheckout buyCartOrders(String userId, String cartId) throws ResourceNotFoundException, GenericException,
			NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		logger.info("ServiceModulesImpl.buyCartOrders()");

		// check is user has that cart id
		Cart cart = cartService.getCartDataofUser(cartId, userId);
		AppUser user = appUserService.getByUserID(userId);

		// Checkout
		OrderCheckout oc = new OrderCheckout();
		oc.setIsPayable("YES");

		checkOut(user, cart, oc);

		return oc;

	}

	@Override
	public OrderCheckout createServiceRequest(ServiceRequest sReq, AppUser user) throws IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, ResourceNotFoundException, GenericException, RTException {
		logger.info("ServiceModulesImpl.createRequest()");

		OrderCheckout oc = new OrderCheckout();

		if (StringUtils.trimToEmpty(sReq.getServiceType()).equalsIgnoreCase(GSGCommon.SERVICE_EMERGENCY)) {
			logger.info("Emergency service selected");
			validateAndProcessSRData(sReq, user); //for location validation
			// flow 3-5-6
			Order order = emergencyServiceRequest(sReq);
			// call module 5 - 6

			// serviceModules.createTicket(order.getOrderId(), sReq);
			try {
				createTicketInRT(sReq, order.getOrderId());
			}
			catch (Exception e) {
				logger.error("RT Error " + e.getMessage());
			}

			oc.setIsPayable("NO");
		}
		else {
			logger.info("Normal service selected");

			// Zero amount flow 1a-3
			// Non Zero Amount flow 1a-2-4
			// Module 1a. validate data
			validateAndProcessSRData(sReq, user);
			double totalPricePerRequest = calculateRequestPriceUsingOffer(user, sReq); //calculateCartTotalPrice(sReq);

			if (totalPricePerRequest == 0) {
				noPaymentServiceRequest(sReq, totalPricePerRequest);
				oc.setIsPayable("NO");
			}
			else {
				Cart cart = normalServiceRequest(sReq);
				oc.setIsPayable("YES");
				checkOut(user, cart, oc);
			}
		}
		return oc;
	}

	@Override
	public void cancelOrderProcessing(String orderId) throws ResourceNotFoundException, GenericException,
			NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		logger.info("ServiceModulesImpl.cancelOrderPostProcessing()");

		Order order = orderService.getByOrderId(orderId);
		if (!order.getRequestStatus().equals(GSGCommon.WORK_STS_CANCELED)) {
			throw new GenericException("Order yet has not been canceled.");
		}

		// only for service, though, for Scheme order that wont be any cancel

		Class<?> pclass = new Price().getClass();

		if (order.getProdutType().equals(GSGCommon.TYPE_SERVICE)) {

			for (OrderDetails oDtl : order.getOrderDtls()) {
				ServiceRequest sr = (ServiceRequest) oDtl.getProduct();
				AppUser user = appUserService.getByUserID(sr.getUserId());
				SchemeData userSchemeData = null;
				for (SchemeData scheme : user.getSchemes()) {
					if (scheme.getSchemeType().equals(sr.getUsrVehicle().getVehicle().getSubType())) {
						userSchemeData = scheme;
					}
				}
				Field fp = pclass.getDeclaredField(sr.getUsrVehicle().getVehicle().getType());
				fp.setAccessible(true);

				if (userSchemeData != null) {

					for (Services ordrService : sr.getServices()) {

						if (ordrService.isFreeApplied()) {
							for (SchemeService ss : userSchemeData.getSchemeServiceDtls()) {
								if (ordrService.getServiceId().equals(ss.getServiceId())) {
									ss.setUsed(ss.getUsed() - 1);
								}
							}
						}
					}

					// check expiry of scheme
					/*
					 * if (sr.isUseUserScheme()) { boolean isExpired = true; for (SchemeService ss :
					 * userSchemeData.getSchemeServiceDtls()) { if (ss.getFreeUsage() != ss.getUsed()) { isExpired =
					 * false; break; }
					 * 
					 * } userSchemeData.setUserSchemeExpired(isExpired); // save scheme // data //
					 * user.getSchemes().set(schemeDataIndex, userSchemeData); // appUserRepository.save(user);
					 * 
					 * }
					 */

					appUserService.updateUser(user);

				}
			} // for loop

		}
	}

	@Override
	public Order emergencyServiceRequest(ServiceRequest sReq) throws ResourceNotFoundException {
		logger.info("ServiceModulesImpl.emergencyServiceRequest()");
		// flow 3-5-6
		// call module 3 - create order

		// Get Customer details
		OrderCheckout oc = new OrderCheckout();
		oc.setAmount(0);
		oc.setTranDate(null);
		oc.setTranId(GSGCommon.TRANSACTION_NOT_APPLICABLE);
		oc.setTranMode(GSGCommon.TRANSACTION_NOT_APPLICABLE);
		oc.setTranSts(GSGCommon.TRANSACTION_NOT_APPLICABLE);

		Order order = createOrder(sReq.getUserId(), null, GSGCommon.SERVICE_EMERGENCY, sReq, 0, oc);

		// Send Message

		return order;

	}

	private Order noPaymentServiceRequest(ServiceRequest sReq, double totalPricePerRequest)
			throws ResourceNotFoundException {
		logger.info("ServiceModulesImpl.noPaymentServiceRequest()");

		OrderCheckout oc = new OrderCheckout();
		oc.setAmount(0);
		oc.setTranDate(null);
		oc.setTranId(GSGCommon.TRANSACTION_NOT_APPLICABLE);
		oc.setTranMode(GSGCommon.TRANSACTION_FROM_SCHEME);
		oc.setTranSts(GSGCommon.TRANSACTION_COMPLETED);
		Order order = createOrder(sReq.getUserId(), null, GSGCommon.SERVICE_NORMAL, sReq, totalPricePerRequest, oc);

		return order;
	}

	public Cart normalServiceRequest(ServiceRequest sReq) throws IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, ResourceNotFoundException, GenericException {
		logger.info("OrderServiceImpl.normalServiceRequest()");

		// Module 2 - Add to User cart
		Cart cart = addToUserCart(sReq.getUserId(), null, sReq, GSGCommon.SERVICE_NORMAL);

		// // Module 4 - Checkout
		// AppUser user = appUserService.getUserByUserID(sReq.getUserId());
		// OrderCheckout oc = new OrderCheckout(user, cart);

		return cart;

	}

	@Override
	public Cart addOnServiceRequest(String orderId, List<AddOnService> aosList) throws GenericException, ResourceNotFoundException{
		logger.info("ServiceModulesImpl.addOnServiceRequest()");

		if (ObjectUtils.isEmpty(aosList) || aosList.isEmpty()) {
			throw new GenericException("Please select some Services.");
		}

		Order order = orderService.getByOrderId(orderId);
		if (ObjectUtils.isEmpty(order.getOrderDtls()) || order.getOrderDtls().isEmpty()) {
			throw new GenericException("Not a valid request.");
		}

		ServiceRequest srOld = (ServiceRequest) order.getOrderDtls().get(0).getProduct();

		// ServiceRequest from AddOnService

		ServiceRequest srNew = new ServiceRequest();

		srNew.setLocation(srOld.getLocation());
		srNew.setServiceType(srOld.getServiceType());
		srNew.setServiceDate(LocalDate.now());// srOld.getServiceDate());
		srNew.setUserId(srOld.getUserId());
		srNew.setUseUserScheme(srOld.isUseUserScheme());
		srNew.setUsrVehicle(srOld.getUsrVehicle());

		List<Services> serviceList = new ArrayList<>();
		for (AddOnService aos : aosList) {
			if (aos.getType().equals(GSGCommon.SVC_SERVICE)) {
				serviceList.addAll(aos.getServiceList());
			}
			else {
				if (!StringUtils.isEmpty(aos.getDesc()) && aos.getPrice() > 0) {
					serviceList.add(new Services(aos.getType(), aos.getDesc(), aos.getPrice(), aos.getQuantity(), aos.getGst()));
				}
			}
		}

		srNew.getServices().addAll(serviceList);

		// flow 1-2

		// call module 1. validate and process data. Get the totalAmount/request
		AppUser user = appUserService.getByUserID(srOld.getUserId());
		validateAndProcessSRData(srNew, user);
		// double totalPricePerRequest =calculateOrderPrice(user, srNew);

		// call module 2. Add to User cart
		Cart cart = addToUserCart(srNew.getUserId(), orderId, srNew, GSGCommon.SERVICE_NORMAL);
		return cart;

	}

	@Override
	public List<Cart> addOnCartDetails(String userId, String orderId) {
		return cartService.getByUserIdAndOrderId(userId, orderId);
	}

	@Override
	public AppUser subscribeScheme(OrderDetails odtl) throws ResourceNotFoundException {
		logger.info("OrderServiceImpl.subscribeScheme()");
		AppUser user = appUserService.getByUserID(odtl.getUserId());

		SchemeData scheme = (SchemeData) odtl.getProduct();
		scheme.setSubscriptionDt(LocalDate.now());
		scheme.setUserSchemeExpired(false);

		// Save to user
		user.getSchemes().add(scheme);

		// send Mail invoice
		
		
		/////////////////
		
		return appUserService.updateUser(user);

	}

	@Override
	public OrderCheckout buyScheme(String userId, String schemeId, ReferralDetails rfrDtl) throws ResourceNotFoundException, GenericException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		logger.info("ServiceModulesImpl.buyScheme()");
		// flow - 1b-2-4

		// Data validation - 1b
		AppUser user = appUserService.getByUserID(userId);

		SchemeData dbSchemeData = masterDataService.getScheme(schemeId);
		dbSchemeData.setRefrralDtl(rfrDtl);

		
		// VerifyRefral Code
		if(!ObjectUtils.isEmpty(rfrDtl)){
			if(!rfrDtl.getReferralCd().isEmpty()){
				if(!salesUserService.isReferralCodeValid(rfrDtl.getReferralCd())){
					throw new GenericException("Invalid Referral Code.");
				}
			}
		}
		
		// Check if user has any vehicle associated
		List<UserVehicle> userVehicles = user.getUserVehicles();
		if (ObjectUtils.isEmpty(userVehicles) || userVehicles.isEmpty()) {
			throw new GenericException("Please add a vehcicle first.");
		}
		else {
			boolean ifSchemeRelatedVehiclePresent = false;
			for (UserVehicle userVehicle : userVehicles) {
				if (StringUtils.trimToEmpty(userVehicle.getVehicle().getSubType())
						.equals(dbSchemeData.getSchemeType())) {
					ifSchemeRelatedVehiclePresent = true;
					break;
				}
			}

			if (!ifSchemeRelatedVehiclePresent) {
				throw new GenericException(gsgCommon.getMsg("scheme.novehicle"));
			}

		}

		// check if any scheme of same type is present in cart
		List<Cart> cartList = cartService.getByUserIdAndProductType(user.getUserId(), GSGCommon.TYPE_SCHEME);
		if (!ObjectUtils.isEmpty(cartList)) {
			// check if scheme types are same ?
			for (Cart c : cartList) {
				if (StringUtils.trimToEmpty(c.getProductType()).equals(dbSchemeData.getSchemeType())) {
					throw new GenericException(gsgCommon.getMsg("error.scheme.presentincart"));
				}
			}

		}

		// check if any pending scheme order is present
		List<OrderDetails> ordeDtls = orderDetailService.getByUserIdAndProdutType(user.getUserId(),
				GSGCommon.TYPE_SCHEME);
		// getByUserIdAndProdutType(user.getUserId(), GSGCommon.TYPE_SCHEME);
		if (!ObjectUtils.isEmpty(ordeDtls)) {

			for (OrderDetails od : ordeDtls) {
				if (StringUtils.trimToEmpty(od.getProdutType()).equals(GSGCommon.TYPE_SCHEME) && !StringUtils
						.trimToEmpty(od.getTransactionStatus()).equalsIgnoreCase(GSGCommon.TRANSACTION_COMPLETED)) {
					throw new GenericException(gsgCommon.getMsg("error.scheme.cod.pending"));
				}
			}
		}

		// check if a scheme exists with user, and of same type as the subscribing one and
		// not expired
		if (!user.getSchemes().isEmpty()) { // user has scheme

			for (SchemeData userScheme : user.getSchemes()) {
				int diff = Period.between(userScheme.getSubscriptionDt(), LocalDate.now()).getDays();

				if (userScheme.getSchemeType().equals(dbSchemeData.getSchemeType())
						&& diff < dbSchemeData.getDurationInDays() && !userScheme.isUserSchemeExpired() // not
																										// expired
				) {
					throw new GenericException(gsgCommon.getMsg("error.scheme.samescheme"));
				}
			}
		}

		// Call Module 2 for add to cart
		// Prepare Details
		// Cart cart = new Cart();
		// cart.setOrderId(null); // Only used in case additional service request
		// cart.setProductType(GSGCommon.TYPE_SCHEME);
		// cart.setProduct(dbSchemeData);
		// cart.setAmount(dbSchemeData.getPrice());
		// cart.setUserId(user.getUserId());
		//
		// cartService.saveToCart(cart);
		//
		Cart cart = addToUserCart(user.getUserId(), null, dbSchemeData, GSGCommon.TYPE_SCHEME);

		// call module 4 - checkout
		OrderCheckout oc = new OrderCheckout();
		oc.setIsPayable("YES");
		checkOut(user, cart, oc);

		return oc;

	}

	//////////////////////
	@Override
	public Order updateOrderStatusByOrderId(String ccUserId, String orderId, OrderStatus os)
			throws ResourceNotFoundException, GenericException {
		logger.info("OrderServiceImpl.updateOrderStatusByOrderId()");

		// validate order
		Order order = orderService.getByOrderId(orderId);

		// validate assigned To user
		
		AppUser ccUser = appUserService.getByUserID(ccUserId);
		AppUser appUser = appUserService.getByUserID(order.getUserId());

		// check if status is a valid status and a valid status to change
		if (!Arrays.asList(GSGCommon.WORK_STS).contains(os.getRequestStatus())) {
			throw new GenericException("Not a valid Staus to update.");
		}

		List<String> allowedStatus = new ArrayList<String>();

		// Logic based on the status ///////////////////////////////
		if (order.getRequestStatus().equals(GSGCommon.WORK_STS_EMERGENCY)
				|| order.getRequestStatus().equals(GSGCommon.WORK_STS_CREATED)) {
			// allowedStatus.add(GSGCommon.WORK_STS_WIP);
			if (ccUser.getRoles().contains(GSGCommon.OPERATION)) {
				allowedStatus.add(GSGCommon.WORK_STS_CANCELED);
				allowedStatus.add(GSGCommon.WORK_STS_CREATED);
				allowedStatus.add(GSGCommon.WORK_STS_EMERGENCY);
			}

			if (ccUser.getRoles().contains(GSGCommon.ENGINEER)) {
				allowedStatus.add(GSGCommon.WORK_STS_WIP);
				allowedStatus.add(GSGCommon.WORK_STS_REJECTED);
			}

		}
		else if (order.getRequestStatus().equals(GSGCommon.WORK_STS_WIP)) {
			if (ccUser.getRoles().contains(GSGCommon.OPERATION)) {
				allowedStatus.add(GSGCommon.WORK_STS_REJECTED);
			}
			if (ccUser.getRoles().contains(GSGCommon.ENGINEER)) {
				allowedStatus.add(GSGCommon.WORK_STS_RESOLVED);
			}
		}
		else if (order.getRequestStatus().equals(GSGCommon.WORK_STS_RESOLVED)) {
			if (ccUser.getRoles().contains(GSGCommon.OPERATION)) {
				allowedStatus.add(GSGCommon.WORK_STS_CLOSED);
			}
		}
		else if (order.getRequestStatus().equals(GSGCommon.WORK_STS_REJECTED)) {
			if (ccUser.getRoles().contains(GSGCommon.OPERATION)) {
				allowedStatus.add(GSGCommon.WORK_STS_CREATED);
				allowedStatus.add(GSGCommon.WORK_STS_CANCELED);
			}
		}
		else if (order.getRequestStatus().equals(GSGCommon.WORK_STS_CLOSED)) {

		}
		else if (order.getRequestStatus().equals(GSGCommon.WORK_STS_CANCELED)) {

		}
		////////////////////////////////////////////////////////////////////

		if (!allowedStatus.contains(os.getRequestStatus())) {
			throw new GenericException(
					"Can't change status from " + order.getRequestStatus() + " to " + os.getRequestStatus());
		}

		if (os.getRequestStatus().equals(GSGCommon.WORK_STS_RESOLVED)) {
			for (OrderDetails od : order.getOrderDtls()) {
				if (od.getTransactionStatus().equals(GSGCommon.TRANSACTION_PENDING)) {
					throw new GenericException("All payment has not been settled. Order can't be RESOLVED.");
				}
			}
			// set invoice id to order upon resolved
			order.setInvoiceNbr(orderService.getInvoiceNbr());
		}

		order.setAssignedQueue(os.getAssignedQueue());
		order.setAssignedToUserId(os.getAssignedToUserId());
		order.setRequestStatus(os.getRequestStatus());
		order.setCcUserId(ccUserId);

		orderService.saveOrderDetails(order);

		// Send SMS/Mail
		String msg = "";
		if (os.getRequestStatus().equals(GSGCommon.WORK_STS_CANCELED)) {
			msg += "Request " + orderId + " has been canceled.";
			smsUtility.sendStatusMessageToUser(msg, appUser.getContactNbr());
			
		}else if (os.getRequestStatus().equals(GSGCommon.WORK_STS_CLOSED)) {
			msg += "Please provide your feedback for Request "+ orderId +". Please contact our customer care for more information.";
			smsUtility.sendStatusMessageToUser(msg, appUser.getContactNbr());
			
		}else{
			AppUser svcEngrUser = appUserService.getByUserID(os.getAssignedToUserId());
			
			
			
			if (os.getRequestStatus().equals(GSGCommon.WORK_STS_WIP)) {
				msg += "Request " + orderId + " has been assigned to our Service Enginner " + svcEngrUser.getFirstName()
						+ "(" + svcEngrUser.getContactNbr() + "). Please reach out to our engineer for more information.";
				smsUtility.sendStatusMessageToUser(msg, appUser.getContactNbr());
			}
	
			if (os.getRequestStatus().equals(GSGCommon.WORK_STS_CREATED)
					&& !StringUtils.trimToEmpty(os.getAssignedToUserId()).isEmpty()) {
				msg += "Request " + orderId + "  has been assigned to you. Please verify the details and contact GSG Care for any clarification.";
	
				smsUtility.sendStatusMessageToUser(msg, svcEngrUser.getContactNbr());
			}
			
			if(os.getRequestStatus().equals(GSGCommon.WORK_STS_RESOLVED)){
				//send mail
				try{
					if(env.getProperty("app.env") != null && env.getProperty("app.env").equals("local"))
					{
						logger.info("skipping invoice for local");
						
					}
					else {
						reportService.sendInvoiceReport(order.getOrderId());
						logger.info("Invoice report sent for order: "+order.getOrderId());
					}
				}catch(Exception e){
					logger.error("Error occured while sending invoice report >>"+e.getMessage());
				}
			}
		}
		return order;
	}

	@Override
	public void updateRTdetailsToOrder(String orderId, String rtDetail) throws ResourceNotFoundException {
		logger.info("OrderServiceImpl.updateRTdetailsToOrder()");
		logger.info("RT Ticket Id :: " + rtDetail);

		Order order = orderService.getByOrderId(orderId);
		order.setRtTktId(rtDetail);
		// Saving Rt id to ticket
		orderService.saveOrder(order);
		// saveOrderDetails(order);

	}

	@Override
	public void removeServiceFromOrder(String orderDtlId, int postion) throws ResourceNotFoundException,
			GenericException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
		logger.info("ServiceModulesImpl.removeServiceFromOrder()");

		// 1. check if orderDtl is present and servicePosition is valid
		OrderDetails odtl = orderDetailService.getById(orderDtlId);
		Order order = orderService.getByOrderDtlId(orderDtlId);
		
		ServiceRequest sReq = (ServiceRequest) odtl.getProduct();
		List<Services> svcList = sReq.getServices();

		Services svc = null;
		try {
			svc = svcList.get(postion);
		}
		catch (Exception e) {
			throw new GenericException("Service Details not found.");
		}

		// 2. check if payment is COD then allow to remove service information
		if (!odtl.getTransactionMode().equals(GSGCommon.PAYMENT_COD) || !order.getRequestStatus().equals(GSGCommon.WORK_STS_WIP)) {
			throw new GenericException("Service can be removed only if the transaction mode is COD and request in progress.");
		}

		if (!ObjectUtils.isEmpty(svc)) {
			svcList.remove(svc);
		}

		// 3. Recalculate amount and update to orderDetail
		AppUser user = appUserService.getByUserID(odtl.getUserId());
		
		/*calculateRequestPriceUsingOffer(user, sReq);
		double calculateAmnt = 0, payableAmnt = 0;
		for(Services s:sReq.getServices()){
			calculateAmnt += s.getEffectivePrice();
			payableAmnt += s.getEffectivePrice();
		}

		odtl.setCalculatedPrice(calculateAmnt);
		odtl.setPayableAmount(payableAmnt);
		odtl.setTotalGst(payableAmnt - calculateAmnt);*/
		
		odtl.calculatePrices();
		
		orderDetailService.saveDetails(odtl);

		//update order total 
		
		order.setTotalPrice(calculateOrderTotalPrice(order, 0));
		orderService.saveOrder(order);
		// 4. Update to userscheme if any free is applied

		if (svc.isFreeApplied()) {

			SchemeData userSchemeData = null;

			// find appropriate scheme from user schemes
			for (SchemeData userSchme : user.getSchemes()) {
				// Select Based on the vehicle selected
				if (userSchme.getSchemeType().equals(sReq.getUsrVehicle().getVehicle().getSubType())) {
					userSchemeData = userSchme;
				}
			}

			List<SchemeService> userSchemeServices = userSchemeData.getSchemeServiceDtls();

			for (SchemeService ss : userSchemeServices) {
				if (ss.getServiceId().equals(svc.getServiceId())) {
					ss.setUsed(ss.getUsed() - 1);
				}
			}

			// save to user details
			appUserService.updateUser(user);

		}

	}

	private double calculateRequsetPrice(ServiceRequest sReq) {
		double totalAmount = 0;
		List<Services> svcList = sReq.getServices();
		
		for (Services svc : svcList) {
			// double price =
			// svc.getPrice().getTypePrice(sReq.getUsrVehicle().getVehicle().getType());
			// svc.setVehicleTypePrice(price);
			// svc.setTotalPrice(price*svc.getQuantity());
			// totalAmount += price*svc.getQuantity();
			
			String vehicleType = sReq.getUsrVehicle().getVehicle().getType();
			svc.calculatePrices(vehicleType);
			
			totalAmount += svc.getEffectivePrice();
			
		}

		return totalAmount;
	}

	@Override
	public Cart removeServiceFromCart(String cartId, int svcPostion) throws GenericException, ResourceNotFoundException,
			IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
		logger.info("ServiceModulesImpl.removeServiceFromCart()");

		// 1. check if orderDtl is present and servicePosition is valid
		Cart cart = cartService.getByCartId(cartId);

		ServiceRequest sReq = (ServiceRequest) cart.getProduct();
		List<Services> svcList = sReq.getServices();

		Services svc = null;
		try {
			svc = svcList.get(svcPostion);
		}
		catch (Exception e) {
			throw new GenericException("Service Details not found.");
		}

		// 2. remove the service information
		if (!ObjectUtils.isEmpty(svc)) {
			svcList.remove(svc);
		}
		//check if all items are deleted, then delete Cart
		if(svcList.size() == 0){
			cartService.removeFromCart(cartId);
			return null;
		}else{
		
			// Recalculate amount and update total amount of cart
			double totalAmount = calculateRequsetPrice(sReq);
			cart.setAmount(totalAmount);
			cartService.saveToCart(cart);
			
			return cart;
		}
		
	}
}
