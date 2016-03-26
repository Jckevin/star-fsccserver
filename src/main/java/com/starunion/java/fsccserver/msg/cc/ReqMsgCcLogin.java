package com.starunion.java.fsccserver.msg.cc;

import com.starunion.java.fsccserver.msg.MsgBase;

public class ReqMsgCcLogin extends MsgBase {

	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
