package com.starunion.java.fsccserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/** 
* @author Lings  
* @date Mar 3, 2016 5:21:22 PM 
* 
*/

@Service
public class LoginAndOutService {
	private static final Logger logger = LoggerFactory.getLogger(LoginAndOutService.class);
	
	public LoginAndOutService(){
		
	}
	
	public boolean AgentLogin(String id,String pwd){
		//:TODO check the agent info from MYSQL ?
		return true;
	}
}
