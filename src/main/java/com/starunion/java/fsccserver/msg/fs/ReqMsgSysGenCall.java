package com.starunion.java.fsccserver.msg.fs;

import com.starunion.java.fsccserver.msg.MsgBase;

public class ReqMsgSysGenCall extends MsgBase {

	private String caller;
	private String callee;
	
	public String getCaller() {
		return caller;
	}

	public void setCaller(String caller) {
		this.caller = caller;
	}

	public String getCallee() {
		return callee;
	}

	public void setCallee(String callee) {
		this.callee = callee;
	}

}
