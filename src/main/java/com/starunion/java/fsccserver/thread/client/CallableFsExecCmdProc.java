package com.starunion.java.fsccserver.thread.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.BindException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.service.fs.FsNotifyMsgCheckService;
import com.starunion.java.fsccserver.util.ConfigManager;
import com.starunion.java.fsccserver.util.ConstantSystem;

/** 
* @author Lings  
* @date Mar 17, 2016 5:30:27 PM 
* 
*/
@Service
public class CallableFsExecCmdProc implements Callable<Integer>{
	
		private static final Logger logger = LoggerFactory.getLogger(CallableFsExecCmdProc.class);

		BufferedWriter out = null;
		private String sendCmd;
		@Autowired
		FsNotifyMsgCheckService msgService;
		boolean isStandard = true;

		@Override
		public Integer call() throws Exception {
			int result = ConstantSystem.FAILED;
			logger.debug("get cmd = {}", sendCmd);
			try {
				String ipAddr = ConfigManager.getInstance().getFsAddr();
				Socket fsClient = new Socket(ipAddr, ConstantSystem.FS_SERV_PORT);

				BufferedReader in = new BufferedReader(new InputStreamReader(fsClient.getInputStream()));
				out = new BufferedWriter(new OutputStreamWriter(fsClient.getOutputStream()));
				StringBuffer respBuffer = new StringBuffer();
				String line = null;
				int nonStandardMsgLen = 0;

				try {
					/** this logic is beautiful for me, hold on! keep on! */
					while ((line = in.readLine()) != null) {
						respBuffer.append(line);
						respBuffer.append("\n");
						if (isStandard) {
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
										if (parts[1].equals("accepted")) {
											fsSendCommand(sendCmd);
										} else if(parts[0].equals(ConstantSystem.FS_CMD_SUCC)){
											result = ConstantSystem.SUCCESS;
											break;
										}
									} else if (contentType.equals("api/response")) {
										logger.debug("this type message mean that follow byte without end indicator [\\n]");
										nonStandardMsgLen = Integer.parseInt(respMap.get("Content-Length").trim());
										isStandard = false;
									} else {
										logger.debug("get content type [{}] without process.", contentType);
									}
								} else {
									logger.debug("receive message without Content-Type");
								}

								respBuffer.delete(0, respBuffer.length());
							}
						} else {
							if (respBuffer.length() == nonStandardMsgLen) {
								logger.debug("receive [whole] non-standard response message from FreeSWITCH:======>\n{}",
										respBuffer.toString());
								isStandard = true;
								if(respBuffer.toString().startsWith(ConstantSystem.FS_CMD_SUCC)){
									result = ConstantSystem.SUCCESS;
								}
								respBuffer.delete(0, respBuffer.length());
								break;
							}
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
			logger.debug("callable return result = {}",result);
			return result;
		}

		public void fsSendCommand(String command) {
			try {
				out.write(command);
				out.flush();
				logger.info("template command sendto FreeSWITCH : {}", command);
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
