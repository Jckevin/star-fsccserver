package com.starunion.java.fsccserver.service.fs;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * @author Lings
 * @date Mar 11, 2016 9:30:08 AM
 * 
 */
@Service
public class FsNotifyMsgCheckService {

	public FsNotifyMsgCheckService() {

	}

	public Map<String, String> parseFsResponse(StringBuffer buff) {
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer line = new StringBuffer();
		for (int i = 0; i < buff.length(); i++) {
			char c;
			if ((c = buff.charAt(i)) != '\n') {
				line.append(c);
			} else {
				// logger.debug("Parse line = {}", line);
				String key = "";
				String value = "";
				int pos = line.indexOf(":");
				if (pos != -1) {
					// logger.debug("pos = {}", pos);
					key = line.substring(0, pos);
					// logger.debug("Parse key = {}", key);
					value = line.substring(pos + 1).trim();
					/**
					 * it seems urlDecode can be invoke when need the value
					 * within Map, but for logic design,here seems better.
					 */
					if (value.contains("%")) {
						String newVal;
						try {
							newVal = new String(java.net.URLDecoder.decode(value, "utf-8"));
							map.put(key, newVal);
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						// logger.debug("Parse newValue = {}", newVal);
					} else {
						// logger.debug("Parse value = {}", value);
						map.put(key, value);
					}
				}
				line.delete(0, line.length());
			}
		}
		return map;
	}
}
