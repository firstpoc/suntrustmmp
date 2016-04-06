package com.poc.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.poc.bean.AccountTransactionStatus;
import com.poc.bean.TransactionAttributes;

public class AccountTransactionDao {
	private static final Logger log = LoggerFactory
			.getLogger(UserAccountCreationDao.class);

	public AccountTransactionStatus insertAccountTransaction(
			TransactionAttributes transactionAttributes) {
		int transactionNumber = 0;
		int useID = 0;
		int mappingID = 0;
		String transType = null;
		double fromAccountBalance = 0.0;
		double toAccountBalance = 0.0;
		boolean accountTransactionResult = false;
		boolean UpdateQueryToAccountResult = false;
		boolean UpdateQueryFromAccountResult = false;
		AccountTransactionStatus accountTransactionStatus = new AccountTransactionStatus();
		String serverIp = "127.0.0.1";
		String keyspace = "suntrust";
		System.out.println("cluster..........");
		Cluster cluster = Cluster.builder().addContactPoints(serverIp).build();
		// cluster.getConfiguration().getSocketOptions().setReadTimeoutMillis(600000);
		System.out.println("keyspace.........." + keyspace);
		Session session = cluster.connect(keyspace);
		System.out.println("session.........." + session);

		log.info("*********Retreive Transaction_Number from table *************");
		String cqlStatement = "SELECT max(Transaction_Number) as Transaction_Number  FROM suntrust.transaction";
		System.out.println("max(Transaction_Number).........." + cqlStatement);
		for (Row row : session.execute(cqlStatement)) {
			transactionNumber = row.getInt("Transaction_Number");
		}
		transactionNumber = transactionNumber + 1;
		System.out.println("transactionNumber.........." + transactionNumber);
		System.out.println("getFromAccountNumber.........."
				+ transactionAttributes.getFromAccountNumber());
		String cqlStatementTransactionUserid = "SELECT User_ID,User_AccountsMappingID FROM suntrust.UserAccountsMapping WHERE AccountNumber="
				+ transactionAttributes.getFromAccountNumber()
				+ " allow filtering;";
		System.out.println("cqlStatementTransactionUserid.........."
				+ cqlStatementTransactionUserid);
		for (Row row : session.execute(cqlStatementTransactionUserid)) {
			useID = row.getInt("User_ID");
			System.out.println("useID...." + useID);
			mappingID = row.getInt("User_AccountsMappingID");
			System.out.println("mappingID........" + mappingID);
		}
		System.out.println("useID...." + useID + "mappingID........"
				+ mappingID);
		String cqlStatementTransactionTransType = "SELECT TransactionTypeID  FROM suntrust.transactiontype WHERE Transaction_Type='"
				+ transactionAttributes.getTransactionType()
				+ "' allow filtering;";
		System.out.println("cqlStatementTransactionTransType.........."
				+ cqlStatementTransactionTransType);
		for (Row row : session.execute(cqlStatementTransactionTransType)) {
			transType = row.getString("TransactionTypeID");
		}
		System.out.println("transType.........." + transType);

		String cqlStatementGetFromAccountBalance = "select  Balance from Account where AccountNumber="
				+ transactionAttributes.getFromAccountNumber() + ";";
		System.out.println("cqlStatementGetFromAccountBalance.........."
				+ cqlStatementGetFromAccountBalance);
		for (Row row : session.execute(cqlStatementGetFromAccountBalance)) {
			fromAccountBalance = row.getDouble("Balance");
		}
		System.out.println("fromAccountBalance.........." + fromAccountBalance);

		String cqlStatementGetToAccountBalance = "select  Balance from Account where AccountNumber="
				+ transactionAttributes.getToAccountNumber() + ";";

		System.out.println("cqlStatementGetToAccountBalance.........."
				+ cqlStatementGetToAccountBalance);
		for (Row row : session.execute(cqlStatementGetToAccountBalance)) {
			toAccountBalance = row.getDouble("Balance");
		}
		System.out.println("toAccountBalance.........." + toAccountBalance);
		System.out.println("TransactionAmount.........."
				+ transactionAttributes.getTransactionAmount());
		if (transType.equalsIgnoreCase("DC")
				|| transType.equalsIgnoreCase("CHQ")) {
			System.out.println("if transType == DC ..........");
			if (fromAccountBalance >= transactionAttributes
					.getTransactionAmount()) {
				System.out.println("if fromAccountBalance >  ..........");
				fromAccountBalance = fromAccountBalance
						- transactionAttributes.getTransactionAmount();
				toAccountBalance = toAccountBalance
						+ transactionAttributes.getTransactionAmount();
				System.out.println("fromAccountBalance...."
						+ fromAccountBalance + "toAccountBalance........"
						+ toAccountBalance);
				String insertQuerytransaction = "insert into transaction (Transaction_Number,Transaction_Date,User_ID,From_AccountNumber,To_AccountNumber,TransactionTypeID,Transaction_Amount,ChequeNumber,CurrencyType,Balance,User_AccountsMappingID,Transaction_Remarks) values ("
						+ transactionNumber
						+ ",dateof(now()),"
						+ useID
						+ ","
						+ transactionAttributes.getFromAccountNumber()
						+ ","
						+ transactionAttributes.getToAccountNumber()
						+ ",'"
						+ transType
						+ "',"
						+ transactionAttributes.getTransactionAmount()
						+ ",'"
						+ transactionAttributes.getChequeNumber()
						+ "', 'INR',"
						+ toAccountBalance + "," + mappingID + ",'');";
				System.out.println("insertQueryAccountMapping.."
						+ insertQuerytransaction);
				ResultSet accountTransactionResults = session
						.execute(insertQuerytransaction);
				accountTransactionResult = accountTransactionResults
						.wasApplied();
				System.out.println("after userAccountMappingResult.........."
						+ accountTransactionResults.wasApplied());

				if (accountTransactionResult == true) {

					String UpdateQueryToAccount = "UPDATE Account SET Balance="
							+ toAccountBalance + " WHERE AccountNumber="
							+ transactionAttributes.getToAccountNumber() + ";";
					System.out.println("insertQueryAccountMapping.."
							+ UpdateQueryToAccount);
					ResultSet UpdateQueryToAccountResults = session
							.execute(UpdateQueryToAccount);
					UpdateQueryToAccountResult = UpdateQueryToAccountResults
							.wasApplied();

					String UpdateQueryFromAccount = "UPDATE Account SET Balance="
							+ fromAccountBalance
							+ " WHERE AccountNumber="
							+ transactionAttributes.getFromAccountNumber()
							+ ";";
					System.out.println("insertQueryAccountMapping.."
							+ UpdateQueryFromAccount);
					ResultSet UpdateQueryFromAccountResults = session
							.execute(UpdateQueryFromAccount);
					UpdateQueryFromAccountResult = UpdateQueryFromAccountResults
							.wasApplied();

				}

			}
		}
		if (accountTransactionResult == true
				&& UpdateQueryToAccountResult == true
				&& UpdateQueryFromAccountResult == true) {

			accountTransactionStatus.setTransactionnumber(transactionNumber);
			accountTransactionStatus.setStatus("Success");

		} else {

			accountTransactionStatus.setStatus("fail");
		}

		return accountTransactionStatus;
	}
}
