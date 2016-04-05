package com.poc.bean;

import org.codehaus.jackson.annotate.JsonProperty;

public class AccountCreationStatus {
	
	private String status;
	@JsonProperty("accountnumber")
	private int accountNumber;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	
	

}
