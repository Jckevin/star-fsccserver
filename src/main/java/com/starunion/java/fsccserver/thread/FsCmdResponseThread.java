package com.starunion.java.fsccserver.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.util.ClientDataMap;

/** 
* @author Lings  
* @date Mar 8, 2016 12:10:36 PM 
* 
*/
@Service
public class FsCmdResponseThread extends Thread{
	private static final Logger logger = LoggerFactory.getLogger(FsCmdRequestThread.class);
	
	@Override
	public void run() {
		logger.debug("client request thread {} started", getName());

		while (true) {
			try {
				logger.debug("listen on the response queue...");
				ClientDataMap.fsCmdResponseQueue.wait();
				//proc logic
				
//				ClientDataMap.fsCmdResponseQueue.add("DANGDANGDANG");

			} catch (Exception e) {
				logger.error("thread {} throw Exception. e: ", getName(), e);
			}
		}
	}

}
