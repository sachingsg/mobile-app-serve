package com.gsg.mongo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@Getter @Setter
public class OrderCheckout {

	private String isPayable = "NO";
	private String productType;

	private String referenceno;
	private double amount;

	private double payableAmount;

	// user details
	private String userId;

	private List<ProductInfo> productInfo = new ArrayList<ProductInfo>();

	// for request
	private String codUrl;
	private String pgUrl;

	private String tranMode;

	private String tranId;
	private String tranSts;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime tranDate;

	// tax
	private String sgst;
	private String cgst;
	private String igst;
	private double totalGst;
	private double roundedAmount;

	// ////////////
	public OrderCheckout() {
		// TODO Auto-generated constructor stub
	}

	// //////////////


	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String getIsPayable() {
		return isPayable;
	}

	public void setIsPayable(String isPayable) {
		this.isPayable = isPayable;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getReferenceno() {
		return referenceno;
	}

	public void setReferenceno(String referenceno) {
		this.referenceno = referenceno;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(double payableAmount) {
		this.payableAmount = payableAmount;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<ProductInfo> getProductInfo() {
		return productInfo;
	}

	public void setProductInfo(List<ProductInfo> productInfo) {
		this.productInfo = productInfo;
	}

	public String getCodUrl() {
		return codUrl;
	}

	public void setCodUrl(String codUrl) {
		this.codUrl = codUrl;
	}

	public String getPgUrl() {
		return pgUrl;
	}

	public void setPgUrl(String pgUrl) {
		this.pgUrl = pgUrl;
	}

	public String getTranMode() {
		return tranMode;
	}

	public void setTranMode(String tranMode) {
		this.tranMode = tranMode;
	}

	public String getTranId() {
		return tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

	public String getTranSts() {
		return tranSts;
	}

	public void setTranSts(String tranSts) {
		this.tranSts = tranSts;
	}

	public LocalDateTime getTranDate() {
		return tranDate;
	}

	public void setTranDate(LocalDateTime tranDate) {
		this.tranDate = tranDate;
	}

	public String getSgst() {
		return sgst;
	}

	public void setSgst(String sgst) {
		this.sgst = sgst;
	}

	public String getCgst() {
		return cgst;
	}

	public void setCgst(String cgst) {
		this.cgst = cgst;
	}

	public String getIgst() {
		return igst;
	}

	public void setIgst(String igst) {
		this.igst = igst;
	}

	public double getTotalGst() {
		return totalGst;
	}

	public void setTotalGst(double totalGst) {
		this.totalGst = totalGst;
	}

	public double getRoundedAmount() {
		return roundedAmount;
	}

	public void setRoundedAmount(double roundedAmount) {
		this.roundedAmount = roundedAmount;
	}

	@Getter @Setter
	public static class ProductInfo {
		private String productDesc;
		private double amount;
		private boolean freeApplied;

		private double gst;
		private double amountAfterTax;

		// ////////
		public ProductInfo() {
			// TODO Auto-generated constructor stub
		}

		/*
		 * public ProductInfo(String productDesc, double amount, boolean
		 * freeApplied) { this.productDesc = productDesc; this.amount = amount;
		 * this.freeApplied = freeApplied; }
		 */

		public ProductInfo(String productDesc, double amount, boolean freeApplied, double gst, double amountAfterTax) {
			super();
			this.productDesc = productDesc;
			this.amount = amount;
			this.freeApplied = freeApplied;
			this.gst = gst;
			this.amountAfterTax = amountAfterTax;
		}

		public String getProductDesc() {
			return productDesc;
		}

		public void setProductDesc(String productDesc) {
			this.productDesc = productDesc;
		}

		public double getAmount() {
			return amount;
		}

		public void setAmount(double amount) {
			this.amount = amount;
		}

		public boolean isFreeApplied() {
			return freeApplied;
		}

		public void setFreeApplied(boolean freeApplied) {
			this.freeApplied = freeApplied;
		}

		public double getGst() {
			return gst;
		}

		public void setGst(double gst) {
			this.gst = gst;
		}

		public double getAmountAfterTax() {
			return amountAfterTax;
		}

		public void setAmountAfterTax(double amountAfterTax) {
			this.amountAfterTax = amountAfterTax;
		}


	}

}
