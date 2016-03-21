package com.starunion.java.fsccserver.util;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.starunion.java.fsccserver.po.TerStatusInfo;
import com.starunion.java.fsccserver.service.InitialService;

/** 
* @author Lings  
* @date Mar 18, 2016 1:02:24 PM 
* 
*/
public class ToolsUtil {
	private static final Logger logger = LoggerFactory.getLogger(ToolsUtil.class);
//	public void printMap(Map map){
//		Iterator<Map.Entry<String, Socket>> iter = map.entrySet().iterator();
//		while (iter.hasNext()) {
//			logger.debug("send notify to client : {}", msg);
//			Map.Entry<String, Socket> entry = iter.next();
//			try {
//				entry.getValue().getOutputStream().write(msg.getBytes());
//				entry.getValue().getOutputStream().flush();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	public void printTerMap(){
		Iterator<Entry<String, TerStatusInfo>> iter = ServerDataMap.terStatusMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, TerStatusInfo> entry = iter.next();

			logger.error("key : {}", entry.getKey());
			logger.error("value status : {},{},{},{},{}", entry.getValue().getStatus(),entry.getValue().getCallDirection(),
					entry.getValue().getCallType(),entry.getValue().getCallUUid(),entry.getValue().getPeerNumber());

		}		
	}

}
