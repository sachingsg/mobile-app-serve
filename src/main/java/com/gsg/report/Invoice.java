package com.gsg.report;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.gsg.mongo.model.AppUser;
import com.gsg.mongo.model.OfficeDetails.BankDetails;
import com.gsg.mongo.model.Order;
import com.gsg.mongo.model.master.SchemeData;
import com.gsg.mongo.model.master.Services;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class Invoice {

	private String gstin;
	private String companyName;
	private String companyAddress;
	private String ofcStateCd;
	private String placeOfSupply;
	
	private BankDetails bank;
	
	private LocalDateTime invoiceDate = LocalDateTime.now();
	// Data
	Order order;
	AppUser appUser;
	
	private String customerName;
	private String customerAddress;
	private String customerState;
	private String customerStateCd;
	private String customerId;
	
	private String vehicleType;
	private String vehicleRegdNbr;
	
	private String productType;
	private String orderId;
	private String invoiceId;
	private List<Services> serviceList = new ArrayList<>();
	private double grandTotal = 0.0;
	private double totalDiscount;
	
	private List<SchemeData> schemeList = new ArrayList<>();
	
	private LocalDateTime creationDate;

	public String getGstin() {
		return gstin;
	}

	public void setGstin(String gstin) {
		this.gstin = gstin;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getOfcStateCd() {
		return ofcStateCd;
	}

	public void setOfcStateCd(String ofcStateCd) {
		this.ofcStateCd = ofcStateCd;
	}

	public String getPlaceOfSupply() {
		return placeOfSupply;
	}

	public void setPlaceOfSupply(String placeOfSupply) {
		this.placeOfSupply = placeOfSupply;
	}

	public BankDetails getBank() {
		return bank;
	}

	public void setBank(BankDetails bank) {
		this.bank = bank;
	}

	public LocalDateTime getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(LocalDateTime invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getCustomerState() {
		return customerState;
	}

	public void setCustomerState(String customerState) {
		this.customerState = customerState;
	}

	public String getCustomerStateCd() {
		return customerStateCd;
	}

	public void setCustomerStateCd(String customerStateCd) {
		this.customerStateCd = customerStateCd;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getVehicleRegdNbr() {
		return vehicleRegdNbr;
	}

	public void setVehicleRegdNbr(String vehicleRegdNbr) {
		this.vehicleRegdNbr = vehicleRegdNbr;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public List<Services> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<Services> serviceList) {
		this.serviceList = serviceList;
	}

	public double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public List<SchemeData> getSchemeList() {
		return schemeList;
	}

	public void setSchemeList(List<SchemeData> schemeList) {
		this.schemeList = schemeList;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}

	
	
}
