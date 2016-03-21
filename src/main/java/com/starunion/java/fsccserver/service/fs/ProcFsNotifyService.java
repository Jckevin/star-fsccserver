package com.starunion.java.fsccserver.service.fs;

import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.po.TerStatusInfo;
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
	
	public void updateMapTerStatus(String id, String status) {
		TerStatusInfo terInfo = ServerDataMap.terStatusMap.get(id);
		terInfo.setStatus(status);
	}
	
	public void updateMapTerStatus(String id, String status, String uuid) {
		TerStatusInfo terInfo = ServerDataMap.terStatusMap.get(id);
		terInfo.setStatus(status);
		terInfo.setCallUUid(uuid);
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
