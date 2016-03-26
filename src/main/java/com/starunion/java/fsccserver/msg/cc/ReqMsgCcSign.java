package com.starunion.java.fsccserver.msg.cc;

import com.starunion.java.fsccserver.msg.MsgBase;
/**
 * @author Lings
 * @date Mar 26, 2016 15:16:58 PM
 * 
 */
public class ReqMsgCcSign extends MsgBase {

	private String caller;
	private String status;

	public String getCaller() {
		return caller;
	}

	public void setCaller(String caller) {
		this.caller = caller;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
