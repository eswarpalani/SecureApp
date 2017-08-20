package com.virtusa.virtusabank.secureapp.model;

public class FundTransfer {
	
	private String fromAcct;
	private String beneAcct;
	private double transferAmt;
	
	public String getFromAcct() {
		return fromAcct;
	}
	public void setFromAcct(String fromAcct) {
		this.fromAcct = fromAcct;
	}
	public String getBeneAcct() {
		return beneAcct;
	}
	public void setBeneAcct(String beneAcct) {
		this.beneAcct = beneAcct;
	}
	public double getTransferAmt() {
		return transferAmt;
	}
	public void setTransferAmt(double transferAmt) {
		this.transferAmt = transferAmt;
	}

}
