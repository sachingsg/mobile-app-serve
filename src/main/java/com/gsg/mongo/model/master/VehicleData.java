package com.gsg.mongo.model.master;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vehicle")
@TypeAlias("Vehicle")
@Getter @Setter
public class VehicleData {

	@Id
	private String make;
	private List<Vehicle> vehicles = new ArrayList<Vehicle>();

	public VehicleData() {
		// TODO Auto-generated constructor stub
	}

	public VehicleData(String make) {
		super();
		this.make = make;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((make == null) ? 0 : make.hashCode());
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
		VehicleData other = (VehicleData) obj;
		if (make == null) {
			if (other.make != null)
				return false;
		} else if (!make.equals(other.make))
			return false;
		return true;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	// Inner Class
	@Getter @Setter
	public static class Vehicle {
		private String make;
		private String models;
		private String type;
		private String subType;
		private String wheels;

		public Vehicle() {
			// TODO Auto-generated constructor stub
		}

		public Vehicle(String[] data) {
			super();
			this.make = data[0];
			this.models = data[1];
			this.type = data[2];
			this.subType = data[3];
			this.wheels = data[4];
		}

			public String getMake() {
			return make;
		}

		public void setMake(String make) {
			this.make = make;
		}

		public String getModels() {
			return models;
		}

		public void setModels(String models) {
			this.models = models;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getSubType() {
			return subType;
		}

		public void setSubType(String subType) {
			this.subType = subType;
		}

		public String getWheels() {
			return wheels;
		}

		public void setWheels(String wheels) {
			this.wheels = wheels;
		}

			@Override
		public String toString() {
			return "[make=" + make + ", models=" + models + ", type=" + type + ", subType=" + subType + ", wheels="
					+ wheels + "]";
		}

		public boolean isEmpty() {
			if (this.make == null || this.make.isEmpty()) {
				return true;
			}

			if (this.models == null || this.models.isEmpty()) {
				return true;
			}
			return false;

		}

	}
}
