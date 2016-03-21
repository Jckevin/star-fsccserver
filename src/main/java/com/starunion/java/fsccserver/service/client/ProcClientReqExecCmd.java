package com.starunion.java.fsccserver.service.client;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.beginning.FsCcServer;
import com.starunion.java.fsccserver.po.TerParkingInfo;
import com.starunion.java.fsccserver.thread.client.CallableFsQueryCmdProc;
import com.starunion.java.fsccserver.util.ConstantCc;
import com.starunion.java.fsccserver.util.ServerDataMap;

/**
 * @author Lings
 * @date Mar 4, 2016 5:38:54 PM
 * 
 */
@Service
public class ProcClientReqExecCmd {
	private static final Logger logger = LoggerFactory.getLogger(ProcClientReqExecCmd.class);

	@Autowired
	CallableFsQueryCmdProc taskInt;

	public ProcClientReqExecCmd() {

	}

	public int execCmdDemolish(String caller, String callee) {
		// w1:current one. w2:opposite one. w3:three way.
		StringBuffer buff = new StringBuffer();
		TerParkingInfo info = new TerParkingInfo();
		info.setType(ConstantCc.SYS_EXEC_DEMOLITSH);
		info.setCallee(callee);
		ServerDataMap.terParkingMap.put(caller, info);

		buff.append("bgapi originate {origination_caller_id_number=");
		buff.append(ConstantCc.FS_DEF_NUMBER);
		buff.append("}user/");
		buff.append(caller);
		buff.append(" &park");
		buff.append(ConstantCc.FS_CMD_TAIL);

		taskInt.setSendCmd(buff.toString());

		return getResult(taskInt);
	}

	public int execCmdInsert(String caller, String callee) {
		// w1:current one. w2:opposite one. w3:three way.
		StringBuffer buff = new StringBuffer();
		String uuid = ServerDataMap.terStatusMap.get(callee).getCallUUid();
		buff.append("bgapi originate {origination_caller_id_number=");
		buff.append(ConstantCc.FS_DEF_NUMBER);
		buff.append("}user/");
		buff.append(caller);
		buff.append(" \'queue_dtmf:w2@500,eavesdrop:");
		buff.append(uuid);
		buff.append("\' inline");
		buff.append(ConstantCc.FS_CMD_TAIL);

		taskInt.setSendCmd(buff.toString());

		return getResult(taskInt);
	}

	public int execCmdMonitor(String caller, String callee) {
		StringBuffer buff = new StringBuffer();
		String uuid = ServerDataMap.terStatusMap.get(callee).getCallUUid();
		buff.append("bgapi originate {origination_caller_id_number=");
		buff.append(ConstantCc.FS_DEF_NUMBER);
		buff.append("}user/");
		buff.append(caller);
		buff.append(" &eavesdrop(");
		buff.append(uuid);
		buff.append(")");
		buff.append(ConstantCc.FS_CMD_TAIL);

		taskInt.setSendCmd(buff.toString());

		return getResult(taskInt);
	}

	public int execCmdCTD(String caller, String callee) {
		StringBuffer buff = new StringBuffer();
		buff.append("bgapi originate {origination_caller_id_number=");
		buff.append(ConstantCc.FS_DEF_NUMBER);
		buff.append("}user/");
		buff.append(caller);
		buff.append(" &bridge(user/");
		buff.append(callee);
		buff.append(")");
		buff.append(ConstantCc.FS_CMD_TAIL);

		taskInt.setSendCmd(buff.toString());

		return getResult(taskInt);
	}

	public int execCmdAgentSign(String caller, String type) {
		StringBuffer buff = new StringBuffer();
		buff.append("api callcenter_config agent set status ");
		buff.append(caller);
		buff.append(" '");
		switch (type) {
		case ConstantCc.CC_AGENT_SIGN_IN:
			buff.append(ConstantCc.FS_AGENT_SIGN_IN);
			break;
		case ConstantCc.CC_AGENT_SIGN_OUT:
			buff.append(ConstantCc.FS_AGENT_SIGN_OUT);
			break;
		case ConstantCc.CC_AGENT_SIGN_BUSY:
			buff.append(ConstantCc.FS_AGENT_SIGN_BREAK);
			break;
		default:
			logger.error("UNKNOWN sign type {} ,return failed", type);
			return ConstantCc.FAILED;
		}
		buff.append("'");
		buff.append(ConstantCc.FS_CMD_TAIL);

		taskInt.setSendCmd(buff.toString());

		return getResult(taskInt);
	}

	private int getResult(CallableFsQueryCmdProc task) {
		Integer result = ConstantCc.FAILED;
		Future<Integer> future = FsCcServer.executor.submit(taskInt);
		try {
			result = future.get(5000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
		}
		return result;
	}
}
