package com.starunion.java.fsccserver.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class StarallyUtils {
	
	public StarallyUtils(){
		
	}
	
	public boolean isDate(String date) {
		/**
		 * 判断日期格式和范围
		 */
		String rexp = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";

		Pattern pat = Pattern.compile(rexp);

		Matcher mat = pat.matcher(date);

		boolean dateType = mat.matches();

		return dateType;
	}
	public boolean isSimpleDate(String date) {
		/**
		 * 判断日期格式和范围
		 */
		String rexp = "[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}-[0-9]{2}-[0-9]{2}";

		Pattern pat = Pattern.compile(rexp);

		Matcher mat = pat.matcher(date);

		boolean dateType = mat.matches();

		return dateType;
	}
	/**
	 * input date format : 2016-01-26 10-19-25
	 * */
	public String dateFormatConvert(String idate){
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
	 * */
	public String dateFormatConvertS(String idate){
		StringBuffer sb = new StringBuffer();
		sb.append(idate.substring(0,4)).append("-");
		sb.append(idate.substring(4,6)).append("-");
		sb.append(idate.substring(6,8)).append(" ");
		sb.append(idate.substring(8,10)).append(":");
		sb.append(idate.substring(10,12)).append(":");
		sb.append(idate.substring(12,14));
		return sb.toString();
	}
}
