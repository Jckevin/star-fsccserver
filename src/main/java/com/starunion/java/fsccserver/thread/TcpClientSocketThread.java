package com.starunion.java.fsccserver.thread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.service.ProcClientRequest;
import com.starunion.java.fsccserver.service.ClientRequestService;
import com.starunion.java.fsccserver.util.ClientDataMap;
import com.starunion.java.fsccserver.util.ConstantCc;

/**
 * @author Lings
 * @date Mar 2, 2016 9:23:50 AM
 * 
 */

@Service
@Scope("prototype")
public class TcpClientSocketThread extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(TcpClientSocketThread.class);

	private Socket clientSocket;
	private String clientId;
	@Autowired
	ClientRequestService clientReqService;

	private long firsttime = 0;
	private boolean isFrist = true;

	public TcpClientSocketThread() {

	}

	public TcpClientSocketThread(String id, Socket client) {
		super("CcClientThread-" + id);
		this.clientId = id;
		this.clientSocket = client;
	}

	public void run() {
		logger.debug("client socket process thread {} started", getName());

		while (true) {
			try {
				if (clientSocket == null || clientSocket.isClosed()) {
					if (isFrist) {
						firsttime = System.currentTimeMillis();
						isFrist = false;
						sleep(5000);
						continue;
					} else {
						long interval = System.currentTimeMillis() - firsttime;
						// socket disconnect, thread hold one minute
						if (interval > ConstantCc.TIMEOUT_CLT_SOCK) {
							interrupt();
							logger.debug("after interrupt ,thread status = {}",Thread.currentThread().getState());
//							Thread.currentThread().destroy();
							ClientDataMap.clientThreadMap.remove(clientId);
							logger.debug("after interrupt, now binding client thread count : {}",ClientDataMap.clientThreadMap.size());
							break;
							/**:TODO why sleep(1) equals break operation ?*/
//							sleep(1);
						} else {
							logger.debug("the socket closed, wait for 5 s...");
							sleep(5000);
							continue;
						}
					}
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
//							RequestClientService reqClientService = new RequestClientService();
							sendBuff = clientReqService.procClientRequest(line);
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
					logger.error("thread {} throw SocketException, e :", getName(), e);
				} catch (IOException e1) {
					logger.error("thread {} closed IOException, e : ", getName(), e1);
				}
			} catch (IOException e) {
				logger.error("thread connection {} throw IOException, e: ", getName(), e);
			} catch (InterruptedException e) {
				break;
			}
		}
		logger.debug(
				"one client initactively closed , now binding client count : {}. and client socket count : {}",
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
		this.clientId = clientId;
	}
}
