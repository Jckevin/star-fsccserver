package com.starunion.java.fsccserver.dao;

import org.springframework.stereotype.Repository;

import com.starunion.java.fsccserver.po.freepbx.UserSip;

/** 
* @author Lings  
* @date Mar 15, 2016 11:53:18 AM 
* 
*/
@Repository
public class DaoUserSip1 extends MyDbUtilsTemplate {

//	public DaoUserSip1(){
//		
//	}
	
	public UserSip findByName(){
		
		return findFirst("select * from sip_users where id = 1",UserSip.class);
	}
}
