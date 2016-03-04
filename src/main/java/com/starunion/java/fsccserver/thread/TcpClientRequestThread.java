package com.starunion.java.fsccserver.thread;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.util.ClientDataMap;

/**
 * @author Lings
 * @date Mar 2, 2016 4:42:20 PM
 * 
 */
@Service
public class TcpClientRequestThread extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(TcpClientRequestThread.class);

	public TcpClientRequestThread() {

	}

	public TcpClientRequestThread(String name) {
		super(name);
	}

	public void run() {
		logger.debug("client request thread {} started", getName());

		while (true) {
			try {
//				logger.debug("listen on the queue...");
				String str = ClientDataMap.clientRequestQueue.poll();
				if(str != null){
					logger.debug("request queue get message : {}", str);
					String[] parts = str.split(":");
					String clt = parts[1];
					String body = parts[2];
					if(parts[0].equals("ccLogin")){
						Thread.sleep(10000);
						ClientDataMap.clientResponseQueue.offer(str+"hahaha");
						logger.debug("process long time job over : {}", str);
					}else{
						ClientDataMap.clientResponseQueue.offer(str);
					}
					
					logger.debug("proc request and put response to the responseQueue : {}", str);
				}
				Thread.sleep(2000);
			} catch (Exception e) {
				logger.error("thread {} throw Exception. e: ", getName(), e);
			}
		}
	}
}
