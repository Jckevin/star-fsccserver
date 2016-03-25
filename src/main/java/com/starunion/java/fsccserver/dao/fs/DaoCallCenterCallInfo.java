package com.starunion.java.fsccserver.dao.fs;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.dao.DbUtilService;
import com.starunion.java.fsccserver.dao.DbUtilTemplate;

/**
 * @author Lings
 * @date Mar 24, 2016 6:09:21 PM
 * 
 */
@Service
public class DaoCallCenterCallInfo extends DbUtilService {
	@Autowired
	DataSource dsFreeSs;

	public int getCcCallCount(String dateStart, String dateEnd) {
		return getCountByTime(dsFreeSs, dateStart, dateEnd, "members");
	}
}
