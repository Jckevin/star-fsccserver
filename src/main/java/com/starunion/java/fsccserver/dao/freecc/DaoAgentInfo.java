package com.starunion.java.fsccserver.dao.freecc;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.starunion.java.fsccserver.dao.DbUtilTemplate;
import com.starunion.java.fsccserver.po.freecc.AgentInfo;

/**
 * @author Lings
 * @date Mar 15, 2016 2:45:14 PM
 * 
 */

@Repository
public class DaoAgentInfo extends DbUtilTemplate {

	@Autowired
	DataSource dsStarCc;

	public AgentInfo findBy(String agId) {
		String sql = "select * from agent_info where agentId = " + agId;
		return findOne(dsStarCc, sql, AgentInfo.class);
	}

	public List<AgentInfo> findAll() {
		String sql = "select * from agent_info";
		return findList(dsStarCc, sql, AgentInfo.class);
	}

	public int add(String agId, String agPwd, String agType) {
		String sql = "insert into agent_info (agentId,agentPwd,agentType) values (?,?,?)";
		Object[] params = { agId, agPwd, agType };
		return update(dsStarCc, sql, params);
	}

	public int delById(String agId) {
		String sql = "delete from agent_info where agentId = " + agId;
		return update(dsStarCc, sql);
	}

	public int updateAll(String agId, Map<String, String> map) {
		StringBuffer sql = new StringBuffer();
		sql.append("update agent_info set ");
		
		StringBuffer subSql = new StringBuffer();
		Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = iter.next();
			subSql.append(entry.getKey());
			subSql.append("=\"");
			subSql.append(entry.getValue());
			subSql.append("\",");
		}
		sql.append(subSql.substring(0, subSql.length() - 1));
		
		sql.append(" where agentId = \"");
		sql.append(agId);
		sql.append("\"");
		return update(dsStarCc, sql.toString());
	}

}
