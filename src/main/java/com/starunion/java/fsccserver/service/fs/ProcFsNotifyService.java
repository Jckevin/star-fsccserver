package com.starunion.java.fsccserver.service.fs;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.beginning.FsCcServer;
import com.starunion.java.fsccserver.po.TerParkingInfo;
import com.starunion.java.fsccserver.po.TerStatusInfo;
import com.starunion.java.fsccserver.service.client.ProcClientReqExecCmd;
import com.starunion.java.fsccserver.thread.client.CallableFsExecCmdProc;
import com.starunion.java.fsccserver.util.ClientDataMap;
import com.starunion.java.fsccserver.util.ConstantCc;
import com.starunion.java.fsccserver.util.ServerDataMap;
import com.starunion.java.fsccserver.util.ToolsUtil;

/**
 * @author Lings
 * @date Mar 21, 2016 11:17:40 AM
 * 
 */

@Service
public class ProcFsNotifyService {
	@Autowired
	ProcClientReqExecCmd procCmd;

	public void procTerParking(String id, String uuid) {
		TerParkingInfo terInfo = ServerDataMap.terParkingMap.get(id);
		String callee = terInfo.getCallee();
		String type = terInfo.getType();
		String uuidCallee = "";
		String updateId = "";
		if (type.equals(ConstantCc.SYS_EXEC_DEMOLITSH) || type.equals(ConstantCc.SYS_EXEC_INTERCEPT)) {
			uuidCallee = ServerDataMap.terStatusMap.get(callee).getPeerUUid();
			updateId = ServerDataMap.terStatusMap.get(callee).getPeerNumber();
		} else if (type.equals(ConstantCc.SYS_EXEC_BRIDGE) || type.equals("12")) {
			uuidCallee = ServerDataMap.terStatusMap.get(callee).getCallUUid();
			updateId = callee;
		}
		
		int res = procCmd.execCmdUUbridge(uuid, uuidCallee);
		
		if (res == ConstantCc.SUCCESS) {
			updateMapTerStatus(id, ConstantCc.TER_STATUS_CONN, uuid, uuidCallee,updateId);
			updateMapTerStatus(updateId, ConstantCc.TER_STATUS_CONN, uuidCallee, uuid,id);
		}
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@");
		new ToolsUtil().printTerMap();
		System.out.println("==========================");

	}

	public void updateMapTerStatus(String id, String status) {
		TerStatusInfo terInfo = ServerDataMap.terStatusMap.get(id);
		terInfo.setStatus(status);
	}

	public void updateMapTerStatus(String id, String status, String uuid, String puid,String peer) {
		TerStatusInfo terInfo = ServerDataMap.terStatusMap.get(id);
		terInfo.setStatus(status);
		terInfo.setCallUUid(uuid);
		terInfo.setPeerUUid(puid);
		terInfo.setPeerNumber(peer);
	}

	public void makeNotifyTerStatus(String id, String status) throws InterruptedException {
		StringBuffer buff = new StringBuffer();
		buff.append(ConstantCc.SYS_NOTIFY_TER_STATUS);
		buff.append(ConstantCc.SYS_SPLIT);
		buff.append(id);
		buff.append(ConstantCc.SYS_SPLIT);
		buff.append(status);
		buff.append(ConstantCc.SYS_TAIL_END);

		ClientDataMap.fsNotifySendQueue.put(buff.toString());
	}

}
