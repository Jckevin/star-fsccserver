package com.starunion.java.fsccserver.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.beginning.FsCcServer;
import com.starunion.java.fsccserver.thread.CallableFsExecCmdProc;
import com.starunion.java.fsccserver.util.ConstantCc;

/** 
* @author Lings  
* @date Mar 17, 2016 10:45:34 AM 
* 
*/
@Service
public class ProcClientReqQuryCmd {
	private static final Logger logger = LoggerFactory.getLogger(ProcClientReqQuryCmd.class);

	@Autowired
	CallableFsExecCmdProc task;

	public ProcClientReqQuryCmd() {

	}

	/**
	 * @param requester
	 *            check the requester whether has permission.
	 * @param callee
	 *            for sigle one or whole list.
	 * @return useful and simple reply.
	 * 
	 * @inner message structure
	 *        (1.)name|(2.)system|(3.)uuid|(4.)type|(5.)contact|(6.)status|(7.)
	 *        state|(8.) max_no_answer|(9.)
	 *        wrap_up_time|(10.)reject_delay_time|(11.)busy_delay_time|(12.)
	 *        no_answer_delay_time|(13.)last_bridge_start|(14.)last_bridge_end|(
	 *        15.)
	 *        last_offered_call|(16.)last_status_change|(17.)no_answer_count|(18
	 *        .) calls_answered|(19.)talk_time|(20.)ready_time
	 * 
	 *        (1.)810|(2.)single_box|(3.)|(4.)callback|(5.)[call_timeout=10]user
	 *        /810|(6.) Available|(7.)
	 *        Waiting|(8.)20|(9.)10|(10).10|(11.)60|(12.)0|(13.)0|(14.)0|(15.)0|
	 *        (16.)1457946937|(17 .)0|(18.)0|(19.)0|(20.)0
	 * 
	 *        (END.) +OK
	 * @date 2016-03-16
	 * @author Lings
	 */
	public String getCcAgentList(String requester, String callee) {
		// :TODO judge requestor whether has permission.
		String cmd = "api callcenter_config agent list1" + ConstantCc.FS_CMD_TAIL;
		StringBuffer buff = new StringBuffer();

		task.setSendCmd(cmd);
		Future<String> result = FsCcServer.executor.submit(task);
		try {
			String res = result.get(5000, TimeUnit.MILLISECONDS);
			String[] parts = res.split("\n");
			for (String p : parts) {
				if (p.contains("|")) {
					String[] subs = p.split("\\|");
					StringBuffer sb = new StringBuffer();
					sb.append(subs[0]).append(":").append(subs[5]).append(":").append(subs[6]).append("\n");
					buff.append(sb);
				} else {
					logger.debug("last line , do nothing.");
				}
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			logger.debug("cmd response time-out...");
			e.printStackTrace();
		}
		return buff.toString();
	}
}
