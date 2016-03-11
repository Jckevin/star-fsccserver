package com.starunion.java.fsccserver.thread.runable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.service.ClientRequestService;
import com.starunion.java.fsccserver.util.ClientDataMap;
import com.starunion.java.fsccserver.util.ConstantCc;

/**
 * @author Lings
 * @date Mar 8, 2016 3:49:56 PM
 * 
 */
@Scope("prototype")
@Service
public class TcpClientSocketRunable implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(TcpClientSocketRunable.class);

	private Socket clientSocket;
	private String clientId;
	private String threadName;
	@Autowired
	ClientRequestService reqClientService;

	private long firsttime = 0;
	private boolean isFrist = true;

	public TcpClientSocketRunable() {

	}

	public TcpClientSocketRunable(String id, Socket client, String name) {
		this.clientId = id;
		this.clientSocket = client;
		this.threadName = name;
	}

	public void run() {
		logger.debug("client socket process thread {} started", threadName);

		while (true) {
			try {
				if (clientSocket == null || clientSocket.isClosed()) {
					if (isFrist) {
						firsttime = System.currentTimeMillis();
						isFrist = false;
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						continue;
					} else {
						long interval = System.currentTimeMillis() - firsttime;
						// socket disconnect, thread hold one minute
						if (interval > ConstantCc.TIMEOUT_CLT_SOCK) {
							Thread.interrupted();
							logger.debug("after interrupt ,thread status = {}",Thread.currentThread().getState());
//							Thread.currentThread().destroy();
							ClientDataMap.clientThreadMap.remove(clientId);
							break;
						} else {
							logger.debug("the socket closed, wait for 5 s...");
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							continue;
						}
					}
//					logger.debug("client id = {}",clientId);
////					ClientDataMap.clientThreadMap.get(clientId).interrupt();
//					Thread.interrupted();
//					logger.debug("after interrupt ,thread status = {}", Thread.currentThread().getState());
//					// Thread.currentThread().destroy();
//					// ClientDataMap.clientThreadMap.remove(clientId);
//					ClientDataMap.clientThreadMap.remove(clientId);
//					break;
				} else {
					if (!isFrist) {
						isFrist = true;
					}
					String line = null;
					StringBuffer sendBuff = new StringBuffer();
					BufferedReader is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					BufferedWriter out = new BufferedWriter(
							new OutputStreamWriter(clientSocket.getOutputStream(), ConstantCc.CODEC_UTF8));
					while ((line = is.readLine()) != null) {
						if (line.length() > 0) {
							logger.info("receive client request message : {}", line);
							// RequestClientService reqClientService = new
							// RequestClientService();
							sendBuff = reqClientService.procClientRequest(line);
							out.write(sendBuff.toString());
							out.flush();
							logger.info("send client response message : {}", sendBuff.toString());
							sendBuff.delete(0, sendBuff.length());
						} else {
							logger.debug("get special characters like [\\r][\\n] etc, drop it.");
						}
					}

					/** close the clientSocket. */
					clientSocket.close();
					ClientDataMap.clientSocketMap.remove(clientId);
					logger.debug(
							"one client initiactively closed , now binding client count : {}. and client socket count : {}",
							ClientDataMap.clientSocketMap.size(), ClientDataMap.clientThreadMap.size());

				}
			} catch (SocketException e) {
				try {
					clientSocket.close();
					clientSocket = null;
					logger.error("thread {} throw SocketException, e :", threadName, e);
				} catch (IOException e1) {
					logger.error("thread {} closed IOException, e : ", threadName, e1);
				}
			} catch (IOException e) {
				logger.error("thread connection {} throw IOException, e: ", threadName, e);
			}
		}
		logger.debug(
				"one client status closed , now binding client count : {}. and client socket count : {}",
				ClientDataMap.clientSocketMap.size(), ClientDataMap.clientThreadMap.size());
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		logger.debug("somebody set me now me = {}",clientId);
		this.clientId = clientId;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

}
