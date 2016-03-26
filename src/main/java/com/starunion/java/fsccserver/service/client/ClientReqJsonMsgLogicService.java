package com.starunion.java.fsccserver.service.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.msg.MsgBase;
import com.starunion.java.fsccserver.msg.cc.ReqMsgCcSign;
import com.starunion.java.fsccserver.msg.fs.ReqMsgSysCallSatistics;
import com.starunion.java.fsccserver.msg.fs.ReqMsgSysGenCall;
import com.starunion.java.fsccserver.msg.fs.ReqMsgSysGenOper;
import com.starunion.java.fsccserver.po.ClientRequestMessageCc;
import com.starunion.java.fsccserver.po.freecc.AgentInfo;
import com.starunion.java.fsccserver.service.LoginAndOutService;
import com.starunion.java.fsccserver.service.ProcLinuxCommand;
import com.starunion.java.fsccserver.service.timer.QuartzTaskService;
import com.starunion.java.fsccserver.util.ConstantSystem;

/**
 * @author Lings
 * @date Mar 26, 2016 15:00:58 PM
 * @describe on the train Beijing->Nanjing
 */
@Service
public class ClientReqJsonMsgLogicService {
	private static final Logger logger = LoggerFactory.getLogger(ClientReqMsgLogicService.class);

	@Autowired
	LoginAndOutService loginService;
	@Autowired
	ClientReqJsonMsgParseService reqMsgParse;
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

	public ClientReqJsonMsgLogicService() {

	}

	public StringBuffer procClientRequest(String reqLine) {
		StringBuffer rspBuff = new StringBuffer();
		MsgBase msgBase = reqMsgParse.parseObj(reqLine, MsgBase.class);
		if (msgBase != null) {
			int res = 0;
			String content = "";
			ReqMsgSysGenCall msg = null;
			logger.debug("receive client request type [{}]", msgBase.getMsgType());
			switch (msgBase.getMsgType()) {
			case ConstantSystem.CC_LOG_OUT:
				rspBuff = makeClientStatusResponse(reqLine, ConstantSystem.FAILED);
				logger.debug("begin process log out service");
				break;
			case ConstantSystem.CC_AGENT_SIGN:
				ReqMsgCcSign msgCcSign = reqMsgParse.parseObj(reqLine, ReqMsgCcSign.class);
				res = reqMsgCmdService.execCmdAgentSign(msgCcSign.getCaller(), msgCcSign.getStatus());
				rspBuff = makeClientStatusResponse(reqLine, res);
				break;
			case ConstantSystem.SYS_EXEC_CTD:
				msg = reqMsgParse.parseObj(reqLine, ReqMsgSysGenCall.class);
				res = reqMsgCmdService.execCmdCTD(msg.getCaller(), msg.getCallee());
				rspBuff = makeClientStatusResponse(reqLine, res);
				break;
			case ConstantSystem.SYS_EXEC_MONITOR:
				msg = reqMsgParse.parseObj(reqLine, ReqMsgSysGenCall.class);
				res = reqMsgCmdService.execCmdMonitor(msg.getCaller(), msg.getCallee());
				rspBuff = makeClientStatusResponse(reqLine, res);
				break;
			case ConstantSystem.SYS_EXEC_INSERT:
				msg = reqMsgParse.parseObj(reqLine, ReqMsgSysGenCall.class);
				res = reqMsgCmdService.execCmdInsert(msg.getCaller(), msg.getCallee());
				rspBuff = makeClientStatusResponse(reqLine, res);
				break;
			case ConstantSystem.SYS_EXEC_DEMOLITSH:
				msg = reqMsgParse.parseObj(reqLine, ReqMsgSysGenCall.class);
				res = reqMsgCmdService.execCmdDemolishBridge(msg.getMsgType(), msg.getCaller(), msg.getCallee());
				rspBuff = makeClientStatusResponse(reqLine, res);
				break;
			case ConstantSystem.SYS_EXEC_BRIDGE:
				msg = reqMsgParse.parseObj(reqLine, ReqMsgSysGenCall.class);
				res = reqMsgCmdService.execCmdDemolishBridge(msg.getMsgType(), msg.getCaller(), msg.getCallee());
				rspBuff = makeClientStatusResponse(reqLine, res);
				break;
			case ConstantSystem.SYS_EXEC_INTERCEPT:
				msg = reqMsgParse.parseObj(reqLine, ReqMsgSysGenCall.class);
				res = reqMsgCmdService.execCmdDemolishBridge(msg.getMsgType(), msg.getCaller(), msg.getCallee());
				rspBuff = makeClientStatusResponse(reqLine, res);
				break;
			case ConstantSystem.SYS_EXEC_HANGUP:
				ReqMsgSysGenOper hangupMsg = reqMsgParse.parseObj(reqLine, ReqMsgSysGenOper.class);
				res = reqMsgCmdService.execCmdHangup(hangupMsg.getCallee());
				rspBuff = makeClientStatusResponse(reqLine, res);
				break;
			case ConstantSystem.SYS_EXEC_RECORD:
				ReqMsgSysGenOper recordMsg = reqMsgParse.parseObj(reqLine, ReqMsgSysGenOper.class);
				res = reqMsgCmdService.execCmdRecord(recordMsg.getCallee());
				rspBuff = makeClientStatusResponse(reqLine, res);
				break;
			/** UP IS EXEC PART, DOWN IS QUERY PART */
			case ConstantSystem.CC_AGENT_QRY:
				content = reqMsgQuryCmdService.getCcAgentInfoList(msgBase.getClientId());
				rspBuff = makeClientContentResponse(content, reqLine);
				break;
			case ConstantSystem.SYS_QUERY_STATISTICS_SESSION_INFO:
				ReqMsgSysCallSatistics msgCallSatis = reqMsgParse.parseObj(reqLine, ReqMsgSysCallSatistics.class);
				int count = procReqSqlService.getCdrSessionCount(msgCallSatis.getTimeStart(),
						msgCallSatis.getTimeEnd());
				StringBuffer buff = new StringBuffer();
				buff.append(ConstantSystem.EXPRESS_STATISTIC_SESSION);
				buff.append(ConstantSystem.SYS_SPLIT);
				buff.append(Integer.toString(count));
				buff.append("\n");
				rspBuff = makeClientContentResponse(buff.toString(), reqLine);
				break;
			case ConstantSystem.SYS_QUERY_STATISTICS_AGENT_SESSION_INFO:

				break;

			default:
				logger.debug("unknow message type, do nothing...");
				rspBuff = makeClientStatusResponse(reqLine, ConstantSystem.FAILED);
				break;
			}
			return rspBuff;
		} else {
			rspBuff = makeClientStatusResponse(reqLine, ConstantSystem.FAILED);
			return rspBuff;
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
