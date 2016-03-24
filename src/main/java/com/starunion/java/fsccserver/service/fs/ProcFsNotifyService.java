package com.starunion.java.fsccserver.service.fs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.po.TerParkingInfo;
import com.starunion.java.fsccserver.po.TerStatusInfo;
import com.starunion.java.fsccserver.service.client.ProcClientReqExecCmd;
import com.starunion.java.fsccserver.util.ClientDataMap;
import com.starunion.java.fsccserver.util.ConstantSystem;
import com.starunion.java.fsccserver.util.ServerDataMap;

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
		if (type.equals(ConstantSystem.SYS_EXEC_DEMOLITSH) || type.equals(ConstantSystem.SYS_EXEC_INTERCEPT)) {
			uuidCallee = ServerDataMap.terStatusMap.get(callee).getPeerUUid();
			updateId = ServerDataMap.terStatusMap.get(callee).getPeerNumber();
		} else if (type.equals(ConstantSystem.SYS_EXEC_BRIDGE) || type.equals("12")) {
			uuidCallee = ServerDataMap.terStatusMap.get(callee).getCallUUid();
			updateId = callee;
		}

		int res = procCmd.execCmdUUbridge(uuid, uuidCallee);

		if (res == ConstantSystem.SUCCESS) {
			updateMapTerStatus(id, ConstantSystem.TER_STATUS_CONN, uuid, uuidCallee, updateId);
			updateMapTerStatus(updateId, ConstantSystem.TER_STATUS_CONN, uuidCallee, uuid, id);
		}

	}

	public void updateMapTerStatus(String id, String status) {
		TerStatusInfo terInfo = ServerDataMap.terStatusMap.get(id);
		terInfo.setStatus(status);
	}

	public void updateMapTerStatus(String id, String status, String uuid, String puid, String peer) {
		TerStatusInfo terInfo = ServerDataMap.terStatusMap.get(id);
		if (terInfo != null) {
			terInfo.setStatus(status);
			terInfo.setCallUUid(uuid);
			terInfo.setPeerUUid(puid);
			terInfo.setPeerNumber(peer);
		} else {
			System.out.println("class:ProcFsNotifyService|line:60|not local terminal, outer ?");
		}
	}

	public void makeNotifyTerStatus(String id, String status) throws InterruptedException {
		StringBuffer buff = new StringBuffer();
		buff.append(ConstantSystem.SYS_NOTIFY_TER_STATUS);
		buff.append(ConstantSystem.SYS_SPLIT);
		buff.append(id);
		buff.append(ConstantSystem.SYS_SPLIT);
		buff.append(status);
		buff.append(ConstantSystem.SYS_TAIL_END);

		ClientDataMap.fsNotifySendQueue.put(buff.toString());
	}

}
