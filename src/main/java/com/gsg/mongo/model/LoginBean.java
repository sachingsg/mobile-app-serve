package com.gsg.mongo.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginBean {

	private String contactNbr;
	private String email;
	private String password;
	private String newPassword;
	private String otp;
	private String msg;
	private String status;
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
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	
}
