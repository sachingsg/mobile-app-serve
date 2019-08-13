package com.gsg.mongo.model;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import com.gsg.mongo.model.AppUser.AddressBook;

@Document(collection = "office")
@TypeAlias("OfficeDetails")
@Getter
@Setter
public class OfficeDetails {

	@Id
	private String id;
	private String companyName;
	private String gstin;
	private AddressBook address = new AddressBook();
	private BankDetails bankdetails = new BankDetails();

	@Getter
	@Setter
	public static class BankDetails {
		private String bankName;
		private String branchName;
		private String ifsc;
		private String account;
		public String getBankName() {
			return bankName;
		}
		public void setBankName(String bankName) {
			this.bankName = bankName;
		}
		public String getBranchName() {
			return branchName;
		}
		public void setBranchName(String branchName) {
			this.branchName = branchName;
		}
		public String getIfsc() {
			return ifsc;
		}
		public void setIfsc(String ifsc) {
			this.ifsc = ifsc;
		}
		public String getAccount() {
			return account;
		}
		public void setAccount(String account) {
			this.account = account;
		}
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getGstin() {
		return gstin;
	}

	public void setGstin(String gstin) {
		this.gstin = gstin;
	}

	public AddressBook getAddress() {
		return address;
	}

	public void setAddress(AddressBook address) {
		this.address = address;
	}

	public BankDetails getBankdetails() {
		return bankdetails;
	}

	public void setBankdetails(BankDetails bankdetails) {
		this.bankdetails = bankdetails;
	}

}
