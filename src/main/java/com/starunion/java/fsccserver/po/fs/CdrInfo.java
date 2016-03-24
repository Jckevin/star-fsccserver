package com.starunion.java.fsccserver.po.fs;

import java.sql.Date;

/**
 * @author Lings
 * @date Mar 24, 2016 10:10:51 AM
 * 
 */

public class CdrInfo {
	private String caller_id_name;
	private String caller_id_number;
	private String destination_number;
	private String context;
	private Date start_stamp;
	private Date answer_stamp;
	private Date end_stamp;
	private int duration;
	private int billsec;
	private String hangup_cause;
	private String uuid;
	private String bleg_uuid;
	private String account_code;

	public String getCaller_id_name() {
		return caller_id_name;
	}

	public void setCaller_id_name(String caller_id_name) {
		this.caller_id_name = caller_id_name;
	}

	public String getCaller_id_number() {
		return caller_id_number;
	}

	public void setCaller_id_number(String caller_id_number) {
		this.caller_id_number = caller_id_number;
	}

	public String getDestination_number() {
		return destination_number;
	}

	public void setDestination_number(String destination_number) {
		this.destination_number = destination_number;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public Date getStart_stamp() {
		return start_stamp;
	}

	public void setStart_stamp(Date start_stamp) {
		this.start_stamp = start_stamp;
	}

	public Date getAnswer_stamp() {
		return answer_stamp;
	}

	public void setAnswer_stamp(Date answer_stamp) {
		this.answer_stamp = answer_stamp;
	}

	public Date getEnd_stamp() {
		return end_stamp;
	}

	public void setEnd_stamp(Date end_stamp) {
		this.end_stamp = end_stamp;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getBillsec() {
		return billsec;
	}

	public void setBillsec(int billsec) {
		this.billsec = billsec;
	}

	public String getHangup_cause() {
		return hangup_cause;
	}

	public void setHangup_cause(String hangup_cause) {
		this.hangup_cause = hangup_cause;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getBleg_uuid() {
		return bleg_uuid;
	}

	public void setBleg_uuid(String bleg_uuid) {
		this.bleg_uuid = bleg_uuid;
	}

	public String getAccount_code() {
		return account_code;
	}

	public void setAccount_code(String account_code) {
		this.account_code = account_code;
	}

}
