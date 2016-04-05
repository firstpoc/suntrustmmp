package com.poc.bean;

import org.codehaus.jackson.annotate.JsonProperty;

public class AccountTransactionStatus {
	private String status;
	@JsonProperty("transactionnumber")
	private int transactionnumber;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getTransactionnumber() {
		return transactionnumber;
	}

	public void setTransactionnumber(int transactionnumber) {
		this.transactionnumber = transactionnumber;
	}

}
