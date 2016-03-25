package com.starunion.java.fsccserver.service.client;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.dao.DaoUserSip1;
import com.starunion.java.fsccserver.dao.freecc.DaoAgentInfo;
import com.starunion.java.fsccserver.dao.freecc.DaoStarAddrBook;
import com.starunion.java.fsccserver.dao.fs.DaoCdrInfo;
import com.starunion.java.fsccserver.po.freecc.AgentInfo;
import com.starunion.java.fsccserver.po.freepbx.UserSip;
import com.starunion.java.fsccserver.service.TimeStringService;

/**
 * @author Lings
 * @date Mar 4, 2016 5:39:22 PM
 * 
 */
@Service
public class ProcClientReqSql {

	@Autowired
	DaoStarAddrBook daoStarAddrBook;
	@Autowired
	DaoUserSip1 daoUserSip;
	@Autowired
	DaoAgentInfo daoAgentInfo;
	@Autowired
	DaoCdrInfo daoCdrInfo;
	@Autowired
	TimeStringService timeStringService;

	public ProcClientReqSql() {

	}

	public int getCdrSessionCount(String id, String content) {
		//:TODO check the id permission
		String[] body = content.split("\\|");
		if (body[0].equals("0") && body[1].equals("0")) {
			return daoCdrInfo.getCdrSessionCountAll();
		} else {
			return daoCdrInfo.getCdrSessionCountByTime(timeStringService.String2TimestampFormat(body[0]),
					timeStringService.String2TimestampFormat(body[1]));
		}

	}

	public int insertAgentInfo(String agId, String agPwd, String agType) {
		return daoAgentInfo.add(agId, agPwd, agType);
	}

	public int delAgentInfo(String agId) {
		return daoAgentInfo.delById(agId);
	}

	public int updateAgentInfo(String agId, Map<String, String> map) {
		return daoAgentInfo.updateAll(agId, map);
	}

	public AgentInfo findAgentInfo(String agId) {
		return daoAgentInfo.findBy(agId);
	}

	public List<AgentInfo> findAgentInfoList() {
		return daoAgentInfo.findAll();
	}

	public int insertAddrBook() {
		return daoStarAddrBook.add();
	}

	public UserSip findByNumber() {
		return daoUserSip.findByName();
	}

}
