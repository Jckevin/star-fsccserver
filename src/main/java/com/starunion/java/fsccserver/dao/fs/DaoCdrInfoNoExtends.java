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
public class DaoCdrInfoNoExtends extends DbUtilTemplate {
	@Autowired
	DataSource dsFreeSs;

	public int getSessionCountAll() {
		return getCount(dsFreeSs, "select count(*) from cdr");
	}

	public int getSessionCountByCallId(String callId) {
		int count = -1;
		StringBuffer buff = new StringBuffer();
		buff.append("select count(*) from cdr where caller_id_number = ");
		buff.append(callId);
		buff.append(" or destination_number = ");
		buff.append(callId);
		count = getCount(dsFreeSs, buff.toString());
		return count;
	}
	
	public int getSessionCountByCallIdIn(String callId) {
		int count = -1;
		StringBuffer buff = new StringBuffer();
		buff.append("select count(*) from cdr where destination_number = ");
		buff.append(callId);
		buff.append(" and caller_id_number != ");
		buff.append(callId);
		count = getCount(dsFreeSs, buff.toString());
		return count;
	}
	
	public int getSessionCountByCallIdOut(String callId) {
		int count = -1;
		StringBuffer buff = new StringBuffer();
		buff.append("select count(*) from cdr where caller_id_number = ");
		buff.append(callId);
		count = getCount(dsFreeSs, buff.toString());
		return count;
	}

	public int getSessionCountByTime(String dateStart, String dateEnd) {
		int count = -1;
		StringBuffer buff = new StringBuffer();
		buff.append("select count(*) from cdr where start_stamp > '");
		buff.append(dateStart);
		buff.append("' and start_stamp < '");
		buff.append(dateEnd);
		buff.append("'");
		count = getCount(dsFreeSs, buff.toString());
		return count;
	}

	public int getSessionCountByCallIdTime(String callId, String dateStart, String dateEnd) {
		int count = -1;
		StringBuffer buff = new StringBuffer();
		buff.append("select count(*) from cdr where start_stamp > '");
		buff.append(dateStart);
		buff.append("' and start_stamp < '");
		buff.append(dateEnd);
		buff.append("' and (caller_id_number = ");
		buff.append(callId);
		buff.append(" or destination_number = ");
		buff.append(callId);
		buff.append(")");
		count = getCount(dsFreeSs, buff.toString());
		return count;
	}
	
	public int getSessionCountByCallIdInTime(String callId, String dateStart, String dateEnd) {
		int count = -1;
		StringBuffer buff = new StringBuffer();
		buff.append("select count(*) from cdr where start_stamp > '");
		buff.append(dateStart);
		buff.append("' and start_stamp < '");
		buff.append(dateEnd);
		buff.append("' and destination_number = ");
		buff.append(callId);
		buff.append(" and caller_id_number != ");
		buff.append(callId);
		count = getCount(dsFreeSs, buff.toString());
		return count;
	}
	
	public int getSessionCountByCallIdOutTime(String callId, String dateStart, String dateEnd) {
		int count = -1;
		StringBuffer buff = new StringBuffer();
		buff.append("select count(*) from cdr where start_stamp > '");
		buff.append(dateStart);
		buff.append("' and start_stamp < '");
		buff.append(dateEnd);
		buff.append("' and caller_id_number = ");
		buff.append(callId);
		count = getCount(dsFreeSs, buff.toString());
		return count;
	}
	
	public int getSessionCountByCallIdReject(String callId) {
		int count = -1;
		StringBuffer buff = new StringBuffer();
		buff.append("select count(*) from cdr where destination_number = ");
		buff.append(callId);
		buff.append(" and hangup_cause = 'CALL_REJECTED'");
		count = getCount(dsFreeSs, buff.toString());
		return count;
	}
	
	public int getSessionCountByCallIdRejectTime(String callId, String dateStart, String dateEnd) {
		int count = -1;
		StringBuffer buff = new StringBuffer();
		buff.append("select count(*) from cdr where start_stamp > '");
		buff.append(dateStart);
		buff.append("' and start_stamp < '");
		buff.append(dateEnd);
		buff.append("' and destination_number = ");
		buff.append(callId);
		buff.append(" and hangup_cause = 'CALL_REJECTED'");
		count = getCount(dsFreeSs, buff.toString());
		return count;
	}
	
	public int getSessionCountByCallIdTransfer(String callId) {
		int count = -1;
		StringBuffer buff = new StringBuffer();
		buff.append("select count(*) from cdr where destination_number = ");
		buff.append(callId);
		buff.append(" and caller_id_name = ");
		buff.append(callId);
		buff.append(" and caller_id_number != ");
		buff.append(callId);
		count = getCount(dsFreeSs, buff.toString());
		return count;
	}
	
	public int getSessionCountByCallIdTransferTime(String callId, String dateStart, String dateEnd) {
		int count = -1;
		StringBuffer buff = new StringBuffer();
		buff.append("select count(*) from cdr where start_stamp > '");
		buff.append(dateStart);
		buff.append("' and start_stamp < '");
		buff.append(dateEnd);
		buff.append("' and destination_number = ");
		buff.append(callId);
		buff.append(" and caller_id_name = ");
		buff.append(callId);
		buff.append(" and caller_id_number != ");
		buff.append(callId);
		count = getCount(dsFreeSs, buff.toString());
		return count;
	}

}
