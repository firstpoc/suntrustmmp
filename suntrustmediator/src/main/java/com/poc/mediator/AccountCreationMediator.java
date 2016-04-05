package com.poc.mediator;

import java.io.IOException;

import org.apache.synapse.MessageContext;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.poc.bean.AccountCreationStatus;
import com.poc.bean.UserAccount;
import com.poc.dao.UserAccountCreationDao;

public class AccountCreationMediator extends AbstractMediator {

	UserAccount userAccount;

	private static final Logger log = LoggerFactory
			.getLogger(AccountCreationMediator.class);

	public boolean mediate(MessageContext context) {

		System.out
				.println("***************Request Payload came into Mediator Class");
		log.debug("Request Payload came into Mediator Class");

		// Getting the json payload to string
		String accpayload = JsonUtil
				.jsonPayloadToString(((Axis2MessageContext) context)
						.getAxis2MessageContext());

		System.out.println("jsonPayloadToString*****" + accpayload);
		try {
			ObjectMapper mapper = new ObjectMapper();

			userAccount = mapper.readValue(accpayload, UserAccount.class);
			log.info("values.." + userAccount);

			UserAccountCreationDao userAccountCreationDao = new UserAccountCreationDao();
			AccountCreationStatus accountCreationStatus = userAccountCreationDao
					.insertUserAccount(userAccount);

			String accresponse = mapper
					.writeValueAsString(accountCreationStatus);
			// String test = "{\"status\":\"success\",\"accountnumber\":33}";

			context.setProperty("account.creation.result", accresponse);

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

}
