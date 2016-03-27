package com.starunion.java.fsccserver.service.client;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.dao.DaoUserSip1;
import com.starunion.java.fsccserver.dao.cc.DaoAgentInfo;
import com.starunion.java.fsccserver.dao.cc.DaoStarAddrBook;
import com.starunion.java.fsccserver.dao.fs.DaoCdrInfo;
import com.starunion.java.fsccserver.po.freecc.AgentInfo;
import com.starunion.java.fsccserver.po.freepbx.UserSip;
import com.starunion.java.fsccserver.service.TimeStringService;

/**
 * @author Lings
 * @date Mar 27, 2016 8:00:21 PM
 * 
 */

@Service
public class ProcClientReqSqlJson {
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

	public ProcClientReqSqlJson() {

	}

	public int getCdrSessionCount(String content) {
		//:TODO check the id permission
		/** type:requester:startTime|endTime */
		String[] data = content.split("\\|");
		if (data[0].equals("0") && data[1].equals("0")) {
			return daoCdrInfo.getCdrSessionCountAll();
		} else {
			return daoCdrInfo.getCdrSessionCountByTime(timeStringService.String2TimestampFormat(data[0]),
					timeStringService.String2TimestampFormat(data[1]));
		}

	}
	
	public int getCdrSessionCountByAgentId(String content) {
		//:TODO check the id permission
		/** type:requester:startTime|endTime|agentId */
		String[] data = content.split("\\|");
		if (data[0].equals("0") && data[1].equals("0")) {
			return daoCdrInfo.getCdrSessionCountByAgentId(data[2]);
		} else {
			return daoCdrInfo.getCdrSessionCountByTimeAgent(timeStringService.String2TimestampFormat(data[0]),
					timeStringService.String2TimestampFormat(data[1]),data[2]);
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
