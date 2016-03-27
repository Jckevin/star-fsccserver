package com.starunion.java.fsccserver.dao.fs;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.dao.DbUtilTemplate;

/**
 * @author Lings
 * @date Mar 27, 2016 8:13:41 PM
 * 
 */
@Repository
public class DaoCdrInfoNoExtends extends DbUtilTemplate{
	@Autowired
	DataSource dsFreeSs;

	public int getCdrSessionCountAll() {
		return getCount(dsFreeSs, "select count(*) from cdr");
	}
	public int getCdrSessionCountByCallId(DataSource ds, String agentId) {
		int count = -1;
		StringBuffer buff = new StringBuffer();
		buff.append("select count(*) from cdr where caller = ");
		buff.append(agentId);
		buff.append(" and where callee = ");
		buff.append(agentId);
		count = getCount(ds, buff.toString());
		return count;
	}

	public int getCdrSessionCountByTime(DataSource ds, String dateStart, String dateEnd) {
		int count = -1;
		StringBuffer buff = new StringBuffer();
		buff.append("select count(*) from cdr where start_stamp > '");
		buff.append(dateStart);
		buff.append("' and start_stamp < '");
		buff.append(dateEnd);
		buff.append("'");
		count = getCount(ds, buff.toString());
		return count;
	}
}
