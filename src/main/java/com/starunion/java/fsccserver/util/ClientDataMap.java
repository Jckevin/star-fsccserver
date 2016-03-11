package com.starunion.java.fsccserver.util;

import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import com.starunion.java.fsccserver.po.ClientStatusInfoCc;
import com.starunion.java.fsccserver.thread.TcpClientSocketThread;

/**
 * @author Lings
 * @date Mar 1, 2016 11:41:36 AM
 * 
 */
public class ClientDataMap {
	
	/**
	 * client status structure 
	 * @param key: Client Identifier Number. 
	 * Like work Number, telephone Number etc.
	 * <p>
	 * @param value: @see class ClientStatusInfo
	 */
	public static ConcurrentHashMap<String, ClientStatusInfoCc> clientStatusMap = new ConcurrentHashMap<String, ClientStatusInfoCc>(0);
	
	/**
	 * client socket structure 
	 * @param key: Client Identifier Number. 
	 * Like work Number, telephone Number etc.
	 * <p>
	 * @param value: Socket
	 */
	public static ConcurrentHashMap<String, Socket> clientSocketMap = new ConcurrentHashMap<String, Socket>(0);
	
	/**
	 * client thread structure 
	 * @param key: Client Identifier Number. 
	 * Like work Number, telephone Number etc.
	 * <p>
	 * @param value: TcpClientSocketThread
	 */
	public static ConcurrentHashMap<String, TcpClientSocketThread> clientThreadMap = new ConcurrentHashMap<String, TcpClientSocketThread>(0);
//	public static ConcurrentHashMap<String, Thread> clientThreadMap = new ConcurrentHashMap<String, Thread>(0);

	/**
	 * client request queue 
	 * @param key: Client Identifier Number. 
	 * Like work Number, telephone Number etc.
	 * <p>
	 * @param value: String
	 */
	public static ConcurrentLinkedQueue<String> clientRequestQueue = new ConcurrentLinkedQueue<String>();
	
	/**
	 * client response queue 
	 * @param key: Client Identifier Number. 
	 * Like work Number, telephone Number etc.
	 * <p>
	 * @param value: String
	 */
	public static ConcurrentLinkedQueue<String> clientResponseQueue = new ConcurrentLinkedQueue<String>();
	
	/**
	 * to FreeSWITCH server request queue 
	 * @param key: Client Identifier Number. 
	 * Like work Number, telephone Number etc.
	 * <p>
	 * @param value: String
	 */
	public static ConcurrentLinkedQueue<String> fsCmdRequestQueue = new ConcurrentLinkedQueue<String>();
	
	/**
	 * for FreeSWITCH server response queue 
	 * @param key: Client Identifier Number. 
	 * Like work Number, telephone Number etc.
	 * <p>
	 * @param value: String
	 */
	public static ConcurrentLinkedQueue<String> fsCmdResponseQueue = new ConcurrentLinkedQueue<String>();
	
	/**
	 * for FreeSWITCH server notify receive queue 
	 * @param String:simple make the 
	 */
//	public static ConcurrentLinkedQueue<String> fsCmdNotifyQueue = new ConcurrentLinkedQueue<String>();
	public static BlockingQueue<Map<String,String>> fsNotifyRecvQueue = new LinkedBlockingQueue<Map<String,String>>();
	public static BlockingQueue<String> fsNotifySendQueue = new LinkedBlockingQueue<String>();

}
