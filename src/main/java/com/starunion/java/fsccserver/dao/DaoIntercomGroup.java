package com.starunion.java.fsccserver.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.starunion.java.fsccserver.po.IntercomGroup;

@Component
public class DaoIntercomGroup extends DbUtilsTemplate {
	public List<IntercomGroup> findAll() {
		List<IntercomGroup> list = new ArrayList<IntercomGroup>();
		list = super.find(IntercomGroup.class, "select * from conf_intercom");
		return list;
	}
}
