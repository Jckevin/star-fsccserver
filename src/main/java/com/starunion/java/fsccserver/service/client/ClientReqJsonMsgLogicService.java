package com.starunion.java.fsccserver.service.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.msg.MsgBase;
import com.starunion.java.fsccserver.msg.RspMsgBase;
import com.starunion.java.fsccserver.msg.cc.ReqMsgCcSign;
import com.starunion.java.fsccserver.msg.fs.ReqMsgSysCallSatistics;
import com.starunion.java.fsccserver.msg.fs.ReqMsgSysGenCall;
import com.starunion.java.fsccserver.msg.fs.ReqMsgSysGenOper;
import com.starunion.java.fsccserver.po.ClientRequestMessageCc;
import com.starunion.java.fsccserver.po.freecc.AgentInfo;
import com.starunion.java.fsccserver.service.LoginAndOutService;
import com.starunion.java.fsccserver.service.JsonMsgService;
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
	JsonMsgService reqMsgParse;
	@Autowired
	ProcClientReqExecCmd reqMsgCmdService;
	@Autowired
	ProcClientReqQuryCmd reqMsgQuryCmdService;
	@Autowired
	ProcClientReqSql procReqSqlService;
	@Autowired
	JsonMsgService jsonService;
	@Autowired
	private QuartzTaskService timerTask;
	@Autowired
	private ProcLinuxCommand procLinuxCmd;

	public ClientReqJsonMsgLogicService() {

	}

	public String procClientRequest(String reqLine) {
		String rspBuff = null;
		MsgBase msgBase = reqMsgParse.convToObj(reqLine, MsgBase.class);
		if (msgBase != null) {
			int res = 0;
			String content = "";
			ReqMsgSysGenCall msg = null;
			logger.debug("receive client request type [{}]", msgBase.getMsgType());
			switch (msgBase.getMsgType()) {
			case ConstantSystem.CC_LOG_OUT:
//				rspBuff = makeClientStatusResponse(reqLine, ConstantSystem.FAILED);
				logger.debug("begin process log out service");
				break;
			case ConstantSystem.CC_AGENT_SIGN:
				ReqMsgCcSign msgCcSign = reqMsgParse.convToObj(reqLine, ReqMsgCcSign.class);
				res = reqMsgCmdService.execCmdAgentSign(msgCcSign.getCaller(), msgCcSign.getStatus());
				rspBuff = makeClientMsgJsonResp(msgBase, res);
				break;
			case ConstantSystem.SYS_EXEC_CTD:
				msg = reqMsgParse.convToObj(reqLine, ReqMsgSysGenCall.class);
				res = reqMsgCmdService.execCmdCTD(msg.getCaller(), msg.getCallee());
				rspBuff = makeClientMsgJsonResp(msgBase, res);
				break;
			case ConstantSystem.SYS_EXEC_MONITOR:
				msg = reqMsgParse.convToObj(reqLine, ReqMsgSysGenCall.class);
				res = reqMsgCmdService.execCmdMonitor(msg.getCaller(), msg.getCallee());
				rspBuff = makeClientMsgJsonResp(msgBase, res);
				break;
			case ConstantSystem.SYS_EXEC_INSERT:
				msg = reqMsgParse.convToObj(reqLine, ReqMsgSysGenCall.class);
				res = reqMsgCmdService.execCmdInsert(msg.getCaller(), msg.getCallee());
				rspBuff = makeClientMsgJsonResp(msgBase, res);
				break;
			case ConstantSystem.SYS_EXEC_DEMOLITSH:
				msg = reqMsgParse.convToObj(reqLine, ReqMsgSysGenCall.class);
				res = reqMsgCmdService.execCmdDemolishBridge(msg.getMsgType(), msg.getCaller(), msg.getCallee());
				rspBuff = makeClientMsgJsonResp(msgBase, res);
				break;
			case ConstantSystem.SYS_EXEC_BRIDGE:
				msg = reqMsgParse.convToObj(reqLine, ReqMsgSysGenCall.class);
				res = reqMsgCmdService.execCmdDemolishBridge(msg.getMsgType(), msg.getCaller(), msg.getCallee());
				rspBuff = makeClientMsgJsonResp(msgBase, res);
				break;
			case ConstantSystem.SYS_EXEC_INTERCEPT:
				msg = reqMsgParse.convToObj(reqLine, ReqMsgSysGenCall.class);
				res = reqMsgCmdService.execCmdDemolishBridge(msg.getMsgType(), msg.getCaller(), msg.getCallee());
				rspBuff = makeClientMsgJsonResp(msgBase, res);
				break;
			case ConstantSystem.SYS_EXEC_HANGUP:
				ReqMsgSysGenOper hangupMsg = reqMsgParse.convToObj(reqLine, ReqMsgSysGenOper.class);
				res = reqMsgCmdService.execCmdHangup(hangupMsg.getCallee());
				rspBuff = makeClientMsgJsonResp(msgBase, res);
				break;
			case ConstantSystem.SYS_EXEC_RECORD:
				ReqMsgSysGenOper recordMsg = reqMsgParse.convToObj(reqLine, ReqMsgSysGenOper.class);
				res = reqMsgCmdService.execCmdRecord(recordMsg.getCallee());
				rspBuff = makeClientMsgJsonResp(msgBase, res);
				break;
			/** UP IS EXEC PART, DOWN IS QUERY PART */
			case ConstantSystem.CC_AGENT_QRY:
				content = reqMsgQuryCmdService.getCcAgentInfoList(msgBase.getClientId());
				rspBuff = makeClientMsgJsonResp(msgBase, res);
				break;
			case ConstantSystem.SYS_QUERY_STATISTICS_SESSION_INFO:
				ReqMsgSysCallSatistics msgCallSatis = reqMsgParse.convToObj(reqLine, ReqMsgSysCallSatistics.class);
				int count = procReqSqlService.getCdrSessionCount(msgCallSatis.getTimeStart(),
						msgCallSatis.getTimeEnd());
				StringBuffer buff = new StringBuffer();
				buff.append(ConstantSystem.EXPRESS_STATISTIC_SESSION);
				buff.append(ConstantSystem.SYS_SPLIT);
				buff.append(Integer.toString(count));
				buff.append("\n");
//				rspBuff = makeClientContentResponse(buff.toString(), reqLine);
				break;
			case ConstantSystem.SYS_QUERY_STATISTICS_AGENT_SESSION_INFO:

				break;

			default:
				logger.debug("unknow message type, do nothing...");
				rspBuff = "unkown message\n";
				break;
			}
			return rspBuff;
		} else {
			rspBuff = makeClientMsgJsonResp(msgBase, -1);
			return rspBuff;
		}

	}

	private String makeClientMsgJsonResp(MsgBase msg, int res) {
		RspMsgBase msgRsp = new RspMsgBase();
		msgRsp.setMsgType(msg.getMsgType());
		msgRsp.setMsgId(msg.getMsgId());
		msgRsp.setClientId(msg.getClientId());
		msgRsp.setRspCode(res);
		if (res == ConstantSystem.SUCCESS) {
			msgRsp.setRspReason("success");
		}
		return jsonService.convToJString(msgRsp);
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
