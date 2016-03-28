package com.starunion.java.fsccserver.service.client;

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
import com.starunion.java.fsccserver.service.LoginAndOutService;
import com.starunion.java.fsccserver.service.ProcLinuxCommand;
import com.starunion.java.fsccserver.service.timer.QuartzTaskService;
import com.starunion.java.fsccserver.util.ConstantSystem;

/**
 * @author Lings
 * @date Mar 3, 2016 3:40:00 PM
 * 
 */

@Service
public class ClientReqMsgLogicService {
	private static final Logger logger = LoggerFactory.getLogger(ClientReqMsgLogicService.class);

	@Autowired
	LoginAndOutService loginService;
	@Autowired
	ClientReqMsgCheckService reqMsgService;
	@Autowired
	ProcClientReqExecCmd reqMsgCmdService;
	@Autowired
	ProcClientReqQuryCmd reqMsgQuryCmdService;
	@Autowired
	ProcClientReqSql procReqSqlService;
	@Autowired
	private QuartzTaskService timerTask;
	@Autowired
	private ProcLinuxCommand procLinuxCmd;

	public ClientReqMsgLogicService() {

	}

	public String procClientRequest(String reqLine) {
		StringBuffer rspBuff = new StringBuffer();

		ClientRequestMessageCc msg = reqMsgService.parseRequestMessage(reqLine);
		if (msg != null) {
			int res = 0;
			String content = "";
			int sessionCount = 0;
			StringBuffer buff = new StringBuffer();
			logger.debug("receive client request type [{}]", msg.getType());
			switch (msg.getType()) {
			case ConstantSystem.CC_LOG_IN:
				logger.debug("begin process login service");
				res = loginService.AgentLogin(msg.getClientId(), msg.getContent());
				rspBuff = makeClientStatusResponse(reqLine, ConstantSystem.SUCCESS);
				break;
			case ConstantSystem.CC_LOG_OUT:
				rspBuff = makeClientStatusResponse(reqLine, ConstantSystem.FAILED);
				logger.debug("begin process log out service");
				break;
			case ConstantSystem.CC_AGENT_SIGN:
				res = reqMsgCmdService.execCmdAgentSign(msg.getClientId(), msg.getContent());
				rspBuff = makeClientStatusResponse(reqLine, res);
				break;
			case ConstantSystem.SYS_EXEC_CTD:
				res = reqMsgCmdService.execCmdCTD(msg.getClientId(), msg.getContent());
				rspBuff = makeClientStatusResponse(reqLine, res);
				break;
			case ConstantSystem.SYS_EXEC_MONITOR:
				res = reqMsgCmdService.execCmdMonitor(msg.getClientId(), msg.getContent());
				rspBuff = makeClientStatusResponse(reqLine, res);
				break;
			case ConstantSystem.SYS_EXEC_INSERT:
				res = reqMsgCmdService.execCmdInsert(msg.getClientId(), msg.getContent());
				rspBuff = makeClientStatusResponse(reqLine, res);
				break;
			case ConstantSystem.SYS_EXEC_DEMOLITSH:
				res = reqMsgCmdService.execCmdDemolishBridge(msg.getType(), msg.getClientId(), msg.getContent());
				rspBuff = makeClientStatusResponse(reqLine, res);
				break;
			case ConstantSystem.SYS_EXEC_BRIDGE:
				res = reqMsgCmdService.execCmdDemolishBridge(msg.getType(), msg.getClientId(), msg.getContent());
				rspBuff = makeClientStatusResponse(reqLine, res);
				break;
			case ConstantSystem.SYS_EXEC_INTERCEPT:
				res = reqMsgCmdService.execCmdDemolishBridge(msg.getType(), msg.getClientId(), msg.getContent());
				rspBuff = makeClientStatusResponse(reqLine, res);
				break;
			case ConstantSystem.SYS_EXEC_HANGUP:
				res = reqMsgCmdService.execCmdHangup(msg.getContent());
				rspBuff = makeClientStatusResponse(reqLine, res);
				break;
			case ConstantSystem.SYS_EXEC_RECORD:
				res = reqMsgCmdService.execCmdRecord(msg.getContent());
				rspBuff = makeClientStatusResponse(reqLine, res);
				break;
			/** UP IS EXEC PART, DOWN IS QUERY PART */
			case ConstantSystem.CC_AGENT_QRY:
				content = reqMsgQuryCmdService.getCcAgentInfoList(msg.getClientId());
				rspBuff = makeClientContentResponse(content, reqLine);
				break;
			case ConstantSystem.SYS_QUERY_SESSION_COUNT_ALL:
				sessionCount = procReqSqlService.getCdrSessionCountAll(msg.getContent());
				buff.append(ConstantSystem.EXPRESS_STATISTIC_SESSION);
				buff.append(ConstantSystem.SYS_SPLIT);
				buff.append(Integer.toString(sessionCount));
				buff.append("\n");
				rspBuff = makeClientContentResponse(buff.toString(), reqLine);
				buff.delete(0, buff.length());
				break;
			case ConstantSystem.SYS_QUERY_SESSION_COUNT_TIME:
				sessionCount = procReqSqlService.getCdrSessionCountByTime(msg.getContent());
				buff.append(ConstantSystem.EXPRESS_STATISTIC_SESSION);
				buff.append(ConstantSystem.SYS_SPLIT);
				buff.append(Integer.toString(sessionCount));
				buff.append("\n");
				rspBuff = makeClientContentResponse(buff.toString(), reqLine);
				buff.delete(0, buff.length());
				break;
			case ConstantSystem.SYS_QUERY_SESSION_COUNT_CALLID:
				/** type:requester:agentId */
				sessionCount = procReqSqlService.getCdrSessionCountByCallId(msg.getContent());
				buff.append(ConstantSystem.EXPRESS_STATISTIC_SESSION);
				buff.append(ConstantSystem.SYS_SPLIT);
				buff.append(Integer.toString(sessionCount));
				buff.append("\n");
				rspBuff = makeClientContentResponse(buff.toString(), reqLine);
				buff.delete(0, buff.length());
				break;
			case ConstantSystem.SYS_QUERY_SESSION_COUNT_CALLID_IN:
				/** type:requester:agentId */
				sessionCount = procReqSqlService.getCdrSessionCountByCallIdIn(msg.getContent());
				buff.append(ConstantSystem.EXPRESS_STATISTIC_SESSION);
				buff.append(ConstantSystem.SYS_SPLIT);
				buff.append(Integer.toString(sessionCount));
				buff.append("\n");
				rspBuff = makeClientContentResponse(buff.toString(), reqLine);
				buff.delete(0, buff.length());
				break;
			case ConstantSystem.SYS_QUERY_SESSION_COUNT_CALLID_OUT:
				/** type:requester:agentId */
				sessionCount = procReqSqlService.getCdrSessionCountByCallIdOut(msg.getContent());
				buff.append(ConstantSystem.EXPRESS_STATISTIC_SESSION);
				buff.append(ConstantSystem.SYS_SPLIT);
				buff.append(Integer.toString(sessionCount));
				buff.append("\n");
				rspBuff = makeClientContentResponse(buff.toString(), reqLine);
				buff.delete(0, buff.length());
				break;
			case ConstantSystem.SYS_QUERY_SESSION_COUNT_CALLID_TIME:
				sessionCount = procReqSqlService.getCdrSessionCountByCallIdTime(msg.getContent());
				buff.append(ConstantSystem.EXPRESS_STATISTIC_SESSION);
				buff.append(ConstantSystem.SYS_SPLIT);
				buff.append(Integer.toString(sessionCount));
				buff.append("\n");
				rspBuff = makeClientContentResponse(buff.toString(), reqLine);
				buff.delete(0, buff.length());
				break;
			case ConstantSystem.SYS_QUERY_SESSION_COUNT_CALLID_IN_TIME:
				sessionCount = procReqSqlService.getCdrSessionCountByCallIdInTime(msg.getContent());
				buff.append(ConstantSystem.EXPRESS_STATISTIC_SESSION);
				buff.append(ConstantSystem.SYS_SPLIT);
				buff.append(Integer.toString(sessionCount));
				buff.append("\n");
				rspBuff = makeClientContentResponse(buff.toString(), reqLine);
				buff.delete(0, buff.length());
				break;
			case ConstantSystem.SYS_QUERY_SESSION_COUNT_CALLID_OUT_TIME:
				sessionCount = procReqSqlService.getCdrSessionCountByCallIdOutTime(msg.getContent());
				buff.append(ConstantSystem.EXPRESS_STATISTIC_SESSION);
				buff.append(ConstantSystem.SYS_SPLIT);
				buff.append(Integer.toString(sessionCount));
				buff.append("\n");
				rspBuff = makeClientContentResponse(buff.toString(), reqLine);
				buff.delete(0, buff.length());
				break;
			case ConstantSystem.SYS_QUERY_SESSION_COUNT_CALLID_REJECT:
				sessionCount = procReqSqlService.getCdrSessionCountByCallIdReject(msg.getContent());
				buff.append(ConstantSystem.EXPRESS_STATISTIC_SESSION);
				buff.append(ConstantSystem.SYS_SPLIT);
				buff.append(Integer.toString(sessionCount));
				buff.append("\n");
				rspBuff = makeClientContentResponse(buff.toString(), reqLine);
				buff.delete(0, buff.length());
				break;
			case ConstantSystem.SYS_QUERY_SESSION_COUNT_CALLID_REJECT_TIME:
				sessionCount = procReqSqlService.getCdrSessionCountByCallIdRejectTime(msg.getContent());
				buff.append(ConstantSystem.EXPRESS_STATISTIC_SESSION);
				buff.append(ConstantSystem.SYS_SPLIT);
				buff.append(Integer.toString(sessionCount));
				buff.append("\n");
				rspBuff = makeClientContentResponse(buff.toString(), reqLine);
				buff.delete(0, buff.length());
				break;
			case ConstantSystem.SYS_QUERY_SESSION_COUNT_CALLID_TRANSFER:
				/** not right enough, maybe freeswitch bug*/
				sessionCount = procReqSqlService.getCdrSessionCountByCallIdTransfer(msg.getContent());
				buff.append(ConstantSystem.EXPRESS_STATISTIC_SESSION);
				buff.append(ConstantSystem.SYS_SPLIT);
				buff.append(Integer.toString(sessionCount));
				buff.append("\n");
				rspBuff = makeClientContentResponse(buff.toString(), reqLine);
				buff.delete(0, buff.length());
				break;
			case ConstantSystem.SYS_QUERY_SESSION_COUNT_CALLID_TRANSFER_TIME:
				/** not right enough, maybe freeswitch bug*/
				sessionCount = procReqSqlService.getCdrSessionCountByCallIdTransferTime(msg.getContent());
				buff.append(ConstantSystem.EXPRESS_STATISTIC_SESSION);
				buff.append(ConstantSystem.SYS_SPLIT);
				buff.append(Integer.toString(sessionCount));
				buff.append("\n");
				rspBuff = makeClientContentResponse(buff.toString(), reqLine);
				buff.delete(0, buff.length());
				break;
			default:
				logger.debug("unknow message type, do nothing...");
				rspBuff = makeClientStatusResponse(reqLine, ConstantSystem.FAILED);
				break;
			}
			return rspBuff.toString();
		} else {
			rspBuff = makeClientStatusResponse(reqLine, ConstantSystem.FAILED);
			return rspBuff.toString();
		}

	}

	private StringBuffer makeClientStatusResponse(String buff, int result) {
		StringBuffer nBuff = new StringBuffer();
		nBuff.append(buff);
		nBuff.append(ConstantSystem.SYS_SPLIT);
		if (result == ConstantSystem.SUCCESS) {
			nBuff.append(ConstantSystem.SYS_TAIL_SUCC);
		} else if (result == ConstantSystem.FAILED) {
			nBuff.append(ConstantSystem.SYS_TAIL_FAIL);
		}
		nBuff.append(ConstantSystem.SYS_TAIL_END);
		return nBuff;
	}

	private StringBuffer makeClientContentResponse(String content, String request) {
		StringBuffer nBuff = new StringBuffer();
		nBuff.append(content);
		nBuff.append(request);
		nBuff.append(ConstantSystem.SYS_SPLIT);
		if (content.length() > 0) {
			nBuff.append(ConstantSystem.SYS_TAIL_SUCC);
		} else {
			nBuff.append(ConstantSystem.SYS_TAIL_FAIL);
		}
		nBuff.append(ConstantSystem.SYS_TAIL_END);
		return nBuff;
	}

}
