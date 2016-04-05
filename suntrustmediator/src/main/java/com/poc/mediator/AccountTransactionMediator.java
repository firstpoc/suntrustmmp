package com.poc.mediator;

import java.io.IOException;

import com.poc.bean.AccountTransactionStatus;
import com.poc.bean.TransactionAttributes;
import com.poc.dao.AccountTransactionDao;

import org.apache.synapse.MessageContext;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class AccountTransactionMediator extends AbstractMediator {

	TransactionAttributes transactionAttributes;

	public boolean mediate(MessageContext context) {
		// Getting the json payload to string
		String transpayload = JsonUtil
				.jsonPayloadToString(((Axis2MessageContext) context)
						.getAxis2MessageContext());

		System.out.println("jsonPayloadToString*****" + transpayload);

		try {
			ObjectMapper mapper = new ObjectMapper();
			
			transactionAttributes=mapper.readValue(transpayload, TransactionAttributes.class);
			log.info("values.." + transactionAttributes);
			System.out.println("values.." + transactionAttributes);
			AccountTransactionDao accountTransactionDao = new AccountTransactionDao();
			AccountTransactionStatus accountTransactionStatus = accountTransactionDao.insertAccountTransaction(transactionAttributes);
			
			String transresponse = mapper
					.writeValueAsString(accountTransactionStatus);
			context.setProperty("account.transaction.result", transresponse);
		} catch (JsonGenerationException e) {
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
