package com.gsg.mongo.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gsg.mongo.model.master.VehicleData.Vehicle;

@Getter @Setter
public class UserVehicle {

	// private String vehicleId;
	@Field("regNbr")
	private String registrationNumber;
	private String modelVersion;

	private Vehicle vehicle = new Vehicle();

	private String variant;
	private int mfgYear;
	@Field("cc")
	private int cubicCapacity;
	private boolean insuranceValid;
	private String insuranceType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate expiryDate;


	public String getRegistrationNumber() {
		return registrationNumber;
	}


	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}


	public String getModelVersion() {
		return modelVersion;
	}


	public void setModelVersion(String modelVersion) {
		this.modelVersion = modelVersion;
	}


	public Vehicle getVehicle() {
		return vehicle;
	}


	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}


	public String getVariant() {
		return variant;
	}


	public void setVariant(String variant) {
		this.variant = variant;
	}


	public int getMfgYear() {
		return mfgYear;
	}


	public void setMfgYear(int mfgYear) {
		this.mfgYear = mfgYear;
	}


	public int getCubicCapacity() {
		return cubicCapacity;
	}


	public void setCubicCapacity(int cubicCapacity) {
		this.cubicCapacity = cubicCapacity;
	}


	public boolean isInsuranceValid() {
		return insuranceValid;
	}


	public void setInsuranceValid(boolean insuranceValid) {
		this.insuranceValid = insuranceValid;
	}


	public String getInsuranceType() {
		return insuranceType;
	}


	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}


	public LocalDate getExpiryDate() {
		return expiryDate;
	}


	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}


	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
