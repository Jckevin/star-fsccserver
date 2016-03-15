package com.starunion.java.fsccserver.dao.freepbx;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.starunion.java.fsccserver.dao.DbUtilsTemplate;
import com.starunion.java.fsccserver.po.freepbx.UserSipCamer;


@Component
public class DaoUserSipCamer extends DbUtilsTemplate {
	public List<UserSipCamer> findAll() {
		List<UserSipCamer> list = new ArrayList<UserSipCamer>();
		list = super.find(UserSipCamer.class, "select * from sip_camers");
		return list;
	}
}
