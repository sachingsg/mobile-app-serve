package com.gsg.mongo.model.master;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gsg.mongo.model.Auditable;
import com.gsg.utilities.GSGCommon;

@Document(collection = "service")
@TypeAlias("Services")
@Getter
@Setter
public class Services extends Auditable {

	@Id
	@JsonIgnore
	private String id;
	private String serviceId;
	private String serviceCd;
	private String category;
	private String subCategory;
	private boolean each;

	private boolean freeApplied;

	private double gst; // tax
	private Price price; // all types of price including tax

	private double discount; // discount if anything
	
	private double vehicleTypePrice; // price as per vehicle/per item including tax
	private double retailPrice; // price per item excluding tax
	private int quantity;
	private double totalPrice; // price for all item without offer

	// After offer
	private double effectivePrice; // price after offer
	private double effectivePriceWithoutTax; // price without tax

	public Services() {
		
	}

	public Services(String category, String subCategory, double price, int quantity, double gst) {
		this.category = category;
		this.subCategory = subCategory;
		this.gst = gst;
		this.price = new Price(price);
		this.quantity = quantity;
	}

	public Services(String serviceCd) {
		super();
		this.serviceCd = serviceCd;
	}

	public Services(String serviceCd, String category, String subCategory, boolean isEach, double smallSegPrice,
			double mediumSegPrice, double luxurySegPrice, int quantity, double gst) {
		this.serviceCd = serviceCd;
		this.category = category;
		this.subCategory = subCategory;
		this.each = isEach;
		this.quantity = quantity;
		this.gst = gst;
		this.price = new Price(smallSegPrice, mediumSegPrice, luxurySegPrice);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceCd() {
		return serviceCd;
	}

	public void setServiceCd(String serviceCd) {
		this.serviceCd = serviceCd;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public boolean isEach() {
		return each;
	}

	public void setEach(boolean each) {
		this.each = each;
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

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getVehicleTypePrice() {
		return vehicleTypePrice;
	}

	public void setVehicleTypePrice(double vehicleTypePrice) {
		this.vehicleTypePrice = vehicleTypePrice;
	}

	public double getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(double retailPrice) {
		this.retailPrice = retailPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public double getEffectivePrice() {
		return effectivePrice;
	}

	public void setEffectivePrice(double effectivePrice) {
		this.effectivePrice = effectivePrice;
	}

	public double getEffectivePriceWithoutTax() {
		return effectivePriceWithoutTax;
	}

	public void setEffectivePriceWithoutTax(double effectivePriceWithoutTax) {
		this.effectivePriceWithoutTax = effectivePriceWithoutTax;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((serviceCd == null) ? 0 : serviceCd.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Services other = (Services) obj;
		if (serviceCd == null) {
			if (other.serviceCd != null)
				return false;
		} else if (!serviceCd.equals(other.serviceCd))
			return false;
		return true;
	}
	
	
	public void calculatePrices(String vehicleType) {
		this.vehicleTypePrice = this.price.getTypePrice(vehicleType); //with tax
		this.retailPrice = GSGCommon.getRetailPrice(this.vehicleTypePrice,this.gst); //without tax
		
		this.totalPrice = GSGCommon.getTwoPrecisionDoubleValue(this.vehicleTypePrice * this.quantity); // without offer
		
		this.effectivePrice = totalPrice;
		if(this.freeApplied){
			this.effectivePrice = 0;
		}
		
		this.effectivePriceWithoutTax = //GSGCommon.getTwoPrecisionDoubleValue(this.effectivePrice * (1 - (this.gst / 100))); 
				GSGCommon.getRetailPrice(this.effectivePrice,this.gst); 

	}

	@Getter
	@Setter
	public static class Price {
		private double small;
		private double medium;
		private double luxury;

		public double getTypePrice(String type) {
			type = StringUtils.trimToEmpty(type);
			if (type.equals("luxury")) {
				return luxury;
			} else if (type.equals("medium")) {
				return medium;
			} else {
				return small;
			}
		}

		public Price() {
		}

		public Price(double small, double medium, double luxury) {
			this.small = small;
			this.medium = medium;
			this.luxury = luxury;
		}

		public Price(double price) {
			this.small = price;
			this.medium = price;
			this.luxury = price;
		}

	}
}
