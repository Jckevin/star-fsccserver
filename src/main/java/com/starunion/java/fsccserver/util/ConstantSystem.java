package com.starunion.java.fsccserver.util;

/**
 * @author Lings
 * @date Feb 25, 2016 10:56:55 AM
 * 
 */
public class ConstantSystem {

	// for the project
	public final static int TCP_SERV_PORT = 6099;
	public final static int FS_SERV_PORT = 8021;

	public final static int SUCCESS = 0;
	public final static int FAILED = -1;
	public final static String CODEC_UTF8 = "utf-8";

	// client connect thread delayed timeout 1 minite
	public final static int CLT_TIMEOUT_SOCK = 1000 * 60;

	// for freeswitch
	public final static String FS_EVENT_HEAD = "Event-Name";
	public final static String FS_CMD_TAIL = "\n\n";
	public final static String FS_CMD_SUCC = "+OK";
	public final static String FS_DEF_NUMBER = "000";
	public final static int FS_TIMEOUT_CMD = 1000 * 5;

	// for terminal info
	public final static String TER_STATUS_UNREG = "0";
	public final static String TER_STATUS_REGED = "1";
	public final static String TER_STATUS_START_CALL = "2";
	public final static String TER_STATUS_RING = "3";
	public final static String TER_STATUS_EARLY = "4";
	public final static String TER_STATUS_CONN = "5";

	// public system command params
	public final static int SYS_REG_MSG_LMT = 3;
	public final static String SYS_SPLIT = ":";
	public final static String SYS_TAIL_SUCC = "OK";
	public final static String SYS_TAIL_FAIL = "FAIL";
	public final static String SYS_TAIL_END = "\r\n";
	// public system API (query)
	public final static String SYS_QUERY_TER_INFO = "sysQueryTerInfo";
	public final static String SYS_QUERY_STATISTICS_CALL_INFO = "sysQueryStatisticsCallInfo";
	public final static String STATISTIC_SESSION = "sessionCount";
	
	// public system API (command)
	public final static String SYS_EXEC_CTD = "sysExecCtd";
	public final static String SYS_EXEC_MONITOR = "sysExecMonitor";
	public final static String SYS_EXEC_INSERT = "sysExecInsert";
	public final static String SYS_EXEC_DEMOLITSH = "sysExecDemolish";
	public final static String SYS_EXEC_BRIDGE = "sysExecBridge";
	public final static String SYS_EXEC_INTERCEPT = "sysExecIntercept";
	public final static String SYS_EXEC_HANGUP = "sysExecHangup";
	public final static String SYS_EXEC_RECORD = "sysExecRecord";

	// public system API (notify)
	/** e.g. sysNotifyTerStatus:800:0\r\n */
	public final static String SYS_NOTIFY_TER_STATUS = "sysNotifyTerStatus";
	/** e.g. sysNotifyServerUnbind\r\n */
	public final static String SYS_NOTIFY_SERVER_UNBIND = "sysNotifyServerUnbind";

	// CC MESSAGE TYPE
	public final static String CC_LOG_IN = "ccLogin";
	public final static String CC_LOG_OUT = "ccLogOut";
	public final static String CC_AGENT_QRY = "ccAgentQuery";
	// for agent sign in and out
	public final static String CC_AGENT_SIGN = "ccAgentSign";
	public final static String CC_AGENT_SIGN_IN = "1";
	public final static String CC_AGENT_SIGN_OUT = "2";
	public final static String CC_AGENT_SIGN_BUSY = "3";
	public final static String FS_AGENT_SIGN_IN = "Available";
	public final static String FS_AGENT_SIGN_OUT = "Logged Out";
	public final static String FS_AGENT_SIGN_BREAK = "On Break";
}
