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
				logger.debug("i am wating on recv freeswitch NOTIFY Queue.....");
				Map<String, String> eventMap = ClientDataMap.fsNotifyRecvQueue.take();
				logger.debug("get a notify with Event-Name : {} ", eventMap.get("Event-Name"));
				String caller = "";
				String callee = "";
				/** Channel-Status(Number):CS_CONSUME_MEDIA(7) */
				if (eventMap.get(ConstantCc.FS_EVENT_HEAD).equals("CHANNEL_CALLSTATE")) {
					if (eventMap.get("Answer-State").equals("ringing")
							&& eventMap.get("Channel-State-Number").equals("7")) {
						String uuidCaller = null;
						String uuidCallee = null;
						caller = eventMap.get("Caller-Caller-ID-Number");
						callee = eventMap.get("Caller-Callee-ID-Number");
						if (!caller.equals(ConstantCc.FS_DEF_NUMBER)) {
							uuidCaller = eventMap.get("Other-Leg-Unique-ID");
							uuidCallee = eventMap.get("Caller-Unique-ID");
							// update caller status
							procFsNotifyService.updateMapTerStatus(caller, ConstantCc.TER_STATUS_EARLY, uuidCaller,
									uuidCallee);
							// make caller status notify
							procFsNotifyService.makeNotifyTerStatus(caller, ConstantCc.TER_STATUS_EARLY);

							// update callee status
							procFsNotifyService.updateMapTerStatus(callee, ConstantCc.TER_STATUS_RING, uuidCallee,
									uuidCaller);
							// make callee status notify
							procFsNotifyService.makeNotifyTerStatus(callee, ConstantCc.TER_STATUS_RING);
						} else {
							uuidCallee = eventMap.get("Caller-Unique-ID");
							procFsNotifyService.updateMapTerStatus(callee, ConstantCc.TER_STATUS_RING, uuidCallee,
									uuidCaller);
							procFsNotifyService.makeNotifyTerStatus(callee, ConstantCc.TER_STATUS_RING);
						}

						new ToolsUtil().printTerMap();

					} else if (eventMap.get("Answer-State").equals("answered")
							&& eventMap.get("Channel-State-Number").equals("7")) {
						caller = eventMap.get("Caller-Caller-ID-Number");
						if (!caller.equals(ConstantCc.FS_DEF_NUMBER)) {
							procFsNotifyService.updateMapTerStatus(caller, ConstantCc.TER_STATUS_CONN);
							procFsNotifyService.makeNotifyTerStatus(caller, ConstantCc.TER_STATUS_CONN);
						}
						callee = eventMap.get("Caller-Callee-ID-Number");
						procFsNotifyService.updateMapTerStatus(callee, ConstantCc.TER_STATUS_CONN);
						procFsNotifyService.makeNotifyTerStatus(callee, ConstantCc.TER_STATUS_CONN);

						new ToolsUtil().printTerMap();

					} else if (eventMap.get("Answer-State").equals("hangup")
							&& eventMap.get("Call-Direction").equals("inbound")) {
						/** for ANI = 000, no hangup for inbound */
						caller = eventMap.get("Caller-Caller-ID-Number");
						procFsNotifyService.updateMapTerStatus(caller, ConstantCc.TER_STATUS_REGED, null, null);
						procFsNotifyService.makeNotifyTerStatus(caller, ConstantCc.TER_STATUS_REGED);

						new ToolsUtil().printTerMap();

					} else if (eventMap.get("Answer-State").equals("hangup")
							&& eventMap.get("Call-Direction").equals("outbound")) {
						callee = eventMap.get("Caller-Callee-ID-Number");
						// this logic may FreeSWITCH notify BUG when call by cmd
						// [originate]
						if (!callee.equals(ConstantCc.FS_DEF_NUMBER)) {
							procFsNotifyService.updateMapTerStatus(callee, ConstantCc.TER_STATUS_REGED, null, null);
							procFsNotifyService.makeNotifyTerStatus(callee, ConstantCc.TER_STATUS_REGED);
						} else {
							caller = eventMap.get("Caller-Caller-ID-Number");
							procFsNotifyService.updateMapTerStatus(caller, ConstantCc.TER_STATUS_REGED, null, null);
							procFsNotifyService.makeNotifyTerStatus(caller, ConstantCc.TER_STATUS_REGED);
						}

						new ToolsUtil().printTerMap();

					} else {
						logger.debug("for normal call , useless call message?");
					}

				} else if (eventMap.get(ConstantCc.FS_EVENT_HEAD).equals("CHANNEL_PARK")) {
					// util now PARK is made by FreeSWITCH, how about from
					// phone?
					caller = eventMap.get("Caller-Caller-ID-Number");
					String uuidCaller = eventMap.get("Caller-Unique-ID");
					procFsNotifyService.procTerParking(caller, uuidCaller);

				} else {
					logger.debug("receive unknown Event-Name : {}", eventMap.get("Event-Name"));
				}
			}
		} catch (InterruptedException e) {

		}
	}

}