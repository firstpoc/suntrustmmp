package com.poc.bean;

import org.codehaus.jackson.annotate.JsonProperty;

public class TransactionAttributes {

	@JsonProperty("FromAccountNumber")
	private int fromAccountNumber;
	@JsonProperty("ToAccountNumber")
	private int toAccountNumber;
	@JsonProperty("TransactionType")
	private String transactionType;
	@JsonProperty("TransactionAmount")
	private double transactionAmount;
	@JsonProperty("AccountType")
	private String accountType;
	@JsonProperty("ChequeNumber")
	private int chequeNumber;

	public int getFromAccountNumber() {
		return fromAccountNumber;
	}

	public void setFromAccountNumber(int fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

	public int getToAccountNumber() {
		return toAccountNumber;
	}

	public void setToAccountNumber(int toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	
	

	public double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public int getChequeNumber() {
		return chequeNumber;
	}

	public void setChequeNumber(int chequeNumber) {
		this.chequeNumber = chequeNumber;
	}

	@Override
	public String toString() {
		return "TransactionAttributes [fromAccountNumber=" + fromAccountNumber
				+ ", toAccountNumber=" + toAccountNumber + ", transactionType="
				+ transactionType + ", transactionAmount=" + transactionAmount
				+ ", accountType=" + accountType + ", chequeNumber="
				+ chequeNumber + "]";
	}
	

}
