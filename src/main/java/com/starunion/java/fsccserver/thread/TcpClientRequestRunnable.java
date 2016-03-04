package com.starunion.java.fsccserver.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.starunion.java.fsccserver.util.ClientDataMap;

/**
 * @author Lings
 * @date Mar 3, 2016 10:31:02 AM
 * 
 */
public class TcpClientRequestRunnable implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(TcpClientRequestRunnable.class);

	private String name;

	public TcpClientRequestRunnable() {

	}

	public TcpClientRequestRunnable(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		logger.debug("client request thread {} started", getName());

		while (true) {
			try {
				// logger.debug("listen on the queue...");
				String str = ClientDataMap.clientRequestQueue.poll();
				if (str != null) {
					logger.debug("request queue get message : {}", str);
					String[] parts = str.split(":");
					String clt = parts[1];
					String body = parts[2];
					if (parts[0].equals("ccLogin")) {
						Thread.sleep(10000);
						ClientDataMap.clientResponseQueue.offer(str + "hahaha");
						logger.debug("process long time job over : {}", str);
					} else {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
