package com.starunion.java.fsccserver.dao.freepbx;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.starunion.java.fsccserver.dao.DbUtilsTemplate;
import com.starunion.java.fsccserver.po.freepbx.SysOperator;


@Component
public class DaoSysOperator extends DbUtilsTemplate {
//	private static final Logger logger = Logger.getLogger(MenuOneDao.class);

	public List<SysOperator> findAll() {
		List<SysOperator> operatorList = new ArrayList<SysOperator>();
		operatorList = super.find(SysOperator.class, "select * from sys_operator");
		System.out.println("get size = "+operatorList.size());
		return operatorList;
	}
	
	public SysOperator findById(int id) {
		SysOperator menu = new SysOperator();
		menu = (SysOperator) super.findBy("select * from menu_main where id = ?",id);
		return menu;
	}

}
