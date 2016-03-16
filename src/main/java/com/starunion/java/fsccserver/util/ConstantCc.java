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

	// CLIENT REQUEST COMMAND TYPE
	// client connect thread delayed timeout 1 minite
	public final static int TIMEOUT_CLT_SOCK = 1000 * 30;
	
	// CC MESSAGE TYPE
	public final static int CC_REQ_PARTS_LMT = 3;
	public final static String CC_REQ_PARTS_SPLIT = ":";

	public final static String CC_LOG_IN = "ccLogin";
	public final static String CC_LOG_OUT = "ccLogOut";
	public final static String CC_CTD = "ccCtd";

	public final static String CC_AGENT_QRY = "ccAgentQuery";
	
	public final static String CC_AGENT_TYPE_ONHOOK = "callback";
	public final static String CC_AGENT_TYPE_OFFHOOK = "uuid-standby";
	
	public final static String CC_AGENT_LOG_IN = "ccAgentLogIn";
	public final static String CC_AGENT_LOG_OUT = "ccAgentLogOut";
	public final static String CC_AGENT_LOG_DEMAND = "ccAgentLogDemand";
	public final static String CC_AGENT_LOG_BREAK = "ccAgentLogBreak";
	
	public final static String CC_AGENT_STATUS_IDLE = "Idle";
	public final static String CC_AGENT_STATUS_WAIT = "Waiting";
	public final static String CC_AGENT_STATUS_RECV = "Receiving";
	public final static String CC_AGENT_STATUS_INQUECALL = "In a queue call";

	public final static String DISP_TAIL_SUCC = ":OK\r\n";
	public final static String DISP_TAIL_FAIL = ":FAIL\r\n";
	

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
