package com.starunion.java.fsccserver.dao.fs;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.dao.DbUtilsTemplate;

/**
 * @author Lings
 * @date Mar 24, 2016 6:09:21 PM
 * 
 */
@Service
public class DaoCallCenterCallInfo extends DbUtilsTemplate {
	@Autowired
	DataSource dsFreeSs;

	public int getCcCallCount(String dateStart, String dateEnd) {
		int count = -1;
		if (dateStart.equals("0") && dateStart.equals("0")) {
			count = getCount(dsFreeSs, "select count(*) from members");
		} else {
			StringBuffer buff = new StringBuffer();
			buff.append("select count(*) from members where start_stamp > '");
			buff.append(dateStart);
			buff.append("' and start_stamp < '");
			buff.append(dateEnd);
			buff.append("'");
			count = getCount(dsFreeSs, buff.toString());
		}
		return count;
	}
}
