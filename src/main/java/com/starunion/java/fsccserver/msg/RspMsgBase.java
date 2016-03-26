package com.starunion.java.fsccserver.msg;
/**
 * @author Lings
 * @date Mar 26, 2016 4:45:45 PM
 * 
 */
public class RspMsgBase extends MsgBase {

	public int getRspCode() {
		return rspCode;
	}

	public void setRspCode(int rspCode) {
		this.rspCode = rspCode;
	}

	public String getRspReason() {
		return rspReason;
	}

	public void setRspReason(String rspReason) {
		this.rspReason = rspReason;
	}

	private int rspCode;
	private String rspReason;

}
