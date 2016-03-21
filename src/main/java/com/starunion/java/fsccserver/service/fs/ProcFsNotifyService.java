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
import com.starunion.java.fsccserver.thread.client.CallableFsQueryCmdProc;
import com.starunion.java.fsccserver.util.ClientDataMap;
import com.starunion.java.fsccserver.util.ConstantCc;
import com.starunion.java.fsccserver.util.ServerDataMap;

/**
 * @author Lings
 * @date Mar 21, 2016 11:17:40 AM
 * 
 */

@Service
public class ProcFsNotifyService {
	@Autowired
	CallableFsQueryCmdProc taskInt;
	
	public void procTerParking(String id, String uuid) {
		TerParkingInfo terInfo = ServerDataMap.terParkingMap.get(id);
		String callee = terInfo.getCallee();
		String type = terInfo.getType();
		StringBuffer buff = new StringBuffer();
		buff.append("bgapi uuid_bridge ");
		buff.append(uuid);
		buff.append(" ");
		if (type.equals(ConstantCc.SYS_EXEC_DEMOLITSH) || type.equals("123")) {
			buff.append(ServerDataMap.terStatusMap.get(callee).getPeerUUid());
		} else if (type.equals(ConstantCc.SYS_EXEC_BRIDGE) || type.equals("12")) {
			buff.append(ServerDataMap.terStatusMap.get(callee).getCallUUid());
		}
		buff.append(ConstantCc.SYS_TAIL_END);
		
		taskInt.setSendCmd(buff.toString());
		Integer result = ConstantCc.FAILED;
		Future<Integer> future = FsCcServer.executor.submit(taskInt);
		try {
			result = future.get(5000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
		}
	}

	public void updateMapTerStatus(String id, String status) {
		TerStatusInfo terInfo = ServerDataMap.terStatusMap.get(id);
		terInfo.setStatus(status);
	}

	public void updateMapTerStatus(String id, String status, String uuid, String puid) {
		TerStatusInfo terInfo = ServerDataMap.terStatusMap.get(id);
		terInfo.setStatus(status);
		terInfo.setCallUUid(uuid);
		terInfo.setPeerUUid(puid);
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
