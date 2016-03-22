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
				String caller = "";
				String callee = "";
				String uuidCaller = null;
				String uuidCallee = null;
				/**
				 * Channel-Status(Number):CS_CONSUME_MEDIA(7) as this happend by
				 * callee ringing, so (Caller-Unique-ID) for callee,
				 * (Other-Leg-Unique-ID) for caller
				 */
				if (eventMap.get(ConstantCc.FS_EVENT_HEAD).equals("CHANNEL_CALLSTATE")) {
					if (eventMap.get("Answer-State").equals("ringing")
							&& eventMap.get("Channel-State-Number").equals("7")) {
						caller = eventMap.get("Caller-Caller-ID-Number");
						callee = eventMap.get("Caller-Callee-ID-Number");
						uuidCallee = eventMap.get("Caller-Unique-ID");
						if (!caller.equals(ConstantCc.FS_DEF_NUMBER)) {
							uuidCaller = eventMap.get("Other-Leg-Unique-ID");
							// update caller status
							procFsNotifyService.updateMapTerStatus(caller, ConstantCc.TER_STATUS_EARLY, uuidCaller,
									uuidCallee, callee);
							// make caller status notify
							procFsNotifyService.makeNotifyTerStatus(caller, ConstantCc.TER_STATUS_EARLY);

							// update callee status
							procFsNotifyService.updateMapTerStatus(callee, ConstantCc.TER_STATUS_RING, uuidCallee,
									uuidCaller, caller);
							// make callee status notify
							procFsNotifyService.makeNotifyTerStatus(callee, ConstantCc.TER_STATUS_RING);
						} else {
							/**
							 * for caller 000, no need update the caller status,
							 * and this situation callee uuid is unknown.
							 */
							procFsNotifyService.updateMapTerStatus(callee, ConstantCc.TER_STATUS_RING, uuidCallee, null,
									"000");
							procFsNotifyService.makeNotifyTerStatus(callee, ConstantCc.TER_STATUS_RING);
						}

						new ToolsUtil().printTerMap();

					} else if (eventMap.get("Answer-State").equals("answered")
							&& eventMap.get("Channel-State-Number").equals("7")) {
						caller = eventMap.get("Caller-Caller-ID-Number");
						if (!caller.equals(ConstantCc.FS_DEF_NUMBER)) {
							// for caller 000, no need update the caller status
							procFsNotifyService.updateMapTerStatus(caller, ConstantCc.TER_STATUS_CONN);
							procFsNotifyService.makeNotifyTerStatus(caller, ConstantCc.TER_STATUS_CONN);
						}
						callee = eventMap.get("Caller-Callee-ID-Number");
						procFsNotifyService.updateMapTerStatus(callee, ConstantCc.TER_STATUS_CONN);
						procFsNotifyService.makeNotifyTerStatus(callee, ConstantCc.TER_STATUS_CONN);

						new ToolsUtil().printTerMap();

					} else if (eventMap.get("Answer-State").equals("answered")
							&& eventMap.get("Channel-State-Number").equals("5")) {
						/**
						 * @desc A call B, B ringing. originate C, connect and
						 *       park. uuid_bridge C to A. B hangup at the time.
						 *       and exchange happend.
						 * @desc the caller become C, callee become A.
						 * @param CS_EXCHANGE_MEDIA(5)
						 */
						// caller = eventMap.get("Caller-Caller-ID-Number");
						callee = eventMap.get("Caller-Callee-ID-Number");
						// uuidCaller = eventMap.get("Other-Leg-Unique-ID");
						// uuidCallee = eventMap.get("Caller-Unique-ID");
						// procFsNotifyService.updateMapTerStatus(callee,
						// ConstantCc.TER_STATUS_CONN, uuidCallee,
						// uuidCaller);
						procFsNotifyService.makeNotifyTerStatus(callee, ConstantCc.TER_STATUS_CONN);

						// procFsNotifyService.updateMapTerStatus(caller,
						// ConstantCc.TER_STATUS_CONN, uuidCaller,
						// uuidCallee);

						new ToolsUtil().printTerMap();

					} else if (eventMap.get("Answer-State").equals("hangup")
							&& eventMap.get("Call-Direction").equals("inbound")) {
						/** for ANI = 000, no hangup for inbound */
						caller = eventMap.get("Caller-Caller-ID-Number");
						procFsNotifyService.updateMapTerStatus(caller, ConstantCc.TER_STATUS_REGED, null, null, null);
						procFsNotifyService.makeNotifyTerStatus(caller, ConstantCc.TER_STATUS_REGED);

						new ToolsUtil().printTerMap();

					} else if (eventMap.get("Answer-State").equals("hangup")
							&& eventMap.get("Call-Direction").equals("outbound")) {
						callee = eventMap.get("Caller-Callee-ID-Number");
						// this logic may FreeSWITCH notify BUG when call by cmd
						// [originate]
						if (!callee.equals(ConstantCc.FS_DEF_NUMBER)) {
							if(eventMap.get("Caller-ANI").equals(ConstantCc.FS_DEF_NUMBER)){
								callee = eventMap.get("Caller-Destination-Number");
								procFsNotifyService.updateMapTerStatus(callee, ConstantCc.TER_STATUS_REGED, null, null,
										null);
								procFsNotifyService.makeNotifyTerStatus(callee, ConstantCc.TER_STATUS_REGED);
							}else{
								procFsNotifyService.updateMapTerStatus(callee, ConstantCc.TER_STATUS_REGED, null, null,
										null);
								procFsNotifyService.makeNotifyTerStatus(callee, ConstantCc.TER_STATUS_REGED);	
							}
						} else {
							caller = eventMap.get("Caller-Caller-ID-Number");
							procFsNotifyService.updateMapTerStatus(caller, ConstantCc.TER_STATUS_REGED, null, null,
									null);
							procFsNotifyService.makeNotifyTerStatus(caller, ConstantCc.TER_STATUS_REGED);
						}

						new ToolsUtil().printTerMap();

					} else {
						logger.debug("for normal call , useless call message?");
					}

				} else if (eventMap.get(ConstantCc.FS_EVENT_HEAD).equals("CHANNEL_PARK")) {
					/**
					 * util now PARK is made by FreeSWITCH, how about from
					 * phone?
					 */

					caller = eventMap.get("Caller-Caller-ID-Number");
					uuidCaller = eventMap.get("Caller-Unique-ID");
					procFsNotifyService.procTerParking(caller, uuidCaller);

				} else {
					logger.debug("receive unknown Event-Name : {}", eventMap.get("Event-Name"));
				}
			}
		} catch (InterruptedException e) {

		}
	}

}
