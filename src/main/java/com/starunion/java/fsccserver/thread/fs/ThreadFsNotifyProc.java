package com.starunion.java.fsccserver.thread.fs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.po.ClientNotifyMessageCc;
import com.starunion.java.fsccserver.po.TerStatusInfo;
import com.starunion.java.fsccserver.service.fs.ProcFsNotifyService;
import com.starunion.java.fsccserver.util.ClientDataMap;
import com.starunion.java.fsccserver.util.ConstantCc;
import com.starunion.java.fsccserver.util.ServerDataMap;
import com.starunion.java.fsccserver.util.ToolsUtil;

/**
 * @author Lings
 * @date Mar 11, 2016 10:59:46 AM
 * 
 */
@Service
public class ThreadFsNotifyProc extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(ThreadFsNotifyProc.class);

	@Autowired
	ProcFsNotifyService procFsNotifyService;
	
	public ThreadFsNotifyProc() {

	}

	/**
	 * @desc Event-Name [CHANNEL_CALLSTATE]
	 * @desc Answer-State [ringing][early][answered][hangup]
	 * @desc Caller-Direction [inbound][outbound]
	 * @desc Caller-ANI
	 * @desc Caller-Caller-ID-Number
	 * @desc Caller-Orig-Caller-ID-Number
	 * @desc Caller-Callee-ID-Number
	 * @desc Caller-Destination-Number
	 * @desc Caller-Unique-ID
	 * @desc Other-Leg-Unique-ID
	 * 
	 */
	public void run() {
		try {
			while (!Thread.interrupted()) {
				// TimeUnit.MILLISECONDS.sleep(100 + rand.nextInt(500));
				logger.debug("i am wating on recv Queue.....");
				Map<String, String> eventMap = ClientDataMap.fsNotifyRecvQueue.take();
				logger.debug("get a notify with Event-Name : {} ", eventMap.get("Event-Name"));
				String caller = "";
				String callee = "";
				String uuidCaller = "";
				String uuidCallee = "";

				if (eventMap.get(ConstantCc.FS_EVENT_HEAD).equals("CHANNEL_CALLSTATE")) {
					if (eventMap.get("Answer-State").equals("ringing")
							&& eventMap.get("Caller-Direction").equals("outbound")) {
						// update callee status
						callee = eventMap.get("Caller-Callee-ID-Number");
						uuidCallee = eventMap.get("Caller-Unique-ID");
						procFsNotifyService.updateMapTerStatus(callee, uuidCallee, ConstantCc.TER_STATUS_RING);
						// update caller status
						caller = eventMap.get("Caller-Caller-ID-Number");
						uuidCaller = eventMap.get("Other-Leg-Unique-ID");
						procFsNotifyService.updateMapTerStatus(caller, uuidCaller, ConstantCc.TER_STATUS_EARLY);
						
						new ToolsUtil().printTerMap();
						
						// make  caller status notify
						procFsNotifyService.makeNotifyTerStatus(caller, ConstantCc.TER_STATUS_EARLY);
						// make  callee status notify
						procFsNotifyService.makeNotifyTerStatus(callee, ConstantCc.TER_STATUS_RING);

					} else if (true) {

					}
				} else {
					logger.debug("receive unknown Event-Name : {}", eventMap.get("Event-Name"));
				}
				logger.debug("receive notify :\n{}", eventMap);
				ClientDataMap.fsNotifySendQueue.put(eventMap.get("Event-Name") + "\n");
			}
		} catch (InterruptedException e) {

		}
		System.out.println("Butterer off");
	}
	
}
