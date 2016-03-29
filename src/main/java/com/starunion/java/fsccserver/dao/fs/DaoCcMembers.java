package com.starunion.java.fsccserver.dao.fs;

import java.math.BigDecimal;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.dao.DbUtilTemplate;

/**
 * @author Lings
 * @date Mar 29, 2016 3:15:02 PM
 * 
 */
@Service
public class DaoCcMembers extends DbUtilTemplate {
	@Autowired
	DataSource dsFreeSs;

	public int getACDCallCount() {
		return getCount(dsFreeSs, "select count(*) from members");
	}

	public int getACDCallCountByTime(int dateStart, int dateEnd) {
		int count = -1;
		StringBuffer buff = new StringBuffer();
		buff.append("select count(*) from cdr where system_epoch > '");
		buff.append(dateStart);
		buff.append(" and system_epoch < ");
		buff.append(dateEnd);
		count = getCount(dsFreeSs, buff.toString());
		return count;
	}

	public int getACDCallCountAbandoned() {
		return getCount(dsFreeSs, "select count(*) from members where bridge_epoch = 0");
	}

	public int getACDCallWaitedSecAnswered() {

		return getColumnSum(dsFreeSs,
				"select sum(s) from(select bridge_epoch-system_epoch as s from members where bridge_epoch!=0) t");
	}

	public int getACDCallWaitedSecAbandoned() {
		return getColumnSum(dsFreeSs,
				"select sum(s) from(select abandoned_epoch-system_epoch as s from members where bridge_epoch=0) t");
	}

	public List<Long> getACDCallWaitedAnsweredList() {
		return findColumnList(dsFreeSs,
				"select s from(select bridge_epoch-system_epoch as s from members where bridge_epoch!=0) t",
				Long.class);
	}

	public List<Long> getACDCallWaitedAbandonedList() {
		return findColumnList(dsFreeSs,
				"select s from(select abandoned_epoch-system_epoch as s from members where bridge_epoch=0) t",
				Long.class);
	}

}
