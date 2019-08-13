package com.gsg.mongo.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gsg.mongo.model.master.SchemeData;
import com.gsg.mongo.model.master.Services;
import com.gsg.utilities.GSGCommon;

@Document(collection = "orderdtl")
@TypeAlias("OrderDetails")
@Getter @Setter
public class OrderDetails extends Auditable {

	@Id
	private String id;
	private String userId;
	private String orderId;
	private String produtType;
	private Object product;
	private String transactionStatus; // payment status
	private String transactionMode;
	private String posReceiptNbr;

	private double totalGst;
	
	private double discount; // discount if anything
	private double calculatedPrice; // without tax
	private double payableAmount; // with tax
	private double roundedAmount;
	private double receivedAmount;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime transactionDate;
	private String transactionId;

	// Work Status // Created,In progress,Completed

	// /////////////
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getProdutType() {
		return produtType;
	}

	public void setProdutType(String produtType) {
		this.produtType = produtType;
	}

	public Object getProduct() {
		return product;
	}

	public void setProduct(Object product) {
		this.product = product;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getTransactionMode() {
		return transactionMode;
	}

	public void setTransactionMode(String transactionMode) {
		this.transactionMode = transactionMode;
	}

	public String getPosReceiptNbr() {
		return posReceiptNbr;
	}

	public void setPosReceiptNbr(String posReceiptNbr) {
		this.posReceiptNbr = posReceiptNbr;
	}

	public double getTotalGst() {
		return totalGst;
	}

	public void setTotalGst(double totalGst) {
		this.totalGst = totalGst;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getCalculatedPrice() {
		return calculatedPrice;
	}

	public void setCalculatedPrice(double calculatedPrice) {
		this.calculatedPrice = calculatedPrice;
	}

	public double getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(double payableAmount) {
		this.payableAmount = payableAmount;
	}

	public double getRoundedAmount() {
		return roundedAmount;
	}

	public void setRoundedAmount(double roundedAmount) {
		this.roundedAmount = roundedAmount;
	}

	public double getReceivedAmount() {
		return receivedAmount;
	}

	public void setReceivedAmount(double receivedAmount) {
		this.receivedAmount = receivedAmount;
	}

	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public void calculatePrices() {

		this.calculatedPrice = 0;
		this.payableAmount = 0;
		this.roundedAmount = 0;
		this.receivedAmount = 0;
		this.totalGst = 0;
		this.discount = 0;

		if (this.produtType.equals(GSGCommon.TYPE_SCHEME)) {
			SchemeData sd = (SchemeData) this.product;
			this.calculatedPrice = sd.getPrice();
			this.payableAmount = sd.getPrice() * sd.getGst();
		} else if (this.produtType.equals(GSGCommon.TYPE_SERVICE)) {
			ServiceRequest sReq = (ServiceRequest) this.product;
			if (sReq.getServices().size() > 0) {
				for (Services svc : sReq.getServices()) {
					this.calculatedPrice += svc.getEffectivePrice();
					this.payableAmount += svc.getEffectivePrice();
					this.discount += svc.getDiscount();
				}
			}
		}
		//this.totalGst += this.payableAmount - this.calculatedPrice;
		this.roundedAmount = GSGCommon.roundedValue(this.payableAmount);
	}
}
