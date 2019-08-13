package com.gsg.mongo.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.gsg.constants.AppUserConst;
import com.gsg.error.GenericException;
import com.gsg.mongo.model.master.SchemeData;
import com.ibm.icu.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "appuser")
@TypeAlias("AppUser")
@JsonInclude(Include.NON_NULL)
@Getter
@Setter
@ToString
public class AppUser extends Auditable {

	@Id
	@JsonIgnore
	private String id;
	private String userId;
	private String email;
	private String firstName;
	private String middleName;
	private String lastName;
	private String password;
	private String gender;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate dob;
	private String maritalStatus;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate anniversaryDate;
	private String contactNbr;
	private String altenateContactNbr;
	private String deviceId;
	private List<String> roles = new ArrayList<>();
	private boolean isActive;
	private String userAcceptance = "N";
	
	private List<AddressBook> address = new ArrayList<>();
	private List<UserVehicle> userVehicles = new ArrayList<>();

	private double[] userLocation;
	
	private String referralCode;
	// @DBRef(lazy = false)
	private List<SchemeData> schemes = new ArrayList<>();

	private ServiceArea serviceArea = new ServiceArea();
	
	
	// Workshop owner
	private String wName ;
//	private String wEmail ;
//	private String wMobile ;
	private float wAge;
	private List<String> wVehicleBrandsRepaired;
	private int wOtherBranchNumber;
	private List<String> wOAddress;
	private int wAvgCarsRptDly;
	private double wMRevenue;
	private int wSMechs;
	private int wMechs;
	private int wHelpers;
	private int wWashers;
	private List<String> wVehicleTypesRepaired;
	private List<String> wServicesProvided;
	private List<String> wFacilities;
	private Date appointmentDate;
	private String appointmentTime;
	private String wsCreatedBy; // get UserId created workshop
	private String wsStatus;
	private List<WsDoc> wsDocs;
	private double[] coordinates;
	private String primaryGet;
	private String[] asstGet;
	private int defaultRating;
	
	
	
	
	
	
	public int getDefaultRating() {
		return defaultRating;
	}
	public void setDefaultRating(int defaultRating) {
		this.defaultRating = defaultRating;
	}
	public String getPrimaryGet() {
		return primaryGet;
	}
	public void setPrimaryGet(String primaryGet) {
		this.primaryGet = primaryGet;
	}
	public String[] getAsstGet() {
		return asstGet;
	}
	public void setAsstGet(String[] asstGet) {
		this.asstGet = asstGet;
	}
	public double[] getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(double[] coordinates) {
		this.coordinates = coordinates;
	}
	public List<WsDoc> getWsDocs() {
		return wsDocs;
	}
	public void setWsDocs(List<WsDoc> wsDocs) {
		this.wsDocs = wsDocs;
	}
	public List<String> getwOAddress() {
		return wOAddress;
	}
	public void setwOAddress(List<String> wOAddress) {
		this.wOAddress = wOAddress;
	}
	public int getwSMechs() {
		return wSMechs;
	}
	public void setwSMechs(int wSMechs) {
		this.wSMechs = wSMechs;
	}
	public int getwMechs() {
		return wMechs;
	}
	public void setwMechs(int wMechs) {
		this.wMechs = wMechs;
	}
	public int getwHelpers() {
		return wHelpers;
	}
	public void setwHelpers(int wHelpers) {
		this.wHelpers = wHelpers;
	}
	public int getwWashers() {
		return wWashers;
	}
	public void setwWashers(int wWashers) {
		this.wWashers = wWashers;
	}
	public String getWsStatus() {
		return wsStatus;
	}
	public void setWsStatus(String wsStatus) {
		this.wsStatus = wsStatus;
	}
	public String getWsCreatedBy() {
		return wsCreatedBy;
	}
	public void setWsCreatedBy(String wsCreatedBy) {
		this.wsCreatedBy = wsCreatedBy;
	}
	public AppUser(String email, String contactNbr, String password) {
		this.email = email;
		this.password = password;
		this.contactNbr = contactNbr;
	}
	public AppUser(String email, String contactNbr, String password,List<String> roles ) {
		this.email = email;
		this.password = password;
		this.contactNbr = contactNbr;
		this.roles = roles;
	}

