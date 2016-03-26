package com.starunion.java.fsccserver.msg.fs;

import com.starunion.java.fsccserver.msg.MsgBase;

/**
 * @author Lings
 * @date Mar 26, 2016 15:02:01 PM
 * 
 */

public class ReqMsgSysGenOper extends MsgBase{

	private String callee;

	public String getCallee() {
		return callee;
	}

	public void setCallee(String callee) {
		this.callee = callee;
	}
	
}
