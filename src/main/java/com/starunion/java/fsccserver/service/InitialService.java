package com.starunion.java.fsccserver.service;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.po.TerStatusInfo;
import com.starunion.java.fsccserver.service.client.ProcClientReqQuryCmd;
import com.starunion.java.fsccserver.util.ConstantCc;
import com.starunion.java.fsccserver.util.ServerDataMap;

/**
 * @author Lings
 * @date Mar 18, 2016 11:44:30 AM
 * 
 */
@Service
public class InitialService {
	private static final Logger logger = LoggerFactory.getLogger(InitialService.class);
	@Autowired
	ProcClientReqQuryCmd procQueryCmd;

	/**
	 * 
	 * @inner message structure (1.)userid|(2.)context|(3.)domain |(4.)group
	 *        |(5.)contact|(6.)callgroup|(7.)effective_caller_id_name|(8.)
	 *        effective_caller_id_number (1.)800
	 *        |(2.)default|(3.)192.168.8.12|(4.)default|(5.)sofia/internal/sip:
	 *        800@192.168.8.166:5060|(6.)techsupport|(7.)800|(8.)800
	 * 
	 *        (END.) +OK
	 * @date 2016-03-16
	 * @author Lings
	 */
	public void initTerInfo() {
		String res = procQueryCmd.getServTerList();
		String[] parts = res.split("\n");
		int len = parts.length - 1;
		String[] subParts = new String[len];
		// as fs response the first line is describe ,skip them
		System.arraycopy(parts, 1, subParts, 0, len);
		for (String p : subParts) {
			if (p.contains("|")) {
				TerStatusInfo terInfo = new TerStatusInfo();
				String[] subs = p.split("\\|");

				if (subs[4].equals("error/user_not_registered")) {
					terInfo.setStatus(ConstantCc.TER_STATUS_UNREG);
				} else {
					terInfo.setStatus(ConstantCc.TER_STATUS_REGED);
				}
				ServerDataMap.terStatusMap.put(subs[0], terInfo);

			} else {
				logger.debug("last line , do nothing.");
			}
		}

	}

}