	public AppUser() {
		defaultRating = 5;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public LocalDate getAnniversaryDate() {
		return anniversaryDate;
	}

	public void setAnniversaryDate(LocalDate anniversaryDate) {
		this.anniversaryDate = anniversaryDate;
	}

	public String getContactNbr() {
		return contactNbr;
	}

	public void setContactNbr(String contactNbr) {
		this.contactNbr = contactNbr;
	}

	public String getAltenateContactNbr() {
		return altenateContactNbr;
	}

	public void setAltenateContactNbr(String altenateContactNbr) {
		this.altenateContactNbr = altenateContactNbr;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getUserAcceptance() {
		return userAcceptance;
	}

	public void setUserAcceptance(String userAcceptance) {
		this.userAcceptance = userAcceptance;
	}

	public List<AddressBook> getAddress() {
		return address;
	}

	public void setAddress(List<AddressBook> address) {
		this.address = address;
	}

	public List<UserVehicle> getUserVehicles() {
		return userVehicles;
	}

	public void setUserVehicles(List<UserVehicle> userVehicles) {
		this.userVehicles = userVehicles;
	}

	public double[] getUserLocation() {
		return userLocation;
	}

	public void setUserLocation(double[] userLocation) {
		this.userLocation = userLocation;
	}

	public String getReferralCode() {
		return referralCode;
	}

	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}

	public List<SchemeData> getSchemes() {
		return schemes;
	}

	public void setSchemes(List<SchemeData> schemes) {
		this.schemes = schemes;
	}

	public ServiceArea getServiceArea() {
		return serviceArea;
	}

	public void setServiceArea(ServiceArea serviceArea) {
		this.serviceArea = serviceArea;
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
	
	
	public int getwOtherBranchNumber() {
		return wOtherBranchNumber;
	}
	public void setwOtherBranchNumber(int wOtherBranchNumber) {
		this.wOtherBranchNumber = wOtherBranchNumber;
	}
	public String getAppointmentTime() {
		return appointmentTime;
	}
	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}
	public int getwAvgCarsRptDly() {
		return wAvgCarsRptDly;
	}
	public void setwAvgCarsRptDly(int wAvgCarsRptDly) {
		this.wAvgCarsRptDly = wAvgCarsRptDly;
	}
	public double getwMRevenue() {
		return wMRevenue;
	}
	public void setwMRevenue(double wMRevenue) {
		this.wMRevenue = wMRevenue;
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
//	public MapLocation getwLocation() {
//		return wLocation;
//	}
//	public void setwLocation(MapLocation wLocation) {
//		this.wLocation = wLocation;
//	}

	
	@JsonInclude(Include.NON_EMPTY)
	@Getter
	@Setter
	@ToString
	public static class WsDoc{
		private String docType;
		private String docStatus;
		private String reviewer;
		private String comment;
		private int udatedBy;
		
		public String getDocType() {
			return docType;
		}
		public void setDocType(String docType) {
			this.docType = docType;
		}
		public String getDocStatus() {
			return docStatus;
		}
		public void setDocStatus(String docStatus) {
			this.docStatus = docStatus;
		}
		public String getReviewer() {
			return reviewer;
		}
		public void setReviewer(String reviewer) {
			this.reviewer = reviewer;
		}
		public String getComment() {
			return comment;
		}
		public void setComment(String comment) {
			this.comment = comment;
		}
		
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

	@JsonInclude(Include.NON_EMPTY)
	@Getter
	@Setter
	@ToString
	public static class ServiceArea {
		private String state;
		private String district;
		private String location;
		private String subLocation;
		private MapLocation mapLocation;

		public ServiceArea() {
		}
	}

	@Getter
	@Setter
	public static class MapLocation {
		
		private double lng = 0;
		private double lat = 0;

		public boolean empty() {
			if (this.lat == 0 && this.lng == 0) 
				return true;
			return false;
		}

		public double getLng() {
			return lng;
		}

		public void setLng(double lng) {
			this.lng = lng;
		}

		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		@Override
		@JsonIgnore
		public String toString() {
			return this.lat + "," + this.lng;
		}

	}

	

}
