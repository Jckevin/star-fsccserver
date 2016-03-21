package com.starunion.java.fsccserver.service.client;

import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.po.ClientRequestMessageCc;
import com.starunion.java.fsccserver.util.ConstantCc;

/**
 * @author Lings
 * @date Mar 4, 2016 10:09:58 AM
 * 
 */
@Service
public class MessageClientReqService {
	
	public MessageClientReqService() {

	}

	public boolean isRequestMessage(String cmd) {
		boolean b = false;
		if (!cmd.contains(ConstantCc.SYS_SPLIT)) {
			b = false;
		} else if (cmd.split(ConstantCc.SYS_SPLIT).length < ConstantCc.SYS_REG_MSG_LMT) {
			b = false;
		} else {
			b = true;
		}
		return b;
	}

	public ClientRequestMessageCc parseRequestMessage(String cmd) {
		if (cmd.contains(ConstantCc.SYS_SPLIT)) {
			String[] parts = cmd.split(ConstantCc.SYS_SPLIT);
			if (cmd.split(ConstantCc.SYS_SPLIT).length < ConstantCc.SYS_REG_MSG_LMT) {
				return null;
			} else {
				ClientRequestMessageCc reqMsg = new ClientRequestMessageCc();
				/**
				 * normally, first part is cmd type; second part is identifer,
				 * who send the cmd; third part is cmd body, with callee info
				 * etc.
				 */
				reqMsg.setType(parts[0]);
				reqMsg.setClientId(parts[1]);
				reqMsg.setContent(parts[2]);
				return reqMsg;
			}
		}
		return null;
	}

}