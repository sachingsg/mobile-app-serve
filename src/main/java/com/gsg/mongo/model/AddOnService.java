package com.gsg.mongo.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.gsg.mongo.model.master.Services;

@Getter @Setter
public class AddOnService {

	private String type;
	private String desc;
	private List<Services> serviceList = new ArrayList<Services>();
	private double price;
	private double gst;
	private int quantity = 1;

	// //////////////
	public AddOnService() {
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<Services> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<Services> serviceList) {
		this.serviceList = serviceList;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getGst() {
		return gst;
	}

	public void setGst(double gst) {
		this.gst = gst;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
