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
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.service.MessageFsNotifyService;
import com.starunion.java.fsccserver.service.ProcClientReqCmd;
import com.starunion.java.fsccserver.service.ProcFsResponse;
import com.starunion.java.fsccserver.util.ConfigManager;

/**
 * @author Lings
 * @date Mar 8, 2016 1:37:37 PM
 * 
 */
@Service
public class FsCmdRequestCallable implements Callable<String> {
	private static final Logger logger = LoggerFactory.getLogger(FsCmdRequestCallable.class);

	BufferedWriter out = null;
	private String sendCmd;
	@Autowired
	MessageFsNotifyService msgService;

	public FsCmdRequestCallable() {

	}

	@Override
	public String call() throws Exception {

		String result = "ABCDEFG";
		logger.debug("get cmd = {}", sendCmd);
		try {
			String ipAddr = ConfigManager.getInstance().getFsAddr();
			int ipPort = Integer.parseInt(ConfigManager.getInstance().getFsPort());
			Socket fsClient = new Socket(ipAddr, ipPort);

			BufferedReader in = new BufferedReader(new InputStreamReader(fsClient.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(fsClient.getOutputStream()));
			StringBuffer respBuffer = new StringBuffer();
			String line = null;

			try {
				/** this logic is beautiful for me, hold on! keep on! */
				while ((line = in.readLine()) != null) {
					respBuffer.append(line);
					respBuffer.append("\n");
					if (line.equals("")) {
						Map<String, String> respMap = new HashMap<String, String>();
						logger.debug("receive response message from FreeSWITCH:======>\n{}", respBuffer.toString());
						respMap = msgService.parseFsResponse(respBuffer);
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
									fsSendCommand(sendCmd);
								} else if (parts[0].equals("+OK")) {
									result = "success";
									break;
								}
							} else {
								logger.debug("get content type [{}] without process.", contentType);
							}
						}
						
						respBuffer.delete(0, respBuffer.length());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 
		} catch (BindException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		if (sendCmd.startsWith("bgapi originate")) {
//			Thread.sleep(15000);
//			logger.debug("long task process over...");
//		} else {
//			Thread.sleep(1500);
//			logger.debug("short task over");
//		}

		return result;
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

	public String getSendCmd() {
		return sendCmd;
	}

	public void setSendCmd(String sendCmd) {
		this.sendCmd = sendCmd;
	}

}
