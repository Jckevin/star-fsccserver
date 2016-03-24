package com.starunion.java.fsccserver.service.client;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.starunion.java.fsccserver.thread.client.CallableFsExecCmdProc;
import com.starunion.java.fsccserver.util.ConfigManager;
import com.starunion.java.fsccserver.util.ConstantSystem;
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
	CallableFsExecCmdProc taskInt;

	public ProcClientReqExecCmd() {

	}

	public int execCmdRecord(String caller) {
		String uuid = ServerDataMap.terStatusMap.get(caller).getCallUUid();
		
		Date time = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
		String timeStr = formatter.format(time);
		String[] timePart = timeStr.split("-");
		StringBuffer buff = new StringBuffer();
		buff.append("bgapi uuid_record ");
		buff.append(uuid);
		buff.append(" start ");
		buff.append(ConfigManager.getInstance().getDisRecordPath());
		buff.append(timePart[0]);
		buff.append("/");
		buff.append(caller);
		buff.append("_");
		buff.append(timePart[1]);
		buff.append(".wav");
		buff.append(ConstantSystem.FS_CMD_TAIL);
		
		logger.debug("!!!!!!!!!!!!!!!!!!!!!!!make cmd :{}",buff.toString());
		taskInt.setSendCmd(buff.toString());

		return getResult(taskInt);
	}
	
	public int execCmdHangup(String caller, String callee) {
		//check wether the caller has the permission.
		StringBuffer buff = new StringBuffer();
		String uuid = ServerDataMap.terStatusMap.get(callee).getCallUUid();
		buff.append("bgapi uuid_kill ");
		buff.append(uuid);
		buff.append(ConstantSystem.FS_CMD_TAIL);

		taskInt.setSendCmd(buff.toString());

		return getResult(taskInt);
	}
	
	public int execCmdUUbridge(String uuider, String uuidee) {
		StringBuffer buff = new StringBuffer();
		buff.append("bgapi uuid_bridge ");
		buff.append(uuider);
		buff.append(" ");
		buff.append(uuidee);
		buff.append(ConstantSystem.FS_CMD_TAIL);

		taskInt.setSendCmd(buff.toString());

		return getResult(taskInt);
	}
	
	public int execCmdDemolishBridge(String type, String caller, String callee) {
		StringBuffer buff = new StringBuffer();
		TerParkingInfo info = new TerParkingInfo();
		info.setType(type);
		info.setCallee(callee);
		ServerDataMap.terParkingMap.put(caller, info);

		buff.append("bgapi originate {origination_caller_id_number=");
		buff.append(ConstantSystem.FS_DEF_NUMBER);
		buff.append("}user/");
		buff.append(caller);
		buff.append(" &park");
		buff.append(ConstantSystem.FS_CMD_TAIL);

		taskInt.setSendCmd(buff.toString());

		return getResult(taskInt);
	}

	public int execCmdInsert(String caller, String callee) {
		// w1:current one. w2:opposite one. w3:three way.
		StringBuffer buff = new StringBuffer();
		String uuid = ServerDataMap.terStatusMap.get(callee).getCallUUid();
		buff.append("bgapi originate {origination_caller_id_number=");
		buff.append(ConstantSystem.FS_DEF_NUMBER);
		buff.append("}user/");
		buff.append(caller);
		buff.append(" \'queue_dtmf:w2@500,eavesdrop:");
		buff.append(uuid);
		buff.append("\' inline");
		buff.append(ConstantSystem.FS_CMD_TAIL);

		taskInt.setSendCmd(buff.toString());

		return getResult(taskInt);
	}

	public int execCmdMonitor(String caller, String callee) {
		StringBuffer buff = new StringBuffer();
		String uuid = ServerDataMap.terStatusMap.get(callee).getCallUUid();
		buff.append("bgapi originate {origination_caller_id_number=");
		buff.append(ConstantSystem.FS_DEF_NUMBER);
		buff.append("}user/");
		buff.append(caller);
		buff.append(" &eavesdrop(");
		buff.append(uuid);
		buff.append(")");
		buff.append(ConstantSystem.FS_CMD_TAIL);

		taskInt.setSendCmd(buff.toString());

		return getResult(taskInt);
	}

	public int execCmdCTD(String caller, String callee) {
		StringBuffer buff = new StringBuffer();
		buff.append("bgapi originate {origination_caller_id_number=");
		buff.append(ConstantSystem.FS_DEF_NUMBER);
		buff.append("}user/");
		buff.append(caller);
		buff.append(" &bridge(user/");
		buff.append(callee);
		buff.append(")");
		buff.append(ConstantSystem.FS_CMD_TAIL);

		taskInt.setSendCmd(buff.toString());

		return getResult(taskInt);
	}

	public int execCmdAgentSign(String caller, String type) {
		StringBuffer buff = new StringBuffer();
		buff.append("api callcenter_config agent set status ");
		buff.append(caller);
		buff.append(" '");
		switch (type) {
		case ConstantSystem.CC_AGENT_SIGN_IN:
			buff.append(ConstantSystem.FS_AGENT_SIGN_IN);
			break;
		case ConstantSystem.CC_AGENT_SIGN_OUT:
			buff.append(ConstantSystem.FS_AGENT_SIGN_OUT);
			break;
		case ConstantSystem.CC_AGENT_SIGN_BUSY:
			buff.append(ConstantSystem.FS_AGENT_SIGN_BREAK);
			break;
		default:
			logger.error("UNKNOWN sign type {} ,return failed", type);
			return ConstantSystem.FAILED;
		}
		buff.append("'");
		buff.append(ConstantSystem.FS_CMD_TAIL);

		taskInt.setSendCmd(buff.toString());

		return getResult(taskInt);
	}

	private int getResult(CallableFsExecCmdProc task) {
		Integer result = ConstantSystem.FAILED;
		Future<Integer> future = FsCcServer.executor.submit(taskInt);
		try {
			result = future.get(5000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
		}
		return result;
	}
}
