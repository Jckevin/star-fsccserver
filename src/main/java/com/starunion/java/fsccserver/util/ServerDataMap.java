package com.starunion.java.fsccserver.util;

import java.util.concurrent.ConcurrentHashMap;

import com.starunion.java.fsccserver.po.TerParkingInfo;
import com.starunion.java.fsccserver.po.TerStatusInfo;

/** 
* @author Lings  
* @date Mar 18, 2016 10:55:54 AM 
* 
*/
public class ServerDataMap {
	/**
	 * terminal status structure 
	 * @param key: Client Identifier Number. 
	 * Like work Number, telephone Number etc.
	 * <p>
	 * @param value: @see class TerStatusInfo
	 */
	public static ConcurrentHashMap<String, TerStatusInfo> terStatusMap = new ConcurrentHashMap<String, TerStatusInfo>(0);
	
	/**
	 * terminal parking map 
	 * @param key: Client Identifier Number. 
	 * Like work Number, telephone Number etc.
	 * <p>
	 * @param value: @see class TerParkingInfo
	 */
	public static ConcurrentHashMap<String, TerParkingInfo> terParkingMap = new ConcurrentHashMap<String, TerParkingInfo>(0);
	
}
