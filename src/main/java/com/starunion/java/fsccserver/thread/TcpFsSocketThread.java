package com.starunion.java.fsccserver.thread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.BindException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.service.FsNotifyService;
import com.starunion.java.fsccserver.service.MessageFsNotifyService;
import com.starunion.java.fsccserver.service.ProcFsResponse;
import com.starunion.java.fsccserver.util.ClientDataMap;
import com.starunion.java.fsccserver.util.ConfigManager;

/**
 * @author Lings
 * @date Mar 10, 2016 6:18:49 PM
 * 
 */
@Service
public class TcpFsSocketThread extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(TcpFsSocketThread.class);
	@Autowired
	MessageFsNotifyService msgService;
	private BufferedWriter out = null;

	public TcpFsSocketThread() {

	}
	
	@Override
	public void run() {
		try {
			String ipAddr = ConfigManager.getInstance().getFsAddr();
			int ipPort = Integer.parseInt(ConfigManager.getInstance().getFsPort());
			Socket fsClient = new Socket(ipAddr, ipPort);

			BufferedReader in = new BufferedReader(new InputStreamReader(fsClient.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(fsClient.getOutputStream()));
			StringBuffer notifyBuffer = new StringBuffer();
			String line = null;
			try {
				/** this logic is beautiful for me, hold on! keep on! */
				while ((line = in.readLine()) != null) {
					notifyBuffer.append(line);
					notifyBuffer.append("\n");
					if (line.equals("")) {
						/**
						 * it seems this condition is enough for message end.
						 */
						Map<String, String> respMap = new HashMap<String, String>();
						logger.debug("receive response message from FreeSWITCH:======>\n{}", notifyBuffer.toString());
						respMap = msgService.parseFsResponse(notifyBuffer);
						String contentType = respMap.get("Content-Type");
						if (contentType != null) {
							if (contentType.equals("auth/request")) {
								fsSendCommand("auth ClueCon\n\n");
							} else if (contentType.equals("command/reply")) {
								String reply = respMap.get("Reply-Text");
								String[] parts = reply.split(" ");
								// if (parts[0].equals("+OK")) {
								// logger.debug("successful reply");
								// } else {
								// logger.debug("unsuccessful reply");
								// }
								if (parts[1].equals("accepted")) {
									fsSendCommand("event plain HEARTBEAT\n\n");
									fsSendCommand("event plain CHANNEL_CALLSTATE\n\n");
									fsSendCommand("event plain CHANNEL_PARK\n\n");
									fsSendCommand("event plain CUSTOM sofia::register\n\n");
								}
							} else {
								logger.debug("get content type [{}] without process.", contentType);
							}
						} else {
							String eventType = respMap.get("Event-Name");
							if (eventType != null) {
								logger.debug("get a notify message {} put it to blocking queue.", eventType);
								try {
									ClientDataMap.fsNotifyRecvQueue.put(respMap);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}

						/**
						 * this step is important ,or memory leak and useless
						 * message .
						 */
						notifyBuffer.delete(0, notifyBuffer.length());
					}
				}
			} catch (IOException e) {
				fsClient.close();
				e.printStackTrace();
			}
		} catch (BindException e) {
			// fsClient.close();
			e.printStackTrace();
		} catch (IOException e) {
			// fsClient.close();
			e.printStackTrace();
		}

	}

	public void fsSendCommand(String command) {
		try {
			out.write(command);
			out.flush();
			logger.info("command sendto FreeSWITCH : {}", command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
