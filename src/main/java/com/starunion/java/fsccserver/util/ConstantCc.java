package com.starunion.java.fsccserver.util;

/**
 * @author Lings
 * @date Feb 25, 2016 10:56:55 AM
 * 
 */
public class ConstantCc {

	// for the project
	public final static int TCP_SERV_PORT = 6099;
	public final static int FS_SERV_PORT = 8021;

	public final static int SUCCESS = 0;
	public final static int FAILED = -1;
	public final static String CODEC_UTF8 = "utf-8";
	
	// for freeswitch
	public final static String FS_EVENT_HEAD = "Event-Name";
	public final static String FS_EVENT_UNBIND = "UnbindServer";
	public final static String FS_CMD_TAIL = "\n\n";
	public final static String FS_CMD_SUCC = "+OK";
	public final static String FS_DEF_NUMBER = "000";
	
	// CLIENT REQUEST COMMAND TYPE
	// client connect thread delayed timeout 1 minite
	public final static int TIMEOUT_CLT_SOCK = 1000 * 30;
	
	// CC MESSAGE TYPE
	public final static int CC_REQ_PARTS_LMT = 3;
	public final static String CC_REQ_PARTS_SPLIT = ":";
	public final static String CC_SUCC_TAIL = ":OK\r\n";
	public final static String CC_FAIL_TAIL = ":FAIL\r\n";
	
	public final static String CC_LOG_IN = "ccLogin";
	public final static String CC_LOG_OUT = "ccLogOut";
	public final static String CC_CTD = "ccCtd";
	public final static String CC_MONITOR = "ccMonitor";

	public final static String CC_AGENT_QRY = "ccAgentQuery";
	// for agent sign in and out
	public final static String CC_AGENT_SIGN = "ccAgentSign";
	public final static String CC_AGENT_SIGN_IN = "1";
	public final static String CC_AGENT_SIGN_OUT = "2";
	public final static String CC_AGENT_SIGN_BUSY = "3";
	// same as freeswitch navie status
	public final static String FS_AGENT_SIGN_IN = "Available";
	public final static String FS_AGENT_SIGN_OUT = "Logged Out";
	public final static String FS_AGENT_SIGN_BREAK = "On Break";
	public final static String FS_AGENT_TYPE_ONHOOK = "callback";
	public final static String FS_AGENT_TYPE_OFFHOOK = "uuid-standby";
	public final static String FS_AGENT_STATUS_IDLE = "Idle";
	public final static String FS_AGENT_STATUS_WAIT = "Waiting";
	public final static String FS_AGENT_STATUS_RECV = "Receiving";
	public final static String FS_AGENT_STATUS_INQUECALL = "In a queue call";
	
	// for mysql operate result code [dbutils]
	public final static int DB_SUCCESS = 1;
	public final static int DB_FAIL_INSERT = -101;
	public final static int DB_FAIL_BATCH_INSERT = -102;

	// for json result code
	public final static int LOGIN_NO_USER = -1;
	public final static int LOGIN_ERR_PWD = -2;
	public final static int NUMBER_EXISTED = -3;

	// for user add function
	public final static int TERTYPE_DIS = 0;
	public final static int TERTYPE_BRO = 1;
	public final static String PWD_SAME_NAME = "0";
	public final static String PWD_STATIC_STR = "1";

}
