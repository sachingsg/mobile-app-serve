package com.gsg.mongo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodPayment {

	private String codMode = "CASH"; // CASH/POS
	private String posReceiptNbr; // if POS
	private double receivedAmount; // if CASH
	public String getCodMode() {
		return codMode;
	}
	public void setCodMode(String codMode) {
		this.codMode = codMode;
	}
	public String getPosReceiptNbr() {
		return posReceiptNbr;
	}
	public void setPosReceiptNbr(String posReceiptNbr) {
		this.posReceiptNbr = posReceiptNbr;
	}
	public double getReceivedAmount() {
		return receivedAmount;
	}
	public void setReceivedAmount(double receivedAmount) {
		this.receivedAmount = receivedAmount;
	}
	
}
