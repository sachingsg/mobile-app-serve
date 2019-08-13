package com.gsg.mongo.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gsg.mongo.model.AppUser.MapLocation;
import com.gsg.mongo.model.master.Services;


@Getter @Setter
public class ServiceRequest {
	private String userId;
	private MapLocation location = new MapLocation();

	// list of service is required as the full object will be dumped into order
	// details
	private List<Services> services = new ArrayList<Services>();

	private UserVehicle usrVehicle = new UserVehicle(); // userVehicle is
														// required in case
														// insurance
	private String serviceType; // Normal, emergency

	private boolean useUserScheme;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate serviceDate = LocalDate.now();

	// private String schemeId;
	// //////////////////

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public MapLocation getLocation() {
		return location;
	}

	public void setLocation(MapLocation location) {
		this.location = location;
	}

	public List<Services> getServices() {
		return services;
	}

	public void setServices(List<Services> services) {
		this.services = services;
	}

	public UserVehicle getUsrVehicle() {
		return usrVehicle;
	}

	public void setUsrVehicle(UserVehicle usrVehicle) {
		this.usrVehicle = usrVehicle;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public boolean isUseUserScheme() {
		return useUserScheme;
	}

	public void setUseUserScheme(boolean useUserScheme) {
		this.useUserScheme = useUserScheme;
	}

	public LocalDate getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(LocalDate serviceDate) {
		this.serviceDate = serviceDate;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
