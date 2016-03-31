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

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.starunion.java.fsccserver.msg.MsgBase;
import com.starunion.java.fsccserver.msg.cc.ReqMsgCcLogin;
import com.starunion.java.fsccserver.po.ClientRequestMessageCc;
import com.starunion.java.fsccserver.service.LoginAndOutService;
import com.starunion.java.fsccserver.service.client.ClientReqMsgCheckService;
import com.starunion.java.fsccserver.thread.client.SocketClientTcpJsonThread;
import com.starunion.java.fsccserver.thread.client.SocketClientTcpThread;
import com.starunion.java.fsccserver.util.ClientDataMap;
import com.starunion.java.fsccserver.util.ConstantSystem;
import com.starunion.java.fsccserver.util.SpringContextUtil;

@Service
public class SocketServerTcpJsonThread extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(SocketServerTcpThread.class);

	@Autowired
	LoginAndOutService loginService;
	@Autowired
	ClientReqMsgCheckService requestCheckService;

	private ServerSocket serverSocket = null;

	public SocketServerTcpJsonThread() {

	}

	private void checkThread(String id, Socket clientSocket) {
		SocketClientTcpThread thread = ClientDataMap.clientThreadMap.get(id);

		if (thread == null || !thread.isAlive()) {
			logger.info("THREAD for the client is NOT EXISTED, create NEW THREAD.");
			// TcpClientSocketThread clientThread = new
			// TcpClientSocketThread(id,clientSocket);
			SocketClientTcpJsonThread clientThread = SpringContextUtil.getApplicationContext()
					.getBean("socketClientTcpJsonThread", SocketClientTcpJsonThread.class);
			clientThread.setClientId(id);
			clientThread.setName("CcClientThread-" + id);
			clientThread.setClientSocket(clientSocket);
			clientThread.start();
			ClientDataMap.clientThreadJsonMap.put(id, clientThread);

		} else {
			logger.info("THREAD for the client is EXISTED, change the relation SOCKET info.");
			thread.setClientSocket(clientSocket);
		}
	}

	private void checkSocket(Socket clientSocket) {
		logger.info("CLIENT HAD BEEN BIND , WAIT THE [ccLogin] MESSAGE...");
		try {
			if (!ClientDataMap.clientSocketMap.containsValue(clientSocket)) {
				String line = null;
				BufferedReader is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				Gson gson = new Gson();
				if ((line = is.readLine()) != null) {
					logger.info("receive request message : {}", line);
					MsgBase msg = gson.fromJson(line, MsgBase.class);
					logger.debug("type:{},from:{},id:{}",msg.getMsgType(),msg.getClientId(),msg.getMsgId());
					if (msg.getMsgType() != null && msg.getMsgType().equals(ConstantSystem.CC_LOG_IN)) {
						/** check the ccLogin request */
						ReqMsgCcLogin login = gson.fromJson(line, ReqMsgCcLogin.class);
						logger.debug("from:{},psd:{}",login.getClientId(), login.getPassword());
						if (loginService.AgentLogin(login.getClientId(), login.getPassword()) == ConstantSystem.SUCCESS) {
							BufferedWriter out = new BufferedWriter(
									new OutputStreamWriter(clientSocket.getOutputStream(), ConstantSystem.CODEC_UTF8));
							out.write("loginSuccess\n");
							out.flush();
							Socket sock = ClientDataMap.clientSocketMap.get(login.getClientId());
							if (sock == null) {

								logger.info("SOCKET info is NOT EXISTED, add to the map.");
								ClientDataMap.clientSocketMap.put(login.getClientId(), clientSocket);

								checkThread(login.getClientId(), clientSocket);

							} else if (sock != clientSocket) {

								logger.info("SOCKET info is EXISTED, seems to be change.");
								ClientDataMap.clientSocketMap.put(login.getClientId(), clientSocket);
								logger.debug("client {} 's socket changed, port from {} to {}, update this info",
										login.getClientId(), sock.getPort(), clientSocket.getPort());

								// :TODO the close action is hard,make the
								// clientThread throw,
								// may need define a notify to the
								// client,and
								// closed by the client.
								sock.close();
								/**
								 * it seems here must sleep for blocking socket
								 * release , or the blocking thread may not see
								 * the new one
								 */
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								/**
								 * check whether has avaliable thread to use
								 */
								checkThread(login.getClientId(), clientSocket);
							}
						}

					} else {
						logger.info("non-standard request message, close the socket connection !");
						clientSocket.close();
					}
					/** close the in BufferedReader */
					// is.close();
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
				serverSocket = new ServerSocket(ConstantSystem.TCP_JSON_SERV_PORT);
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
