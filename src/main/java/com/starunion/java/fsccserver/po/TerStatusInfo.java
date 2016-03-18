package com.starunion.java.fsccserver.po;

/**
 * @author Lings
 * @date Mar 18, 2016 10:49:38 AM
 * 
 */
public class TerStatusInfo {
	private String status;
	private String callType;
	private String callDirection;
	private String callUUid;
	private String peerNumber;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getCallDirection() {
		return callDirection;
	}

	public void setCallDirection(String callDirection) {
		this.callDirection = callDirection;
	}

	public String getCallUUid() {
		return callUUid;
	}

	public void setCallUUid(String callUUid) {
		this.callUUid = callUUid;
	}

	public String getPeerNumber() {
		return peerNumber;
	}

	public void setPeerNumber(String peerNumber) {
		this.peerNumber = peerNumber;
	}

}
