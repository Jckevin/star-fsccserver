package com.starunion.java.fsccserver.msg.fs;

import com.starunion.java.fsccserver.msg.MsgBase;

/**
 * @author Lings
 * @date Mar 26, 2016 16:15:58 PM
 * 
 */
public class ReqMsgSysCallSatistics extends MsgBase {

	private String timeStart;
	private String timeEnd;

	public String getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

}
