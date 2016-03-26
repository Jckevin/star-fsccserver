package com.starunion.java.fsccserver.service;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;

/**
 * @author Lings
 * @date Mar 26, 2016 14:09:58 PM
 * 
 */
@Service
public class JsonMsgService {
	private Gson gson = new Gson();

	public boolean isValidMsg(String line) {
		boolean res = false;
		// :TODO java Pattern

		return res;
	}

	public <T> T convToObj(String str, Class<T> entityClass) {

		return gson.fromJson(str, entityClass);
	}

	public String convToJString(Object obj) {
		return gson.toJson(obj);
	}
}
