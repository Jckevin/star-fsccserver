package com.starunion.java.fsccserver.thread;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.util.ClientDataMap;

/**
 * @author Lings
 * @date Mar 11, 2016 10:59:46 AM
 * 
 */
@Service
public class TcpFsNotifyProcThread extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(TcpFsNotifyProcThread.class);

	public TcpFsNotifyProcThread() {

	}

	public void run() {
		try {
			while (!Thread.interrupted()) {
				// TimeUnit.MILLISECONDS.sleep(100 + rand.nextInt(500));
				logger.debug("i am wating on recv Queue.....");
				Map<String, String> notify = ClientDataMap.fsNotifyRecvQueue.take();
				logger.debug("got one...event : {} ",notify.get("Event-Name"));
				// t.butter();
				// System.out.println(t);
				// butteredQueue.put(t);
				ClientDataMap.fsNotifySendQueue.put(notify.get("Event-Name")+"\n");
			}
		} catch (InterruptedException e) {

		}
		System.out.println("Butterer off");
	}
}
