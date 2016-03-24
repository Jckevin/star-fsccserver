package com.starunion.java.fsccserver.util;

/**
 * @author Lings
 * @date Mar 24, 2016 5:32:31 PM
 * 
 */
public class ConstantReference {
	// freeswitch cc navie status
	public String FS_AGENT_SIGN_IN = "Available";
	public String FS_AGENT_SIGN_OUT = "Logged Out";
	public String FS_AGENT_SIGN_BREAK = "On Break";
	public String FS_AGENT_SIGN_DEMAND = "Available (On Demand)";
	public String FS_AGENT_TYPE_ONHOOK = "callback";
	public String FS_AGENT_TYPE_OFFHOOK = "uuid-standby";
	public String FS_AGENT_STATUS_IDLE = "Idle";
	public String FS_AGENT_STATUS_WAIT = "Waiting";
	public String FS_AGENT_STATUS_RECV = "Receiving";
	public String FS_AGENT_STATUS_INQUECALL = "In a queue call";

	// for mysql operate result code [dbutils]
	public int DB_SUCCESS = 1;
	public int DB_FAIL_INSERT = -101;
	public int DB_FAIL_BATCH_INSERT = -102;

	// for json result code
	public int LOGIN_NO_USER = -1;
	public int LOGIN_ERR_PWD = -2;
	public int NUMBER_EXISTED = -3;

	// for user add function
	public int TERTYPE_DIS = 0;
	public int TERTYPE_BRO = 1;
	public String PWD_SAME_NAME = "0";
	public String PWD_STATIC_STR = "1";

}
