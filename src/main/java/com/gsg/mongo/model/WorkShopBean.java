package com.gsg.mongo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.gsg.mongo.model.AppUser.AddressBook;
import com.gsg.mongo.model.AppUser.MapLocation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
public class WorkShopBean {

	private String contactNbr;
	private String email;
	private String password;
	private String newPassword;
	private String msg;
	
	// workshop specific property
	private String wName ;
	private float wAge;
	private List<String> wVehicleBrandsRepaired;
	private List<String> wOtherBranchNumber;
	private List<AddressBook> wOAddress = new ArrayList<>();
	private double wAvgCarsRptDly;
	private double wMRevenue;
	private List<String> wSMechs;
	private List<String> wMechs;
	private List<String> wHelpers;
	private List<String> wWashers;
	private List<String> wVehicleTypesRepaired;
	private List<String> wServicesProvided;
	private List<String> wFacilities;
	private Date appointmentDate;
	private MapLocation wLocation;
	public String getContactNbr() {
		return contactNbr;
	}
	public void setContactNbr(String contactNbr) {
		this.contactNbr = contactNbr;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getwName() {
		return wName;
	}
	public void setwName(String wName) {
		this.wName = wName;
	}
	public float getwAge() {
		return wAge;
	}
	public void setwAge(float wAge) {
		this.wAge = wAge;
	}
	public List<String> getwVehicleBrandsRepaired() {
		return wVehicleBrandsRepaired;
	}
	public void setwVehicleBrandsRepaired(List<String> wVehicleBrandsRepaired) {
		this.wVehicleBrandsRepaired = wVehicleBrandsRepaired;
	}
	public List<String> getwOtherBranchNumber() {
		return wOtherBranchNumber;
	}
	public void setwOtherBranchNumber(List<String> wOtherBranchNumber) {
		this.wOtherBranchNumber = wOtherBranchNumber;
	}
	public List<AddressBook> getwOAddress() {
		return wOAddress;
	}
	public void setwOAddress(List<AddressBook> wOAddress) {
		this.wOAddress = wOAddress;
	}
	public double getwAvgCarsRptDly() {
		return wAvgCarsRptDly;
	}
	public void setwAvgCarsRptDly(double wAvgCarsRptDly) {
		this.wAvgCarsRptDly = wAvgCarsRptDly;
	}
	public double getwMRevenue() {
		return wMRevenue;
	}
	public void setwMRevenue(double wMRevenue) {
		this.wMRevenue = wMRevenue;
	}
	public List<String> getwSMechs() {
		return wSMechs;
	}
	public void setwSMechs(List<String> wSMechs) {
		this.wSMechs = wSMechs;
	}
	public List<String> getwMechs() {
		return wMechs;
	}
	public void setwMechs(List<String> wMechs) {
		this.wMechs = wMechs;
	}
	public List<String> getwHelpers() {
		return wHelpers;
	}
	public void setwHelpers(List<String> wHelpers) {
		this.wHelpers = wHelpers;
	}
	public List<String> getwWashers() {
		return wWashers;
	}
	public void setwWashers(List<String> wWashers) {
		this.wWashers = wWashers;
	}
	public List<String> getwVehicleTypesRepaired() {
		return wVehicleTypesRepaired;
	}
	public void setwVehicleTypesRepaired(List<String> wVehicleTypesRepaired) {
		this.wVehicleTypesRepaired = wVehicleTypesRepaired;
	}
	public List<String> getwServicesProvided() {
		return wServicesProvided;
	}
	public void setwServicesProvided(List<String> wServicesProvided) {
		this.wServicesProvided = wServicesProvided;
	}
	public List<String> getwFacilities() {
		return wFacilities;
	}
	public void setwFacilities(List<String> wFacilities) {
		this.wFacilities = wFacilities;
	}
	public Date getAppointmentDate() {
		return appointmentDate;
	}
	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}
	public MapLocation getwLocation() {
		return wLocation;
	}
	public void setwLocation(MapLocation wLocation) {
		this.wLocation = wLocation;
	}

	@JsonInclude(Include.NON_EMPTY)
	@Getter
	@Setter
	@ToString
	public static class AddressBook {

		private String houseNbr;
		private String apartmentName;
		private String street;
		private String locality;
		private String landmark;
		

		private String city;
		private String district;
		private String po_ps_name;
		private String zip;
		private String state;
		private String stateCd;
		private String country;
		private MapLocation location;

		public AddressBook() {
			//
		}
		public String getLandmark() {
			return landmark;
		}

		public void setLandmark(String landmark) {
			this.landmark = landmark;
		}
		public String getHouseNbr() {
			return houseNbr;
		}

		public void setHouseNbr(String houseNbr) {
			this.houseNbr = houseNbr;
		}

		public String getApartmentName() {
			return apartmentName;
		}

		public void setApartmentName(String apartmentName) {
			this.apartmentName = apartmentName;
		}

		public String getStreet() {
			return street;
		}

		public void setStreet(String street) {
			this.street = street;
		}

		public String getLocality() {
			return locality;
		}

		public void setLocality(String locality) {
			this.locality = locality;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getDistrict() {
			return district;
		}

		public void setDistrict(String district) {
			this.district = district;
		}

		public String getPo_ps_name() {
			return po_ps_name;
		}

		public void setPo_ps_name(String po_ps_name) {
			this.po_ps_name = po_ps_name;
		}

		public String getZip() {
			return zip;
		}

		public void setZip(String zip) {
			this.zip = zip;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getStateCd() {
			return stateCd;
		}

		public void setStateCd(String stateCd) {
			this.stateCd = stateCd;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public MapLocation getLocation() {
			return location;
		}

		public void setLocation(MapLocation location) {
			this.location = location;
		}

		@JsonIgnore
		public String getAddressFull() {
			StringBuilder add = new StringBuilder();
			add.append(StringUtils.trimToEmpty(houseNbr).isEmpty()?"":StringUtils.trimToEmpty(houseNbr)+", ")
			.append(StringUtils.trimToEmpty(apartmentName).isEmpty()?"":StringUtils.trimToEmpty(apartmentName)+", ")
			.append(StringUtils.trimToEmpty(street).isEmpty()?"":StringUtils.trimToEmpty(street)+", ")
			.append(StringUtils.trimToEmpty(locality).isEmpty()?"":StringUtils.trimToEmpty(locality)+", ")
			.append(StringUtils.trimToEmpty(po_ps_name).isEmpty()?"":StringUtils.trimToEmpty(po_ps_name)+", ")
			.append(StringUtils.trimToEmpty(city).isEmpty()?"":StringUtils.trimToEmpty(city)+", ")
			.append(StringUtils.trimToEmpty(state).isEmpty()?"":StringUtils.trimToEmpty(state)+", ")
			.append(StringUtils.trimToEmpty(country).isEmpty()?"":StringUtils.trimToEmpty(country))
			.append(StringUtils.trimToEmpty(landmark).isEmpty()?"":StringUtils.trimToEmpty(landmark));
			return add.toString();
		}

	}
	

	
}
