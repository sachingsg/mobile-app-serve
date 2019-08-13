package com.gsg.mongo.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tax")
public class Tax {

	private double sgstPercentage;
	private double cgstPercentage;
	private double igstPercentage;

	public double getSgstPercentage() {
		return sgstPercentage;
	}

	public void setSgstPercentage(double sgstPercentage) {
		this.sgstPercentage = sgstPercentage;
	}

	public double getCgstPercentage() {
		return cgstPercentage;
	}

	public void setCgstPercentage(double cgstPercentage) {
		this.cgstPercentage = cgstPercentage;
	}

	public double getIgstPercentage() {
		return igstPercentage;
	}

	public void setIgstPercentage(double igstPercentage) {
		this.igstPercentage = igstPercentage;
	}
}
