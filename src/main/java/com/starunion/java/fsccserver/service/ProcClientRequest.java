package com.starunion.java.fsccserver.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.starunion.java.fsccserver.po.ClientRequestMessageCc;
import com.starunion.java.fsccserver.po.freecc.AgentInfo;
import com.starunion.java.fsccserver.service.timer.QuartzTaskService;
import com.starunion.java.fsccserver.util.ConstantCc;

/**
 * @author Lings
 * @date Mar 3, 2016 3:40:00 PM
 * 
 */

@Service
public class ProcClientRequest {
	private static final Logger logger = LoggerFactory.getLogger(ProcClientRequest.class);

	@Autowired
	LoginAndOutService loginService;
	@Autowired
	MessageClientReqService reqMsgService;
	@Autowired
	ProcClientReqCmd reqMsgCmdService;
	@Autowired
	ProcClientReqSql procReqSqlService;
	@Autowired
	private QuartzTaskService timerTask;
	@Autowired
	private ProcLinuxCommand procLinuxCmd;

	public ProcClientRequest() {

	}

	public StringBuffer procClientRequest(String reqLine) {
		StringBuffer rspBuff = new StringBuffer();

		ClientRequestMessageCc msg = reqMsgService.parseRequestMessage(reqLine);
		if (msg != null) {
			int res = 0;
			logger.debug("receive client request type [{}]", msg.getType());
			switch (msg.getType()) {
			case ConstantCc.CC_LOGIN:
				logger.debug("begin process login service");
				res = loginService.AgentLogin(msg.getClientId(), msg.getContent());
				rspBuff = makeClientResponse(reqLine, ConstantCc.SUCCESS);
				break;
			case ConstantCc.CC_LOGOUT:
				rspBuff = makeClientResponse(reqLine, ConstantCc.FAILED);
				logger.debug("begin process log out service");
				break;
			case ConstantCc.CC_CTD:
				res = reqMsgCmdService.procCmdCTD(msg.getClientId(), msg.getContent());
				if (res == 0) {
					rspBuff = makeClientResponse(reqLine, ConstantCc.SUCCESS);
				} else {
					rspBuff = makeClientResponse(reqLine, ConstantCc.FAILED);
				}
				logger.debug("begin process the third party call service");
				break;
			case "ccLogina":
				rspBuff = makeClientResponse(reqLine, procReqSqlService.insertAgentInfo("1", "password", "0"));
				break;
			case "ccLogind":
				int i = procReqSqlService.delAgentInfo("1");
				logger.debug("!!!!!!!!!after delete ,result = {}",i);
				rspBuff = makeClientResponse(reqLine, i);
				break;
			case "ccLoginu":
				// UserSip us = procReqSqlService.findByNumber();
				Map<String, String> map = new HashMap<String, String>();
				map.put("agentPwd", "drowssap");
				map.put("agentType", "9");
				rspBuff = makeClientResponse(reqLine, procReqSqlService.updateAgentInfo("1", map));
				break;
			case "ccLoginq":
				AgentInfo us = procReqSqlService.findAgentInfo("1");
				logger.debug("pwd = {},type={}", us.getAgentPwd(), us.getAgentType());
				rspBuff = makeClientResponse(reqLine, 1);
				break;
			case "ccLoginqq":
				List<AgentInfo> uslist = procReqSqlService.findAgentInfoList();
				for (AgentInfo in : uslist) {
					logger.debug("from list pwd = {},type={}", in.getAgentPwd(), in.getAgentType());
				}
				rspBuff = makeClientResponse(reqLine, 1);
				break;
			default:
				logger.debug("begin process ctd service");
				reqMsgCmdService.procCmdCTD1(msg.getClientId(), msg.getContent());
				rspBuff = makeClientResponse(reqLine, ConstantCc.SUCCESS);
				break;
			}
			return rspBuff;
		} else {
			rspBuff = makeClientResponse(reqLine, ConstantCc.FAILED);
			return rspBuff;
		}

	}

	private StringBuffer makeClientResponse(String buff, int result) {
		StringBuffer nBuff = new StringBuffer();
		nBuff.append(buff);
		if (result == ConstantCc.SUCCESS) {
			nBuff.append(ConstantCc.DISP_TAIL_SUCC);
		} else if (result == ConstantCc.FAILED) {
			nBuff.append(ConstantCc.DISP_TAIL_FAIL);
		}
		return nBuff;
	}

}
