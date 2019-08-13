package com.gsg.mongo.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderStatus {

	private String orderId;
	private String assignedQueue;
	private String assignedToUserId;
	private String requestStatus;

	// user details
	private String userId;
	private String userName;
	private String contactNbr;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getAssignedQueue() {
		return assignedQueue;
	}
	public void setAssignedQueue(String assignedQueue) {
		this.assignedQueue = assignedQueue;
	}
	public String getAssignedToUserId() {
		return assignedToUserId;
	}
	public void setAssignedToUserId(String assignedToUserId) {
		this.assignedToUserId = assignedToUserId;
	}
	public String getRequestStatus() {
		return requestStatus;
	}
	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getContactNbr() {
		return contactNbr;
	}
	public void setContactNbr(String contactNbr) {
		this.contactNbr = contactNbr;
	}
	
}
