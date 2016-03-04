package com.starunion.java.fsccserver.thread;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.util.ClientDataMap;

/**
 * @author Lings
 * @date Mar 2, 2016 5:31:17 PM
 * 
 */
@Service
public class TcpClientResponseThread extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(TcpClientResponseThread.class);

	public TcpClientResponseThread() {

	}

	public TcpClientResponseThread(String name) {
		super(name);
	}

	public void run() {
		logger.debug("client response thread {} started", getName());

		while (true) {
			try {
				// logger.debug("listen on the queue...");
				String str = ClientDataMap.clientResponseQueue.poll();
				if (str != null) {
					logger.debug("response queue received message : {}", str);
					String[] parts = str.split(":");
					String clt = parts[1];
					String body = parts[2];
					Socket socket = ClientDataMap.clientSocketMap.get(clt);
					BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "gbk"));
					out.write(body + "\n");
					out.flush();
					logger.debug("proc response send it to the client !!!:{}", body);
				}
				Thread.sleep(1);
			} catch (Exception e) {
				logger.error("thread {} throw Exception. e: ", getName(), e);
			}
		}
	}
}
