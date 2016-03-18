package com.starunion.java.fsccserver.po;

public class ClientStatusInfo {

	private String number;
	private int status;
	private int callType;
	private String uuidOwn;
	private String uuidPeer;
	private String callWith;

	public ClientStatusInfo() {

	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getUuidOwn() {
		return uuidOwn;
	}

	public void setUuidOwn(String uuidOwn) {
		this.uuidOwn = uuidOwn;
	}

	public String getUuidPeer() {
		return uuidPeer;
	}

	public void setUuidPeer(String uuidPeer) {
		this.uuidPeer = uuidPeer;
	}

	public String getCallWith() {
		return callWith;
	}

	public void setCallWith(String callWith) {
		this.callWith = callWith;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getCallType() {
		return callType;
	}

	public void setCallType(int callType) {
		this.callType = callType;
	}

}
