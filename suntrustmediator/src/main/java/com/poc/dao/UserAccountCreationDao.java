package com.poc.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.poc.bean.AccountCreationStatus;
import com.poc.bean.UserAccount;
/**
 * @author vamsikrishnareddy_p
 *
 */
public class UserAccountCreationDao {
	private static final Logger log = LoggerFactory.getLogger(UserAccountCreationDao.class);
	
	public AccountCreationStatus insertUserAccount(UserAccount userAccount){
		
		int userID = 0;
		int accNum = 0;
		int accMapping=0;
		String accTypeID =null;
		boolean userResult = false;
		boolean userAccountResult = false;		
		AccountCreationStatus accountCreationStatus = new AccountCreationStatus();
		String serverIp = "127.0.0.1";
	    String keyspace = "suntrust";
	    System.out.println("cluster..........");
	    Cluster cluster = Cluster.builder().addContactPoints(serverIp).build();
	    //cluster.getConfiguration().getSocketOptions().setReadTimeoutMillis(600000);
	    System.out.println("Session..........");
	    Session session = cluster.connect(keyspace);
	    
	    System.out.println("select stmt..........");
	    log.info("*********Retreive ID from table *************");
	    String cqlStatement = "SELECT max(User_ID) as User_ID  FROM suntrust.users";
	    for (Row row : session.execute(cqlStatement)) {
	       userID  = row.getInt("User_ID");
	             }
	    userID = userID + 1;
	    
	    System.out.println("insert stmt.........."+userID+userAccount.getFirstName()+userAccount.getLastName()+userAccount.getAddress().getZipCode());
	    String insertQueryUser = "insert into users (User_ID,FirstName,LastName,DateOfBirth,Email,City,ZIPCode,State,Street,MobileNumber) values ("+userID+", '"+userAccount.getFirstName()+"','"+userAccount.getLastName()+"','"+userAccount.getDateOfBirth()+"','"+userAccount.getEmail()+"', '"+userAccount.getAddress().getCity()+"',"+userAccount.getAddress().getZipCode()+", '"+userAccount.getAddress().getState()+"','"+userAccount.getAddress().getStreet()+"','"+userAccount.getMobileNumber()+"');";
	    log.info("insertQuery.."+insertQueryUser);
	    System.out.println("insertQuery.."+insertQueryUser);
	    ResultSet userResults  = session.execute(insertQueryUser);
	    
	    userResult =  userResults.wasApplied();
	    
	   // Row row = userResults.one();
	    

	    System.out.println("after insertUserstmt.........."+ userResults.wasApplied());
		
	    if (userResult==true)
	    {
	    	System.out.println("if condition ..........");
	    	
		    String cqlStatementUserAccount = "SELECT max(AccountNumber) as AccountNumber  FROM suntrust.account";
		    for (Row row : session.execute(cqlStatementUserAccount)) {
		       accNum  = row.getInt("AccountNumber");
		             }		    
		    accNum = accNum + 1;
		    
		    System.out.println("accNum .........."+accNum);
		    String cqlStatementUserAccountType = "SELECT AccountTypeID   FROM suntrust.accounttype WHERE AccountType='"+userAccount.getAccountType()+"'allow filtering;";
		    for (Row row : session.execute(cqlStatementUserAccountType)) {
			 accTypeID  = row.getString("AccountTypeID");
			             }			   
			String insertQueryAccount="insert into account(AccountNumber,User_ID,AccountTypeID,Balance,Date,AccountStatus,User_name,Password) values ("+accNum+","+userID+",'"+accTypeID+"',0.00,dateof(now()),'Active','','');";
			System.out.println("insertQueryAccount.."+insertQueryAccount);
			ResultSet userAccountResults  = session.execute(insertQueryAccount);
			userAccountResult =  userAccountResults.wasApplied();
			
			System.out.println("after insertAccountUserstmt.........."+ userResults.wasApplied());
	    }
	    
	    if (userResult==true && userAccountResult== true)
	    {
	    	String cqlStatementUserAccountmapping = "SELECT max(User_AccountsMappingID) as User_AccountsMappingID  FROM suntrust.useraccountsmapping;";
		    for (Row row : session.execute(cqlStatementUserAccountmapping)) {
		       accMapping  = row.getInt("User_AccountsMappingID");
		             }		    
		    accMapping = accMapping + 1;
		    
		    String insertQueryAccountMapping="insert into useraccountsmapping (User_AccountsMappingID,User_ID,AccountNumber) values ("+accMapping+","+userID+","+accNum+");";
			System.out.println("insertQueryAccountMapping.."+insertQueryAccountMapping);
			ResultSet userAccountMappingResults  = session.execute(insertQueryAccountMapping);			
			System.out.println("after userAccountMappingResult.........."+ userAccountMappingResults.wasApplied());
			
	    }
	    if ( userAccountResult== true )
	    {
	    accountCreationStatus.setAccountNumber(accNum);
	    accountCreationStatus.setStatus("success");
	    }	
	    else
	    {
	    	accountCreationStatus.setStatus("fail");
	    }
	    
	  return accountCreationStatus;
	  
		
	}

}

