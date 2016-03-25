package com.starunion.java.fsccserver.dao.fs;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.starunion.java.fsccserver.dao.DbUtilService;
import com.starunion.java.fsccserver.dao.DbUtilTemplate;
import com.starunion.java.fsccserver.po.fs.CdrInfo;

/**
 * @author Lings
 * @date Mar 24, 2016 10:35:35 AM
 * 
 */

@Repository
public class DaoCdrInfo extends DbUtilService {
	@Autowired
	DataSource dsFreeSs;

	public int getCdrSessionCountAll() {
		return getCountAll(dsFreeSs, "cdr");
	}

	public int getCdrSessionCountByTime(String dateStart, String dateEnd) {
		return getCountByTime(dsFreeSs, dateStart, dateEnd, "cdr");
	}

	public int getCdrSessionCountByTimeAgent(String dateStart, String dateEnd,String agent ) {
		return getCountByTime(dsFreeSs, dateStart, dateEnd, "cdr");
	}
	
	public CdrInfo findBy(String agId) {
		String sql = "select * from cdr where hangup_cause = NORMAL_CLEARING";
		return findOne(dsFreeSs, sql, CdrInfo.class);
	}

	public int getSessionNoAnswerCount() {
		return update(dsFreeSs, "select count(*) from cdr ");
	}
	// 1.任何情况下SELECT COUNT(*) FROM tablename是最优选择；
	// 2.尽量减少SELECT COUNT(*) FROM tablename WHERE COL = ‘value’ 这种查询；
	// 3.杜绝SELECT COUNT(COL) FROM tablename WHERE COL2 = ‘value’ 的出现。
	//
	//
	// 如果表没有主键，那么count（1）比count（*）快。
	// 如果有主键，那么count（主键，联合主键）比count（*）快。
	// 如果表只有一个字段，count（*）最快。
	// SELECT COUNT(*) FROM users WHERE k='avs';
	// 或：
	// SELECT id FROM goods WHERE k='avs' LIMIT 10;
	// 但是记录总数总是需要单独的语句来查询，例如在分页查询程序中就有这样的问题，其实mysql可以在一次查询中获取记录和总数的，这就是要使用SQL_CALC_FOUND_ROWS参数，使用方法如下：
	// SELECT SQL_CALC_FOUND_ROWS goods WHERE k='avs' LIMIT 10;
	// SELECT FOUND_ROWS();
	// 这虽然是两个sql语句，但是确是查询一次数据库，效率明显提高了一半！其中SQL_CALC_FOUND_ROWS
	// 告诉Mysql将sql所处理的行数记录下来，FOUND_ROWS() 则取到了这个纪录。

	// select count(bank) from table group by bank
}
