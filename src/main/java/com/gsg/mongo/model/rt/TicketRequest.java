package com.gsg.mongo.model.rt;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class TicketRequest {

	private String Subject;
	private String Queue;
	private String Owner;
	private String Status;
	private String Priority;
	private String Comment;
	private CustomFields CustomFields;

	//
	public String getSubject() {
		return Subject;
	}

	@JsonProperty("Subject")
	public void setSubject(String subject) {
		Subject = subject;
	}

	public String getQueue() {
		return Queue;
	}

	@JsonProperty("Queue")
	public void setQueue(String queue) {
		Queue = queue;
	}

	public String getOwner() {
		return Owner;
	}

	@JsonProperty("Owner")
	public void setOwner(String owner) {
		Owner = owner;
	}

	public String getStatus() {
		return Status;
	}

	@JsonProperty("Status")
	public void setStatus(String status) {
		Status = status;
	}

	public String getPriority() {
		return Priority;
	}

	@JsonProperty("Priority")
	public void setPriority(String priority) {
		Priority = priority;
	}

	public String getComment() {
		return Comment;
	}

	@JsonProperty("Comment")
	public void setComment(String comment) {
		Comment = comment;
	}

	public CustomFields getCustomFields() {
		return CustomFields;
	}

	@JsonProperty("CustomFields")
	public void setCustomFields(CustomFields customFields) {
		CustomFields = customFields;
	}

	@Override
	public String toString() {
		return "TicketRequest [Subject=" + Subject + ", Queue=" + Queue + ", Owner=" + Owner + ", Status=" + Status
				+ ", Priority=" + Priority + ", Comment=" + Comment + ", CustomFields=" + CustomFields + "]";
	}
	
	@JsonInclude(Include.NON_NULL)
	public static class CustomFields {
		private String Location;
		private List<String> Services = new ArrayList<String>();
		private String Vehicle;

		public String getLocation() {
			return Location;
		}

		@JsonProperty("Location")
		public void setLocation(String location) {
			Location = location;
		}

		public List<String> getServices() {
			return Services;
		}

		@JsonProperty("Services")
		public void setServices(List<String> services) {
			Services = services;
		}

		public String getVehicle() {
			return Vehicle;
		}

		@JsonProperty("Vehicle")
		public void setVehicle(String vehicle) {
			Vehicle = vehicle;
		}

	}

}
