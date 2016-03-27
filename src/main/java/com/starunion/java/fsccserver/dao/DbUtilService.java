package com.starunion.java.fsccserver.dao;

import javax.sql.DataSource;

import org.springframework.stereotype.Service;

/**
 * @author Lings
 * @date Mar 25, 2016 11:56:42 AM
 * 
 */

@Service
public class DbUtilService extends DbUtilTemplate {

	public int getCountAll(DataSource ds, String dbName) {
		return getCount(ds, "select count(*) from " + dbName);
	}
	
	public int getCountAllByCondition(DataSource ds, String dbName,String condition) {
		int count = -1;
		StringBuffer buff = new StringBuffer();
		buff.append("select count(*) from ");
		buff.append(dbName);
		buff.append(" and where ");
		buff.append(condition);
		count = getCount(ds, buff.toString());
		return count;
	}

	public int getCountByTime(DataSource ds, String dateStart, String dateEnd, String dbName) {
		int count = -1;
		StringBuffer buff = new StringBuffer();
		buff.append("select count(*) from ");
		buff.append(dbName);
		buff.append("where start_stamp > '");
		buff.append(dateStart);
		buff.append("' and start_stamp < '");
		buff.append(dateEnd);
		buff.append("'");
		count = getCount(ds, buff.toString());
		return count;
	}
	public int getCountByTimeCondition(DataSource ds, String dateStart, String dateEnd, String dbName,String condition) {
		int count = -1;
		StringBuffer buff = new StringBuffer();
		buff.append("select count(*) from ");
		buff.append(dbName);
		buff.append("where start_stamp > '");
		buff.append(dateStart);
		buff.append("' and start_stamp < '");
		buff.append(dateEnd);
		buff.append("' and ");
		buff.append(condition);
		count = getCount(ds, buff.toString());
		return count;
	}
}
