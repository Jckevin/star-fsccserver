package com.starunion.java.fsccserver.thread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.po.ClientRequestMessageCc;
import com.starunion.java.fsccserver.service.LoginAndOutService;
import com.starunion.java.fsccserver.service.RequestMessageService;
import com.starunion.java.fsccserver.util.ClientDataMap;
import com.starunion.java.fsccserver.util.ConstantCc;

/*
 * @Author LingSong 
 * @Date   2016-03-01
 * 
 */
@Service
public class TcpServerSocketThread extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(TcpServerSocketThread.class);

	 @Autowired
	 LoginAndOutService loginService;
	 @Autowired
	 RequestMessageService requestCheckService;
	 @Autowired
	 TcpClientSocketThread clientThread;

	private ServerSocket serverSocket = null;

	public TcpServerSocketThread() {

	}

	public TcpServerSocketThread(String name) {
		super(name);
	}

	private void checkThread(String id, Socket clientSocket) {
		TcpClientSocketThread thread = ClientDataMap.clientThreadMap.get(id);

		if (thread == null || !thread.isAlive()) {
			logger.info("THREAD for the client is NOT EXISTED, create NEW THREAD.");
//			clientThread = new TcpClientSocketThread(id,clientSocket);
			clientThread.setClientId(id);
			clientThread.setName("CcClientThread-" + id);
			clientThread.setClientSocket(clientSocket);
			clientThread.start();

			ClientDataMap.clientThreadMap.put(id, clientThread);

		} else {
			logger.info("THREAD for the client is EXISTED, change the relation SOCKET info.");
			thread.setClientSocket(clientSocket);
		}
	}

	private void checkSocket(Socket clientSocket) {
		String line = null;
		BufferedReader is = null;
		logger.info("CLIENT HAD BEEN BIND , WAIT THE [ccLogin] MESSAGE...");
		try {
			if (!ClientDataMap.clientSocketMap.containsValue(clientSocket)) {
				is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				if ((line = is.readLine()) != null) {
					logger.info("receive request message : {}", line);
//					RequestMessageCheckService requestCheckService = new RequestMessageCheckService();
					ClientRequestMessageCc reqMessage = requestCheckService.parseRequestMessage(line);
					/** check the request message whether standard */
					if (reqMessage != null && reqMessage.getType().equals(ConstantCc.CC_LOGIN)) {
						/** check the login request whether success */
//						LoginAndOutService loginService = new LoginAndOutService();
						if (loginService.AgentLogin(reqMessage.getClientId(), reqMessage.getContent())) {

							Socket sock = ClientDataMap.clientSocketMap.get(reqMessage.getClientId());
							if (sock == null) {

								logger.info("SOCKET info is NOT EXISTED, add to the map.");
								ClientDataMap.clientSocketMap.put(reqMessage.getClientId(), clientSocket);
								checkThread(reqMessage.getClientId(), clientSocket);

							} else if (sock != clientSocket) {

								logger.info("SOCKET info is EXISTED, seems to be change.");
								ClientDataMap.clientSocketMap.put(reqMessage.getClientId(), clientSocket);
								logger.debug("client {} 's socket changed, port from {} to {}, update this info",
										reqMessage.getClientId(), sock.getPort(), clientSocket.getPort());

								// :TODO the close action is hard,make the
								// clientThread throw,
								// may need define a notify to the
								// client,and
								// closed by the client.
								sock.close();
								/** it seems here must sleep for blocking socket release ,
								 * or the blocking thread will not see the new one*/
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								/**
								 * check whether has avaliable thread to use
								 */
								checkThread(reqMessage.getClientId(), clientSocket);

							}
							logger.debug(
									"one client login , now binding client count : {}. and client socket count : {}",
									ClientDataMap.clientSocketMap.size(), ClientDataMap.clientThreadMap.size());
						} else {
							logger.info("login failed witch err infomation, close the socket connection !");
							clientSocket.close();
						}

					} else {
						logger.info("non-standard request message, close the socket connection !");
						clientSocket.close();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {

			if (serverSocket == null) {
				serverSocket = new ServerSocket(ConstantCc.TCP_SERV_PORT);
			}

			logger.debug("server tcp thread had been started, wait for client binding...");
			while (true) {
				try {
					Socket clientSocket = serverSocket.accept();

					checkSocket(clientSocket);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (BindException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
