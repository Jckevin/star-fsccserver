package com.starunion.java.fsccserver.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.starunion.java.fsccserver.po.freepbx.UserSip;

/**
 * @author Lings
 * @date Mar 15, 2016 11:53:18 AM
 * 
 */
@Repository
public class DaoUserSip1 extends DbUtilTemplate {

	// public DaoUserSip1(){
	//
	// }
	@Autowired
	DataSource dsStarPbx;

	public UserSip findByName() {

		return findOne(dsStarPbx, "select * from sip_users where id = 1", UserSip.class);
	}
}
