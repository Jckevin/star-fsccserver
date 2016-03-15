package com.starunion.java.fsccserver.thread;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.po.ClientBindInfo;
import com.starunion.java.fsccserver.util.ClientDataMap;

/** 
* @author Lings  
* @date Mar 11, 2016 3:21:11 PM 
* 
*/
@Service
public class ThreadNotifySendToClient extends Thread{
	private static final Logger logger = LoggerFactory.getLogger(ThreadNotifySendToClient.class);

	public ThreadNotifySendToClient() {

	}

	public void run() {
		try {
			while (!Thread.interrupted()) {
				// TimeUnit.MILLISECONDS.sleep(100 + rand.nextInt(500));
				logger.debug("i am wating on send Queue.....");
				String msg = ClientDataMap.fsNotifySendQueue.take();
				logger.debug("got one send message : {} ",msg);
				ConcurrentHashMap<String, Socket> map = ClientDataMap.clientSocketMap;
				logger.debug("now binding client size {}",map.size());
				Iterator<Map.Entry<String, Socket>> iter = map.entrySet().iterator();
				while (iter.hasNext()) {
					logger.debug("send notify to client : {}", msg);
					Map.Entry<String, Socket> entry = iter.next();
					try {
						entry.getValue().getOutputStream().write(msg.getBytes());
						entry.getValue().getOutputStream().flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (InterruptedException e) {

		}
		System.out.println("Butterer off");
	}
}
