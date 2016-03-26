package com.starunion.java.fsccserver.service.client;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
/**
 * @author Lings
 * @date Mar 26, 2016 14:09:58 PM
 * 
 */
@Service
public class ClientReqJsonMsgParseService {
	public boolean isValidMsg(String line) {
		boolean res = false;
		// :TODO java Pattern

		return res;
	}

	public <T> T parseObj(String str, Class<T> entityClass) {
		Gson gson = new Gson();
		return gson.fromJson(str, entityClass);
	}

}
