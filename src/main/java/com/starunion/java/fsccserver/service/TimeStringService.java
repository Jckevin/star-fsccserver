package com.starunion.java.fsccserver.service;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

/**
 * @author Lings
 * @date Mar 24, 2016 2:12:13 PM
 * 
 */
@Service
public class TimeStringService {

	/**
	 * input date format : 2016-01-26 10-19-25
	 */
	public String dateFormatConvert(String idate) {
		StringBuffer sb = new StringBuffer();
		String[] parts = idate.split(" ");
		sb.append(parts[0]);
		sb.append(" ");
		String[] subParts = parts[1].split("-");
		sb.append(subParts[0]).append(":");
		sb.append(subParts[1]).append(":");
		sb.append(subParts[2]);
		return sb.toString();
	}

	/**
	 * input date format : 20160126101925
	 */

	public String String2TimestampFormat(String idate) {
		// :TODO add idate valiad check
		StringBuffer sb = new StringBuffer();
		sb.append(idate.substring(0, 4)).append("-");
		sb.append(idate.substring(4, 6)).append("-");
		sb.append(idate.substring(6, 8)).append(" ");
		sb.append(idate.substring(8, 10)).append(":");
		sb.append(idate.substring(10, 12)).append(":");
		sb.append(idate.substring(12, 14));
		return sb.toString();
	}

	public Timestamp String2Timestamp(String idate) {
		Timestamp ts = null;
		// :TODO add idate valiad check
		if (true) {
			StringBuffer sb = new StringBuffer();
			sb.append(idate.substring(0, 4)).append("-");
			sb.append(idate.substring(4, 6)).append("-");
			sb.append(idate.substring(6, 8)).append(" ");
			sb.append(idate.substring(8, 10)).append(":");
			sb.append(idate.substring(10, 12)).append(":");
			sb.append(idate.substring(12, 14));
			ts = Timestamp.valueOf(sb.toString());
		}

		return ts;
	}
}
