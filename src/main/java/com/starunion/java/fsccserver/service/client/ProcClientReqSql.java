package com.starunion.java.fsccserver.service.client;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.dao.DaoUserSip1;
import com.starunion.java.fsccserver.dao.cc.DaoAgentInfo;
import com.starunion.java.fsccserver.dao.cc.DaoStarAddrBook;
import com.starunion.java.fsccserver.dao.fs.DaoCdrInfo;
import com.starunion.java.fsccserver.dao.fs.DaoCdrInfoNoExtends;
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
	DaoCdrInfoNoExtends daoCdrInfoNew;
	@Autowired
	TimeStringService timeStringService;

	public ProcClientReqSql() {

	}

	public int getCdrSessionCountAll(String content) {
		/** it seems content no use ,only for placeholder */
		return daoCdrInfoNew.getSessionCountAll();

	}

	public int getCdrSessionCountByTime(String content) {
		// :TODO check the id permission
		/** type:requester:startTime|endTime */
		String[] data = content.split("\\|");
		return daoCdrInfoNew.getSessionCountByTime(timeStringService.String2TimestampFormat(data[0]),
				timeStringService.String2TimestampFormat(data[1]));
	}

	public int getCdrSessionCountByCallId(String content) {
		// :TODO check the id permission
		/** type:requester:agentId */
		return daoCdrInfoNew.getSessionCountByCallId(content);
	}
	
	public int getCdrSessionCountByCallIdTime(String content) {
		// :TODO check the id permission
		/** type:requester:agentId|startTime|endTime */
		String[] data = content.split("\\|");
		return daoCdrInfoNew.getSessionCountByCallIdTime(data[0], timeStringService.String2TimestampFormat(data[1]),
				timeStringService.String2TimestampFormat(data[2]));
	}
	
	public int getCdrSessionCountByCallIdIn(String content) {
		// :TODO check the id permission
		/** type:requester:agentId */
		return daoCdrInfoNew.getSessionCountByCallIdIn(content);
	}
	
	public int getCdrSessionCountByCallIdInTime(String content) {
		// :TODO check the id permission
		/** type:requester:agentId|startTime|endTime */
		String[] data = content.split("\\|");
		return daoCdrInfoNew.getSessionCountByCallIdInTime(data[0], timeStringService.String2TimestampFormat(data[1]),
				timeStringService.String2TimestampFormat(data[2]));
	}
	
	public int getCdrSessionCountByCallIdOut(String content) {
		// :TODO check the id permission
		/** type:requester:agentId */
		return daoCdrInfoNew.getSessionCountByCallIdOut(content);
	}
	
	public int getCdrSessionCountByCallIdOutTime(String content) {
		// :TODO check the id permission
		/** type:requester:agentId|startTime|endTime */
		String[] data = content.split("\\|");
		return daoCdrInfoNew.getSessionCountByCallIdOutTime(data[0], timeStringService.String2TimestampFormat(data[1]),
				timeStringService.String2TimestampFormat(data[2]));
	}
	
	public int getCdrSessionCountByCallIdReject(String content) {
		// :TODO check the id permission
		/** type:requester:agentId|startTime|endTime */
		return daoCdrInfoNew.getSessionCountByCallIdReject(content);
	}
	
	public int getCdrSessionCountByCallIdRejectTime(String content) {
		// :TODO check the id permission
		/** type:requester:agentId|startTime|endTime */
		String[] data = content.split("\\|");
		return daoCdrInfoNew.getSessionCountByCallIdRejectTime(data[0], timeStringService.String2TimestampFormat(data[1]),
				timeStringService.String2TimestampFormat(data[2]));
	}
	
	public int getCdrSessionCountByCallIdTransfer(String content) {
		// :TODO check the id permission
		return daoCdrInfoNew.getSessionCountByCallIdTransfer(content);
	}
	
	public int getCdrSessionCountByCallIdTransferTime(String content) {
		// :TODO check the id permission
		/** type:requester:agentId|startTime|endTime */
		String[] data = content.split("\\|");
		return daoCdrInfoNew.getSessionCountByCallIdTransferTime(data[0], timeStringService.String2TimestampFormat(data[1]),
				timeStringService.String2TimestampFormat(data[2]));
	}

}
