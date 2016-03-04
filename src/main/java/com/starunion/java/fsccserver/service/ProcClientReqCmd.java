package com.starunion.java.fsccserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.util.ConstantCc;

/** 
* @author Lings  
* @date Mar 4, 2016 5:38:54 PM 
* 
*/
@Service
public class ProcClientReqCmd {
	private static final Logger logger = LoggerFactory.getLogger(ProcClientReqCmd.class);

	public ProcClientReqCmd(){
		
	}
	
	public int procCmdCTD(String caller,String callee){
		StringBuffer buff = new StringBuffer();
		buff.append("bgapi originate {origination_caller_id_number=000}user/");
		buff.append(caller);
		buff.append(" &bridge(user/");
		buff.append(callee);
		buff.append(")");
		buff.append(ConstantCc.DISP_FSCMD_TAIL);
//		fsSocket.fsSendCommand(buff.toString());
		logger.debug("make command : {}", buff.toString());
		
		return 0;
	}
}
