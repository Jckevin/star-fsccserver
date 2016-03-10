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

import com.starunion.java.fsccserver.service.ProcClientReqCmd;
import com.starunion.java.fsccserver.service.ProcFsResponse;
import com.starunion.java.fsccserver.util.ConfigManager;

/** 
* @author Lings  
* @date Mar 8, 2016 1:37:37 PM 
* 
*/
@Service
public class FsCmdRequestCallable implements Callable<String>{
	private static final Logger logger = LoggerFactory.getLogger(FsCmdRequestCallable.class);

	BufferedWriter out = null;
	private String sendCmd;
	
	public FsCmdRequestCallable(){
		
	}
	
	public Map<String, String> parseFsResponse(StringBuffer buff) {
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer line = new StringBuffer();
		for (int i = 0; i < buff.length(); i++) {
			char c;
			if ((c = buff.charAt(i)) != '\n') {
				line.append(c);
			} else {
				// logger.debug("Parse line = {}", line);
				String key = "";
				String value = "";
				int pos = line.indexOf(":");
				if (pos != -1) {
					// logger.debug("pos = {}", pos);
					key = line.substring(0, pos);
					// logger.debug("Parse key = {}", key);
					value = line.substring(pos + 1).trim();
					/**
					 * it seems urlDecode can be invoke when need the value
					 * within Map, but for logic design,here seems better.
					 */
					if (value.contains("%")) {
						String newVal;
						try {
							newVal = new String(java.net.URLDecoder.decode(value, "utf-8"));
							map.put(key, newVal);
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						// logger.debug("Parse newValue = {}", newVal);
					} else {
						// logger.debug("Parse value = {}", value);
						map.put(key, value);
					}
				}
				line.delete(0, line.length());
			}
		}
		return map;
	}
	
	@Override
	public String call() throws Exception {

		String result = "ABCDEFG";
		logger.debug("get cmd = {}",sendCmd);
//		try {
//			String ipAddr = ConfigManager.getInstance().getFsAddr();
//			int ipPort = Integer.parseInt(ConfigManager.getInstance().getFsPort());
//			Socket fsClient = new Socket(ipAddr, ipPort);
//
//			BufferedReader in = new BufferedReader(new InputStreamReader(fsClient.getInputStream()));
//			out = new BufferedWriter(new OutputStreamWriter(fsClient.getOutputStream()));
//			StringBuffer respBuffer = new StringBuffer();
//			String line = null;
//			
//			try {
//				/** this logic is beautiful for me, hold on! keep on!*/
//				while ((line = in.readLine()) != null) {
//					respBuffer.append(line);
//					respBuffer.append("\n");
//					if (line.equals("")) {
//						Map<String, String> respMap = new HashMap<String, String>();
//						 logger.debug("receive response message from FreeSWITCH:======>\n{}",
//						 respBuffer.toString());
//						respMap = parseFsResponse(respBuffer);
//						String contentType = respMap.get("Content-Type");
//						if (contentType != null) {
//							if (contentType.equals("auth/request")) {
//								fsSendCommand("auth ClueCon\n\n");
//							} else if (contentType.equals("command/reply")) {
//								String reply = respMap.get("Reply-Text");
//								String[] parts = reply.split(" ");
//								// if (parts[0].equals("+OK")) {
//								// logger.debug("successful reply");
//								// } else {
//								// logger.debug("unsuccessful reply");
//								// }
//								if (parts[1].equals("accepted")) {
//									fsSendCommand(sendCmd);
//								} else if(parts[0].equals("+OK")){
//									result = "INVOKE SUCCESS";
//									break;
//								}
//							} else {
//								logger.debug("get content type [{}] without process.", contentType);
//							}
//						} 
//						/**
//						 * it seems this condition is enough for message end.
//						 */
////						msgProc.procFsResponse(respBuffer);
//						/**
//						 * this step is important ,or memory leak and useless
//						 * message .
//						 */
//						respBuffer.delete(0, respBuffer.length());
//					}
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		} catch (BindException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		if(sendCmd.startsWith("bgapi originate")){
			Thread.sleep(15000);
			logger.debug("long task process over...");
		}else{
			Thread.sleep(1500);
			logger.debug("short task over");
		}
		
		
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
