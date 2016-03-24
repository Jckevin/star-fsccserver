package com.starunion.java.fsccserver.po.fs;

/**
 * @author Lings
 * @date Mar 24, 2016 6:06:15 PM
 * 
 */
public class CallCenterCallInfo {

	private String queue;
	private String system;
	private String uuid;
	private String session_uuid;
	private String cid_number;
	private String cid_name;
	private int system_epoch;
	private int joined_epoch;
	private int rejoined_epoch;
	private int bridge_epoch;
	private int abandoned_epoch;
	private int base_score;
	private int skill_score;
	private String serving_agent;
	private String serving_system;
	private String state;

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getSession_uuid() {
		return session_uuid;
	}

	public void setSession_uuid(String session_uuid) {
		this.session_uuid = session_uuid;
	}

	public String getCid_number() {
		return cid_number;
	}

	public void setCid_number(String cid_number) {
		this.cid_number = cid_number;
	}

	public String getCid_name() {
		return cid_name;
	}

	public void setCid_name(String cid_name) {
		this.cid_name = cid_name;
	}

	public int getSystem_epoch() {
		return system_epoch;
	}

	public void setSystem_epoch(int system_epoch) {
		this.system_epoch = system_epoch;
	}

	public int getJoined_epoch() {
		return joined_epoch;
	}

	public void setJoined_epoch(int joined_epoch) {
		this.joined_epoch = joined_epoch;
	}

	public int getRejoined_epoch() {
		return rejoined_epoch;
	}

	public void setRejoined_epoch(int rejoined_epoch) {
		this.rejoined_epoch = rejoined_epoch;
	}

	public int getBridge_epoch() {
		return bridge_epoch;
	}

	public void setBridge_epoch(int bridge_epoch) {
		this.bridge_epoch = bridge_epoch;
	}

	public int getAbandoned_epoch() {
		return abandoned_epoch;
	}

	public void setAbandoned_epoch(int abandoned_epoch) {
		this.abandoned_epoch = abandoned_epoch;
	}

	public int getBase_score() {
		return base_score;
	}

	public void setBase_score(int base_score) {
		this.base_score = base_score;
	}

	public int getSkill_score() {
		return skill_score;
	}

	public void setSkill_score(int skill_score) {
		this.skill_score = skill_score;
	}

	public String getServing_agent() {
		return serving_agent;
	}

	public void setServing_agent(String serving_agent) {
		this.serving_agent = serving_agent;
	}

	public String getServing_system() {
		return serving_system;
	}

	public void setServing_system(String serving_system) {
		this.serving_system = serving_system;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
